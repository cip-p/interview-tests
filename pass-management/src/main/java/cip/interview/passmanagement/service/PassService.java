package cip.interview.passmanagement.service;

import cip.interview.passmanagement.exceptions.InvalidPassException;
import cip.interview.passmanagement.exceptions.PassAlreadyExistsException;
import cip.interview.passmanagement.exceptions.PassNotFoundException;
import cip.interview.passmanagement.domain.Pass;
import cip.interview.passmanagement.domain.PassRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.time.Instant.now;

@Service
public class PassService {

    private final PassRepository repository;

    public PassService(PassRepository repository) {
        this.repository = repository;
    }

    public void create(Pass pass) {
        validate(pass);

        if (repository.existsByVendorIdAndPassId(pass.getVendorId(), pass.getPassId())) {
            throw new PassAlreadyExistsException();
        }

        repository.save(pass);
    }

    @Transactional
    public void cancel(String vendorId, String passId) {
        if (repository.existsByVendorIdAndPassId(vendorId, passId)) {
            repository.deleteByVendorIdAndPassId(vendorId, passId);
        } else {
            throw new PassNotFoundException();
        }
    }

    public void renew(String vendorId, String passId, Long passLength) {
        validateNotNullField(passLength, "passLength");

        repository.findByVendorIdAndPassId(vendorId, passId)
                .map(pass -> {
                    pass.setPassLength(passLength);
                    repository.save(pass);
                    return pass;
                })
                .orElseThrow(PassNotFoundException::new);
    }

    public boolean isActive(String vendorId, String passId) {
        return repository.findByVendorIdAndPassId(vendorId, passId)
                .map(pass -> {
                    Long expiryDate = pass.getPassLength();

                    return expiryDate != null && now().getEpochSecond() < expiryDate;
                })
                .orElseThrow(PassNotFoundException::new);
    }

    private void validate(Pass pass) {
        if (pass == null) {
            throw new InvalidPassException("pass is null");
        }
        validateNotNullField(pass.getVendorId(), "vendorId");
        validateNotNullField(pass.getPassId(), "passId");
        validateNotNullField(pass.getCustomerName(), "customerName");
        validateNotNullField(pass.getHomeCity(), "homeCity");
        validateNotNullField(pass.getPassCity(), "passCity");
        validateNotNullField(pass.getPassLength(), "passLength");
    }

    private void validateNotNullField(Object field, String fieldName) {
        if (field == null) {
            throw new InvalidPassException(String.format("%s is required", fieldName));
        }
    }

}
