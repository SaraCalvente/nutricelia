package nutricelia.com.OpenFoodFactsAPI;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class Product {
    @JsonProperty("product_name")
    private String productName;

    private Nutriments nutriments;

    @JsonProperty("brands_tags")
    private String[] brandsTags;

    @JsonProperty("allergens_tags")
    private String[] allergensTags;

    Map<String, Object> other = new LinkedHashMap<>();

    @JsonAnySetter
    void setDetail(String key, Object value) {
        other.put(key, value);
    }
}