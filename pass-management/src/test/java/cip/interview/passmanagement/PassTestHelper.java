package cip.interview.passmanagement;

import cip.interview.passmanagement.web.PassRequest;
import cip.interview.passmanagement.domain.Pass;

public class PassTestHelper {

    static final String DB_EXISTING_VENDOR_ID = "john-vendor-id";
    static final String DB_EXISTING_PASS_ID = "john-pass-id";

    static final String DB_INACTIVE_VENDOR_ID = "craig-vendor-id";
    static final String DB_INACTIVE_PASS_ID = "craig-pass-id";

    public static final String VENDOR_ID = "vendor-id";
    public static final String PASS_ID = "pass-id";

    private static final String CUSTOMER_NAME = "customer name";
    private static final String HOME_CITY = "home city";
    private static final String PASS_CITY = "pass city";
    public static final Long PASS_LENGTH = 1553267640L;

    public static Pass validPass() {
        Pass pass = new Pass();
        pass.setCustomerName(CUSTOMER_NAME);
        pass.setHomeCity(HOME_CITY);
        pass.setPassCity(PASS_CITY);
        pass.setPassLength(PASS_LENGTH);
        pass.setPassId(VENDOR_ID);
        pass.setVendorId(PASS_ID);
        return pass;
    }

    public static PassRequest validPassRequest() {
        PassRequest pass = new PassRequest();
        pass.setCustomerName(CUSTOMER_NAME);
        pass.setHomeCity(HOME_CITY);
        pass.setPassCity(PASS_CITY);
        pass.setPassLength(PASS_LENGTH);
        return pass;
    }
}
