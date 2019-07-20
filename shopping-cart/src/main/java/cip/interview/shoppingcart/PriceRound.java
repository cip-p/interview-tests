package cip.interview.shoppingcart;

import java.math.BigDecimal;
import java.math.RoundingMode;

class PriceRound {

    static double roundTo2Decimals(double price) {
        BigDecimal bd = new BigDecimal(Double.valueOf(price).toString())
                .setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}
