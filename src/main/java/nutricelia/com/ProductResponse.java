package nutricelia.com;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class ProductResponse {
    @JsonProperty("products")
    private List<Product> products;

    public List<Product> getProducts() {
        return products;
    }


    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
