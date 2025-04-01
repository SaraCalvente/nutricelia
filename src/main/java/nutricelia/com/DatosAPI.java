package nutricelia.com;

import org.jose4j.json.internal.json_simple.JSONArray;
import org.jose4j.json.internal.json_simple.JSONObject;

import java.sql.*;
import java.util.Properties;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatosAPI {



    public static void main(String[] args) {
        try {

            String databaseFile = "/Users/saracalvente/Downloads/food.parquet";
            Properties connectionProperties = new Properties();
            connectionProperties.setProperty("temp_directory", "/Users/saracalvente/Downloads/openfoodfactsdir/");

            Connection conn = DriverManager.getConnection("jdbc:duckdb:", connectionProperties);
            Connection c = DriverManager.getConnection("jdbc:duckdb:", connectionProperties);
            Statement stmt = conn.createStatement();

            // Configuración de la conexión PostgreSQL
            String dbUrl = "jdbc:postgresql://db-aules.uji.es:5432/nutricelia?characterEncoding=UTF-8";
            String dbUser = "al415706";
            String dbPassword = "20970145X";

            Connection connPostgres = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            connPostgres.setAutoCommit(false);

            // Consulta SQL para filtrar productos de Mercadona con los campos requeridos
            String query = """
            WITH expanded_nutrients AS (
                SELECT 
                    code,
                    array_extract(product_name, 1)->>'text' AS product_name,
                    array_extract(brands_tags,1) AS brands_tags,
                    string_to_array(categories, ', ') AS category,
                    unnest(nutriments) AS nutrient,
                    images,
                    allergens_tags
                FROM 
                    '%s'
                WHERE 
                    list_contains(stores_tags, 'mercadona')
            )
            SELECT 
                code,
                product_name,
                brands_tags,
                category,
                images,
                MAX(CASE WHEN nutrient.name = 'carbohydrates' THEN nutrient.value END) AS carbohydrates_100g,
                MAX(CASE WHEN nutrient.name = 'proteins' THEN nutrient.value END) AS proteins_100g,
                MAX(CASE WHEN nutrient.name = 'sugars' THEN nutrient.value END) AS sugars_100g,
                MAX(CASE WHEN nutrient.name = 'energy-kcal' THEN nutrient.value END) AS energy_kcal_100g,  
                MAX(CASE WHEN nutrient.name = 'fat' THEN nutrient.value END) AS fat_100g,        
                MAX(CASE WHEN nutrient.name = 'salt' THEN nutrient.value END) AS salt_100g,    
           
                allergens_tags
            FROM 
                expanded_nutrients
            GROUP BY 
                code, product_name, brands_tags,category,images, allergens_tags
            ;
        """.formatted(databaseFile);

            String query1 = """
                        SELECT
                        array_extract(product_name, 1)->>'text' AS product_name,                        
                        images,
                        code
                        FROM '%s' WHERE list_contains(stores_tags, 'mercadona') LIMIT 5;
                    """.formatted(databaseFile);

            try (ResultSet rs = stmt.executeQuery(query)) {
                handleResult(rs, connPostgres);
            }
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void handleResult(ResultSet rs, Connection connPostgres) throws Exception {
        int productCount = 0;
        int id = 0;

        while (rs.next()) {
            productCount++;
            id++;
            var productName = rs.getString("product_name");
            var brands = rs.getString("brands_tags");
            var allergens = rs.getString("allergens_tags");
            var code = rs.getString("code");
            Array imagesArray = rs.getArray("images");

            Array categoriesArray = rs.getArray("category");
            String categories = getCategories(categoriesArray);
            String URL = getUrlImage(imagesArray, code);

            if (productName == null) {
                productName = "Sin nombre"; // O usa "" para cadena vacía
            }
            if (brands == null) {
                brands = "Sin marca"; // O usa "" para cadena vacía
            }
            if (categories == null) {
                categories = "Sin categoria"; // O usa "" para cadena vacía
            }
            boolean containsGluten = false;

            if (allergens != null) {
                String cleanedAllergens = allergens.replaceAll("[\\[\\]\"]", "")
                        .replaceAll("en:", "")
                        .toLowerCase();

                containsGluten = cleanedAllergens.contains("gluten");
            }
            else {
                containsGluten = false;
            }

            System.out.println("-------------------------");
            System.out.println("Product Name: " + productName);
            System.out.println("Brands: " + brands);
            System.out.println("Allergens: " + allergens);
            System.out.println("Categories: " + categories);
            System.out.println("URL Imagen: " + URL);

            String insertSQL = """
                INSERT INTO producto (id, nombre, singluten, marca, categoria, urlImagen)
                VALUES (?, ?, ?, ?, ?, ?)
                ON CONFLICT (id) 
                DO UPDATE 
                SET nombre = EXCLUDED.nombre, 
                    marca = EXCLUDED.marca, 
                    singluten = EXCLUDED.singluten,
                    categoria = EXCLUDED.categoria,
                    urlImagen = EXCLUDED.urlImagen
                RETURNING id;
            """;

            try (PreparedStatement ps = connPostgres.prepareStatement(insertSQL)) {
                ps.setInt(1, id);
                ps.setString(2, productName);
                ps.setInt(3, containsGluten ? 1 : 0);
                ps.setString(4, brands);
                ps.setString(5, categories);
                ps.setString(6, URL);

                try (ResultSet rsId = ps.executeQuery()) {
                    if (rsId.next()) {
                        int productId = rsId.getInt("id");
                        handleResult1(rs, connPostgres, productId);

                        System.out.println("Producto insertado con ID: " + productId);
                    }
                }            }
        }
        connPostgres.commit();

        System.out.println("TOTAL PRODUCTOS: " + productCount);
        connPostgres.close();


    }

    private static String getCategories(Array categoriesArray) throws SQLException {
        var categories = "Sin categorias";
        if (categoriesArray != null) {
            Object[] categoryObjects = (Object[]) categoriesArray.getArray();
            String[] categoryElements = new String[categoryObjects.length];

            for (int i = 0; i < categoryObjects.length; i++) {
                categoryElements[i] = categoryObjects[i].toString();
            }

            categories = String.join(", ", categoryElements);
        }
        return categories;
    }

    private static String getUrlImage(Array imagesArray, String code) throws SQLException {
        String images = "";

        if (imagesArray != null) {
            Object[] imageObjects = (Object[]) imagesArray.getArray();
            String[] imageElements = new String[imageObjects.length];
            for (int i = 0; i < imageObjects.length; i++) {
                imageElements[i] = imageObjects[i].toString();
            }

            images = String.join(", ", imageElements);
        }
        String[] imageArray = images.split("}, \\{");

        String revValue = "No disponible";
        String keyValue = "No disponible";
        boolean foundFrontImage = false;
        for (String image : imageArray) {
            if (image.contains("key=front_es") || image.contains("key=front_en")) {
                foundFrontImage = true;

                String[] parts = image.split(", ");
                for (String part : parts) {
                    if (part.trim().startsWith("rev=") || part.trim().startsWith("{rev=")) {
                        revValue = part.split("=")[1];
                    }
                    if (part.trim().startsWith("key=")){
                        keyValue = part.split("=")[1];
                    }
                }
            }
        }

        if (!foundFrontImage) {
            System.out.println("\t No se encontró imagen con key=front_es");
        }
        String codigURL = getUrlCode(code);

        String URL = "https://images.openfoodfacts.org/images/products" + codigURL + keyValue + "." + revValue + ".400.jpg";
        return URL;
    }

    private static String getUrlCode (String code){
        while (code.length() < 13) {
            code = "0" + code;
        }
        String codigURL =  "/" + code.substring(0, 3) + "/" + code.substring(3, 6) + "/" +
                code.substring(6, 9) + "/" + code.substring(9, 13) + "/";
        return codigURL;
    }

    private static void handleResult1(ResultSet rs, Connection connPostgres, int productId) throws Exception {
        var carbohydrates = rs.getFloat("carbohydrates_100g");
        var proteins = rs.getFloat("proteins_100g");
        var sugars = rs.getFloat("sugars_100g");
        var energyKcal = rs.getFloat("energy_kcal_100g");  // Calorías
        var fat = rs.getFloat("fat_100g");                 // Grasas
        var salt = rs.getFloat("salt_100g");               // Sal

        String insertSQL = "INSERT INTO valornutricional (calorias_100g, proteinas_100g, grasas_100g, azucar_100g, carbohidratos_100g, sal_100g, id) VALUES (?, ?, ?, ?, ?, ?, ?)\n" +
                "ON CONFLICT (id) \n" +
                "DO UPDATE \n" +
                "SET carbohidratos_100g = EXCLUDED.carbohidratos_100g,\n" +
                "    proteinas_100g = EXCLUDED.proteinas_100g,\n" +
                "    azucar_100g = EXCLUDED.azucar_100g,\n" +
                "    calorias_100g = EXCLUDED.calorias_100g,\n" +
                "    grasas_100g = EXCLUDED.grasas_100g,\n" +
                "    sal_100g = EXCLUDED.sal_100g";

        try (PreparedStatement ps = connPostgres.prepareStatement(insertSQL)) {
            ps.setFloat(1, energyKcal);
            ps.setFloat(2, proteins);
            ps.setFloat(3, fat);
            ps.setFloat(4, sugars);
            ps.setFloat(5, carbohydrates);
            ps.setFloat(6, salt);
            ps.setInt(7, productId);


            ps.executeUpdate();
        }

        connPostgres.commit();
    }
}
