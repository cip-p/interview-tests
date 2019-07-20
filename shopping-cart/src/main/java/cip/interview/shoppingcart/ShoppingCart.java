package cip.interview.shoppingcart;

import java.util.ArrayList;
import java.util.List;

import static cip.interview.shoppingcart.PriceRound.roundTo2Decimals;

public class ShoppingCart {

    private static final double SALES_TAX_RATE_PERCENTAGE = 12.5;

    private List<Product> products = new ArrayList<>();

    void add(int quantity, Product product) {
        for (int i = 0; i < quantity; i++) {
            products.add(product);
        }
    }

    double totalPrice() {
        return salesTax() + roundTo2Decimals(totalPriceBeforeTax());
    }

    double salesTax() {
        return roundTo2Decimals(totalPriceBeforeTax() * SALES_TAX_RATE_PERCENTAGE / 100);
    }

    private double totalPriceBeforeTax() {
        return products.stream()
                .mapToDouble(Product::getPrice)
                .sum();
    }

    List<Product> getProducts() {
        return products;
    }
}
