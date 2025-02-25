package nutricelia.com.OpenFoodFactsAPI;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;


@Data
public class Nutriments {

    private float carbohydrates;

    @JsonProperty("carbohydrates_100g")
    private float carbohydrates100G;

    @JsonProperty("carbohydrates_unit")
    private String carbohydratesUnit;

    private int energy;

    @JsonProperty("energy_100g")
    private int energy100G;

    @JsonProperty("energy-kcal_100g")
    private int energyKcal100G;

    @JsonProperty("energy-kj_100g")
    private int energyKj100G;

    @JsonProperty("energy_unit")
    private String energyUnit;

    @JsonProperty("energy-kcal_unit")
    private String energyKcalUnit;

    @JsonProperty("energy-kj_unit")
    private String energyKjUnit;

    private float fat;

    @JsonProperty("fat_100g")
    private float fat100G;

    @JsonProperty("fat_unit")
    private String fatUnit;

    private float proteins;

    @JsonProperty("proteins_100g")
    private float proteins100G;

    @JsonProperty("proteins_unit")
    private String proteinsUnit;

    private float salt;

    @JsonProperty("salt_100g")
    private float salt100G;

    @JsonProperty("salt_unit")
    private String saltUnit;

    @JsonProperty("saturated-fat")
    private float saturatedFat;

    @JsonProperty("saturated-fat_100g")
    private float saturatedFat100G;

    @JsonProperty("saturated-fat_unit")
    private String saturatedFatUnit;

    private float sugars;

    @JsonProperty("sugars_100g")
    private float sugars100G;

    @JsonProperty("sugars_unit")
    private String sugarsUnit;

    Map<String, Object> other = new LinkedHashMap<>();

    @JsonAnySetter
    void setDetail(String key, Object value) {
        other.put(key, value);
    }
}
