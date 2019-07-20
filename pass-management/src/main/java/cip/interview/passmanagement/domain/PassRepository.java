package cip.interview.passmanagement.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PassRepository extends CrudRepository<Pass, Long> {

    boolean existsByVendorIdAndPassId(String vendorId, String passId);

    Optional<Pass> findByVendorIdAndPassId(String vendorId, String passId);

    void deleteByVendorIdAndPassId(String vendorId, String passId);
}
