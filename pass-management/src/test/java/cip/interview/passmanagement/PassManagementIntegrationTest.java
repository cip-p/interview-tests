package cip.interview.passmanagement;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
public class PassManagementIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldCreateValidPass() {

        ResponseEntity<String> response = restTemplate.postForEntity("/vendors/{vendorId}/passes/{passId}", PassTestHelper.validPassRequest(), String.class, PassTestHelper.VENDOR_ID, PassTestHelper.PASS_ID);

        assertThat(response.getStatusCode()).isEqualTo(CREATED);
        assertThat(response.getBody()).isNull();
    }

    @Test
    public void shouldNotCreatePassWhenExists() {

        ResponseEntity<String> response = restTemplate.postForEntity("/vendors/{vendorId}/passes/{passId}", PassTestHelper.validPassRequest(), String.class, PassTestHelper.DB_EXISTING_VENDOR_ID, PassTestHelper.DB_EXISTING_PASS_ID);

        assertThat(response.getStatusCode()).isEqualTo(CONFLICT);
        assertThat(response.getBody()).isNull();
    }

    @Test
    public void shouldCancelExistingPass() {

        ResponseEntity<String> response = restTemplate.exchange("/vendors/{vendorId}/passes/{passId}", DELETE, null, String.class, PassTestHelper.DB_EXISTING_VENDOR_ID, PassTestHelper.DB_EXISTING_PASS_ID);

        assertThat(response.getStatusCode()).isEqualTo(NO_CONTENT);
        assertThat(response.getBody()).isNull();
    }

    @Test
    public void shouldNotCancelPassWhenItDoesntExist() {

        ResponseEntity<String> response = restTemplate.exchange("/vendors/{vendorId}/passes/{passId}", DELETE, null, String.class, PassTestHelper.VENDOR_ID, PassTestHelper.PASS_ID);

        assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }

    @Test
    public void shouldRenewExistingPass() {

        ResponseEntity<String> response = restTemplate.exchange("/vendors/{vendorId}/passes/{passId}/renew", PUT, new HttpEntity<>(7864678800L), String.class, PassTestHelper.DB_EXISTING_VENDOR_ID, PassTestHelper.DB_EXISTING_PASS_ID);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNull();
    }

    @Test
    public void shouldNotRenewPassWhenItDoesntExist() {

        ResponseEntity<String> response = restTemplate.exchange("/vendors/{vendorId}/passes/{passId}/renew", PUT, new HttpEntity<>(7864678800L), String.class, PassTestHelper.VENDOR_ID, PassTestHelper.PASS_ID);

        assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }

    @Test
    public void shouldCheckIfThePassIsActiveReturningTrue() {

        ResponseEntity<String> response = restTemplate.getForEntity("/vendors/{vendorId}/passes/{passId}/is-active", String.class, PassTestHelper.DB_EXISTING_VENDOR_ID, PassTestHelper.DB_EXISTING_PASS_ID);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isEqualTo("true");
    }

    @Test
    public void shouldCheckIfThePassIsActiveReturningFalse() {

        ResponseEntity<String> response = restTemplate.getForEntity("/vendors/{vendorId}/passes/{passId}/is-active", String.class, PassTestHelper.DB_INACTIVE_VENDOR_ID, PassTestHelper.DB_INACTIVE_PASS_ID);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isEqualTo("false");
    }

    @Test
    public void shouldNotCheckIfThePassIsActiveWhenItDoesntExist() {

        ResponseEntity<String> response = restTemplate.getForEntity("/vendors/{vendorId}/passes/{passId}/is-active", String.class, PassTestHelper.VENDOR_ID, PassTestHelper.PASS_ID);

        assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }

}
