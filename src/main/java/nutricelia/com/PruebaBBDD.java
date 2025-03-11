package nutricelia.com;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class PruebaBBDD {

    public static void main(String[] args) {
        try {
            String databaseFile = "/Users/saracalvente/Downloads/food.parquet";
            Properties connectionProperties = new Properties();
            connectionProperties.setProperty("temp_directory", "/Users/saracalvente/Downloads/openfoodfactsdir/");

            Connection conn = DriverManager.getConnection("jdbc:duckdb:", connectionProperties);
            Statement stmt = conn.createStatement();

            // Consulta SQL para filtrar productos de Mercadona con los campos requeridos
            String query = """
                        WITH expanded_nutrients AS (
                            SELECT 
                                code,
                                array_extract(product_name, 1)->>'text' AS product_name,
                                brands_tags,
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
                            MAX(CASE WHEN nutrient.name = 'energy-kcal' THEN nutrient.value END) AS energy_kcal_100g,  -- Calorías
                            MAX(CASE WHEN nutrient.name = 'fat' THEN nutrient.value END) AS fat_100g,         -- Grasas
                            MAX(CASE WHEN nutrient.name = 'salt' THEN nutrient.value END) AS salt_100g,     -- Sal
                       
                            allergens_tags
                        FROM 
                            expanded_nutrients
                        GROUP BY 
                            code, product_name, brands_tags, allergens_tags
                        ;
                    """.formatted(databaseFile);

            try (ResultSet rs = stmt.executeQuery(query)) {
                handleResult(rs);
            }
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }



    private static void handleResult(ResultSet rs) throws Exception {
        int productCount = 0; // Inicializamos el contador de productos

        while (rs.next()) {
            productCount++;
            var code = rs.getString("code");
            var productName = rs.getString("product_name");
            var brands = rs.getString("brands_tags");
            var carbohydrates = rs.getFloat("carbohydrates_100g");
            var proteins = rs.getFloat("proteins_100g");
            var sugars = rs.getFloat("sugars_100g");
            var energyKcal = rs.getFloat("energy_kcal_100g");  // Calorías
            var fat = rs.getFloat("fat_100g");                 // Grasas
            var salt = rs.getFloat("salt_100g");               // Sal
            var allergens = rs.getString("allergens_tags");

            System.out.println("-------------------------");
            System.out.println("Code: " + code);
            System.out.println("Product Name: " + productName);
            System.out.println("Brands: " + brands);
            System.out.println("Carbohydrates (per 100g): " + carbohydrates);
            System.out.println("Proteins (per 100g): " + proteins);
            System.out.println("Sugars (per 100g): " + sugars);
            System.out.println("Energy (kcal per 100g): " + energyKcal);
            System.out.println("Fat (per 100g): " + fat);
            System.out.println("Salt (per 100g): " + salt);
            System.out.println("Allergens: " + allergens);
        }
        System.out.println("Total de productos devueltos: " + productCount);

    }


}
