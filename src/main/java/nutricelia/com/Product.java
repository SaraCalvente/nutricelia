package nutricelia.com;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Product {

    @JsonProperty("product_name")
    private String productName;

    @JsonProperty("brands_tags")
    private List<String> brandsTags;

    @JsonProperty("categories_tags")
    private List<String> categoriesTags;

    // Getters y setters
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public List<String> getBrandsTags() {
        return brandsTags;
    }

    public void setBrandsTags(List<String> brandsTags) {
        this.brandsTags = brandsTags;
    }

    public List<String> getCategoriesTags() {
        return categoriesTags;
    }

    public void setCategoriesTags(List<String> categoriesTags) {
        this.categoriesTags = categoriesTags;
    }
}