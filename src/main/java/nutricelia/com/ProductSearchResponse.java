package nutricelia.com;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductSearchResponse {

    private List<Product> products;
    private int count;
    private int page;
    private int pageCount;
    private int pageSize;
    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Product {
        public String product_name;
        public List<String> brands_tags;
        public List<String> allergens_tags;
        public List<String> categories_tags;
        public Nutriments nutriments;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Nutriments {
        public Double carbohydrates_100g;
        @JsonProperty("energy-kcal_100g")
        public Double energyKcal100g;
        public Double fat_100g;
        public Double proteins_100g;
        public Double salt_100g;
        public Double sugars_100g;
        public Double carbohydrates_serving;
        @JsonProperty("energy-kcal_serving")
        public Double energyKcalServing;
        public Double fat_serving;
        public Double proteins_serving;
        public Double salt_serving;
        public Double sugars_serving;
    }
}