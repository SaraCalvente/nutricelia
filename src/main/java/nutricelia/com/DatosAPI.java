package nutricelia.com;

import java.sql.*;
import java.util.Properties;

public class DatosAPI {



    public static void main(String[] args) {
        try {

            String databaseFile = "/Users/saracalvente/Downloads/food.parquet";
            Properties connectionProperties = new Properties();
            connectionProperties.setProperty("temp_directory", "/Users/saracalvente/Downloads/openfoodfactsdir/");

            Connection conn = DriverManager.getConnection("jdbc:duckdb:", connectionProperties);
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
                    unnest(nutriments) AS nutrient,
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
                code, product_name, brands_tags, allergens_tags
            ;
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
            var brands = rs.getString("brands_tags");           // Sal
            var allergens = rs.getString("allergens_tags");

            if (productName == null) {
                productName = "Sin nombre"; // O usa "" para cadena vacía
            }
            if (brands == null) {
                brands = "Sin marca"; // O usa "" para cadena vacía
            }
            boolean containsGluten = false;

            if (allergens != null) {
                // Elimina corchetes y espacios en blanco
                String cleanedAllergens = allergens.replaceAll("[\\[\\]\"]", "")
                        .replaceAll("en:", "")
                        .toLowerCase();

                // Verifica si contiene la palabra "gluten"
                containsGluten = cleanedAllergens.contains("gluten");
            }
            else {
                containsGluten = false;
            }

            System.out.println("-------------------------");
            System.out.println("Product Name: " + productName);
            System.out.println("Brands: " + brands);
            System.out.println("Allergens: " + allergens);

            // Inserción en la tabla productos
            String insertSQL = """
                INSERT INTO producto (id, nombre, singluten, marca)
                VALUES (?, ?, ?, ?)
                ON CONFLICT (id) 
                DO UPDATE 
                SET nombre = EXCLUDED.nombre, 
                    marca = EXCLUDED.marca, 
                    singluten = EXCLUDED.singluten
                RETURNING id;
            """;


            try (PreparedStatement ps = connPostgres.prepareStatement(insertSQL)) {
                ps.setInt(1, id);
                ps.setString(2, productName);
                ps.setInt(3, containsGluten ? 1 : 0);
                ps.setString(4, brands);


                // Ejecutar la inserción
                try (ResultSet rsId = ps.executeQuery()) { // Usa executeQuery() para manejar RETURNING
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

    private static void handleResult1(ResultSet rs, Connection connPostgres, int productId) throws Exception {
        int productCount = 0; // Inicializamos el contador de productos

            productCount++;
            var carbohydrates = rs.getFloat("carbohydrates_100g");
            var proteins = rs.getFloat("proteins_100g");
            var sugars = rs.getFloat("sugars_100g");
            var energyKcal = rs.getFloat("energy_kcal_100g");  // Calorías
            var fat = rs.getFloat("fat_100g");                 // Grasas
            var salt = rs.getFloat("salt_100g");               // Sal


            System.out.println("Carbohydrates (per 100g): " + carbohydrates);
            System.out.println("Proteins (per 100g): " + proteins);
            System.out.println("Sugars (per 100g): " + sugars);
            System.out.println("Energy (kcal per 100g): " + energyKcal);
            System.out.println("Fat (per 100g): " + fat);
            System.out.println("Salt (per 100g): " + salt);

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


                // Ejecutar la inserción
                ps.executeUpdate();
            }

        connPostgres.commit();


    }




}
