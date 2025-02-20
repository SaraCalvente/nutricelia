package nutricelia.com.Modelo;

import okhttp3.Response;
import pl.coderion.model.*;
import pl.coderion.service.OpenFoodFactsWrapper;
import pl.coderion.service.impl.OpenFoodFactsWrapperImpl;

public class Main {
    public static void main(String[] args) {
        OpenFoodFactsWrapper wrapper = new OpenFoodFactsWrapperImpl();
        ProductResponse productResponse = wrapper.fetchProductByCode("3017620422003");

        if (!productResponse.isStatus()) {
            System.out.println("Status: " + productResponse.getStatusVerbose());
            return;
        }

        Product product = productResponse.getProduct();

        System.out.println("Name: " + product.getProductName());
        System.out.println("Generic name: " + product.getGenericName());
        System.out.println("Product code: " + product.getCode());
        System.out.println("");

        System.out.println("=== Nutriments ===");
        Nutriments nutriments = product.getNutriments();
        if (nutriments != null) {
            System.out.println(String.format(" * Energy = %s%s 100g", nutriments.getEnergy100G(), nutriments.getEnergyUnit()));
            System.out.println(String.format(" * Calcium = %s%s 100g", nutriments.getCalcium100G(), nutriments.getCalciumUnit()));
            System.out.println(String.format(" * Sugars = %s%s 100g", nutriments.getSugars100G(), nutriments.getSugarsUnit()));
        }

        System.out.println("=== Images ===");
        SelectedImages selectedImages = product.getSelectedImages();
        if (selectedImages != null) {
            System.out.println(" * Front: " + selectedImages.getFront().getDisplay().getUrl());

        }

    }

}
