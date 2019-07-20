package cip.interview.shoppingcart;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ProductTest {


    @Test
    public void shouldGetTheProductName() {
        Product product = new Product("product name", 1.22);

        assertThat(product.getName(), is("product name"));
    }

    @Test
    public void shouldGetTheProductPrice() {
        Product product = new Product("product name", 1.22);

        assertThat(product.getPrice(), is(1.22));
    }
}