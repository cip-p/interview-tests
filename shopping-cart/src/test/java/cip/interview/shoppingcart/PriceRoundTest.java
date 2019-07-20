package cip.interview.shoppingcart;

import org.junit.Test;

import static cip.interview.shoppingcart.PriceRound.roundTo2Decimals;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class PriceRoundTest {

    @Test
    public void shouldRoundUpTo2Decimals() {
        assertThat(roundTo2Decimals(0.565), is(0.57));
        assertThat(roundTo2Decimals(0.566), is(0.57));
    }

    @Test
    public void shouldRoundDown2Decimals() {
        assertThat(roundTo2Decimals(0.5649), is(0.56));
    }

}