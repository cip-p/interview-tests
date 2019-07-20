package cip.interview.shoppingcart;

import org.junit.Test;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ShoppingCartTest {

    private ShoppingCart shoppingCart = new ShoppingCart();

    private Product DOVE_SOAP_PRODUCT = new Product("Dove Soap", 39.99);
    private Product AXE_DEO_PRODUCT = new Product("Axe Deo", 99.99);

    @Test
    public void emptyShoppingCartShouldHaveNoProducts() {
        List<Product> products = shoppingCart.getProducts();

        assertThat(products.size(), is(0));
    }

    @Test
    public void shouldAddMoreThanOneProductToTheShoppingCart() {
        shoppingCart.add(5, DOVE_SOAP_PRODUCT);

        List<Product> products = shoppingCart.getProducts();

        assertThat(products.size(), is(5));
    }

    @Test
    public void shouldAddMultipleTimesProductsToTheShoppingCart() {
        shoppingCart.add(2, DOVE_SOAP_PRODUCT);
        shoppingCart.add(2, AXE_DEO_PRODUCT);

        List<Product> products = shoppingCart.getProducts();

        assertThat(products.size(), is(4));

        assertThatTheNumberOfProductsIs(DOVE_SOAP_PRODUCT.getName(), 2);
        assertThatTheNumberOfProductsIs(AXE_DEO_PRODUCT.getName(), 2);
    }

    @Test
    public void shoppingCartShouldContainProductsWithTheirUnitPrice() {
        shoppingCart.add(2, DOVE_SOAP_PRODUCT);
        shoppingCart.add(2, AXE_DEO_PRODUCT);

        assertThatProductsHavePrice(DOVE_SOAP_PRODUCT.getName(), DOVE_SOAP_PRODUCT.getPrice());
        assertThatProductsHavePrice(AXE_DEO_PRODUCT.getName(), AXE_DEO_PRODUCT.getPrice());
    }

    @Test
    public void shoppingCartShouldContainProductsWithTheirName() {
        shoppingCart.add(2, DOVE_SOAP_PRODUCT);
        shoppingCart.add(2, AXE_DEO_PRODUCT);

        assertThatProductsHaveName(DOVE_SOAP_PRODUCT.getName(), DOVE_SOAP_PRODUCT.getName());
        assertThatProductsHaveName(AXE_DEO_PRODUCT.getName(), AXE_DEO_PRODUCT.getName());
    }

    @Test
    public void shouldCalculateTotalPrice() {
        shoppingCart.add(2, DOVE_SOAP_PRODUCT);
        shoppingCart.add(2, AXE_DEO_PRODUCT);

        double totalPrice = shoppingCart.totalPrice();

        assertThat(totalPrice, is(314.96));
    }

    @Test
    public void shouldCalculateSalesTaxAmount() {
        shoppingCart.add(2, DOVE_SOAP_PRODUCT);
        shoppingCart.add(2, AXE_DEO_PRODUCT);

        double salesTax = shoppingCart.salesTax();

        assertThat(salesTax, is(35.00));
    }

    private void assertThatTheNumberOfProductsIs(String productName, long quantity) {
        long numberOfProducts = shoppingCart.getProducts().stream()
                .filter(product -> productName.equals(product.getName()))
                .count();

        assertThat("found different number of products for " + productName, numberOfProducts, is(quantity));
    }

    private void assertThatProductsHavePrice(String productName, double price) {
        productsWithName(productName)
                .forEach(product -> assertThat("invalid price for product " + productName, product.getPrice(), is(price)));
    }

    private void assertThatProductsHaveName(String productName, String name) {
        productsWithName(productName)
                .forEach(product -> assertThat("invalid name for product " + productName, product.getName(), is(name)));
    }

    private List<Product> productsWithName(String productName) {
        return shoppingCart.getProducts().stream()
                .filter(product -> productName.equals(product.getName()))
                .collect(toList());
    }
}
