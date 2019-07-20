package cip.interview.passmanagement.service;

import cip.interview.passmanagement.PassTestHelper;
import cip.interview.passmanagement.exceptions.PassAlreadyExistsException;
import cip.interview.passmanagement.exceptions.PassNotFoundException;
import cip.interview.passmanagement.domain.Pass;
import cip.interview.passmanagement.domain.PassRepository;
import cip.interview.passmanagement.exceptions.InvalidPassException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static java.lang.String.format;
import static java.time.Instant.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PassServiceTest {

    @InjectMocks
    private PassService service;

    @Mock
    private PassRepository repository;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldCreatePassWhenValid() {
        Pass pass = PassTestHelper.validPass();

        service.create(PassTestHelper.validPass());

        verify(repository).save(pass);
    }

    @Test
    public void shouldNotCreatePassWhenItAlreadyExists() {
        Pass pass = PassTestHelper.validPass();
        when(repository.existsByVendorIdAndPassId(pass.getVendorId(), pass.getPassId())).thenReturn(true);
        expectedException.expect(PassAlreadyExistsException.class);

        service.create(PassTestHelper.validPass());
    }

    @Test
    public void shouldValidateAndFailCreateWhenPassIsNull() {
        expectedException.expect(InvalidPassException.class);
        expectedException.expectMessage("pass is null");

        service.create(null);
    }

    @Test
    public void shouldValidateAndFailCreateWhenPassIsInvalid() {

        Pass pass = PassTestHelper.validPass();
        pass.setVendorId(null);
        assertValidationError(pass, "vendorId");

        pass = PassTestHelper.validPass();
        pass.setPassId(null);
        assertValidationError(pass, "passId");

        pass = PassTestHelper.validPass();
        pass.setCustomerName(null);
        assertValidationError(pass, "customerName");

        pass = PassTestHelper.validPass();
        pass.setHomeCity(null);
        assertValidationError(pass, "homeCity");

        pass = PassTestHelper.validPass();
        pass.setPassCity(null);
        assertValidationError(pass, "passCity");

        pass = PassTestHelper.validPass();
        pass.setPassLength(null);
        assertValidationError(pass, "passLength");
    }

    @Test
    public void shouldCancelPassWhenExists() {
        Pass pass = PassTestHelper.validPass();
        when(repository.existsByVendorIdAndPassId(pass.getVendorId(), pass.getPassId())).thenReturn(true);

        service.cancel(pass.getVendorId(), pass.getPassId());

        verify(repository).deleteByVendorIdAndPassId(pass.getVendorId(), pass.getPassId());
    }

    @Test
    public void shouldNotCancelPassWhenItDoesntExist() {
        Pass pass = PassTestHelper.validPass();
        when(repository.existsByVendorIdAndPassId(pass.getVendorId(), pass.getPassId())).thenReturn(false);
        expectedException.expect(PassNotFoundException.class);

        service.cancel(pass.getVendorId(), pass.getPassId());
    }

    @Test
    public void shouldRenewPassWhenExists() {
        Pass pass = PassTestHelper.validPass();
        pass.setId(1L);
        when(repository.findByVendorIdAndPassId(pass.getVendorId(), pass.getPassId())).thenReturn(Optional.of(pass));

        service.renew(pass.getVendorId(), pass.getPassId(), PassTestHelper.PASS_LENGTH);

        verify(repository).save(pass);
    }

    @Test
    public void shouldNotRenewPassWhenItDoesntExist() {
        Pass pass = PassTestHelper.validPass();
        when(repository.findByVendorIdAndPassId(pass.getVendorId(), pass.getPassId())).thenReturn(Optional.empty());
        expectedException.expect(PassNotFoundException.class);

        service.renew(pass.getVendorId(), pass.getPassId(), PassTestHelper.PASS_LENGTH);
    }

    @Test
    public void shouldNotRenewPassWhenPassLengthIsNull() {
        Pass pass = PassTestHelper.validPass();
        expectedException.expect(InvalidPassException.class);
        expectedException.expectMessage("passLength is required");

        service.renew(pass.getVendorId(), pass.getPassId(), null);
    }

    @Test
    public void shouldBeActiveIfPassLengthIsInFuture() {
        Pass pass = PassTestHelper.validPass();
        long lengthInPast = now().getEpochSecond() + 1_000;
        pass.setPassLength(lengthInPast);

        when(repository.findByVendorIdAndPassId(pass.getVendorId(), pass.getPassId())).thenReturn(Optional.of(pass));

        boolean active = service.isActive(pass.getVendorId(), pass.getPassId());

        assertThat(active).isTrue();
    }

    @Test
    public void shouldNotBeActiveIfPassLengthIsInPast() {
        Pass pass = PassTestHelper.validPass();
        long lengthInPast = now().getEpochSecond() - 1_000;
        pass.setPassLength(lengthInPast);

        when(repository.findByVendorIdAndPassId(pass.getVendorId(), pass.getPassId())).thenReturn(Optional.of(pass));

        boolean active = service.isActive(pass.getVendorId(), pass.getPassId());

        assertThat(active).isFalse();
    }

    @Test
    public void shouldNotCheckIsActivePassWhenItDoesntExist() {
        Pass pass = PassTestHelper.validPass();
        when(repository.findByVendorIdAndPassId(pass.getVendorId(), pass.getPassId())).thenReturn(Optional.empty());
        expectedException.expect(PassNotFoundException.class);

        service.isActive(pass.getVendorId(), pass.getPassId());
    }

    private void assertValidationError(Pass pass, String fieldName) {
        try {
            service.create(pass);

            fail(format("pass is invalid when %s is null", fieldName));
        } catch (InvalidPassException e) {
            assertThat(e.getMessage()).isEqualTo(format("%s is required", fieldName));
        }
    }
}
