package nutricelia.com;

import java.util.List;

public class ProductEntity {

    private String productName;  // Usaremos el nombre del producto como ID

    private List<String> brandsTags;

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