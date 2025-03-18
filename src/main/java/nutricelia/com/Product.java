package nutricelia.com;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {
    private String product_name;
    private String brands;
    private String categories;

    public String getProduct_name() { return product_name; }
    public void setProduct_name(String product_name) { this.product_name = product_name; }

    public String getBrands() { return brands; }
    public void setBrands(String brands) { this.brands = brands; }

    public String getCategories() { return categories; }
    public void setCategories(String categories) { this.categories = categories; }
}