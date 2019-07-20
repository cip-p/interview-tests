package cip.interview.passmanagement.domain;

import cip.interview.passmanagement.PassTestHelper;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class PassRepositoryTest {

    @Autowired
    private PassRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    private Pass validPass = PassTestHelper.validPass();

    @Test
    public void shouldCreatePass() {

        Pass savedPass = repository.save(validPass);

        Pass existingPass = entityManager.find(Pass.class, savedPass.getId());
        assertThat(existingPass).isNotNull();
    }

    @Test
    public void shouldExistPassByVendorIdAndPassId() {
        entityManager.persistAndFlush(validPass);

        boolean exists = repository.existsByVendorIdAndPassId(validPass.getVendorId(), validPass.getPassId());

        assertThat(exists).isTrue();
    }

    @Test
    public void shouldNotExistPassByVendorIdAndPassId() {
        entityManager.persistAndFlush(validPass);

        boolean exists = repository.existsByVendorIdAndPassId("unknown vendor id", "unknown pass id");

        assertThat(exists).isFalse();
    }

    @Test
    public void shouldDeletePassByVendorIdAndPassId() {
        Pass newPass = entityManager.persistAndFlush(validPass);

        repository.deleteByVendorIdAndPassId(validPass.getVendorId(), validPass.getPassId());

        Pass pass = entityManager.find(Pass.class, newPass.getId());
        assertThat(pass).isNull();
    }

    @Test
    public void shouldNotDeletePassByVendorIdAndPassId() {
        Pass newPass = entityManager.persistAndFlush(validPass);

        repository.deleteByVendorIdAndPassId("unknown vendor id", "unknown pass id");

        Pass pass = entityManager.find(Pass.class, newPass.getId());
        assertThat(pass).isNotNull();
    }

    @Test
    public void shouldFindPassByVendorIdAndPassId() {
        Pass pass = entityManager.persistAndFlush(validPass);

        Optional<Pass> potentialPass = repository.findByVendorIdAndPassId(validPass.getVendorId(), validPass.getPassId());

        Assertions.assertThat(potentialPass).isEqualTo(Optional.of(pass));
    }

    @Test
    public void shouldNotFindPassByVendorIdAndPassId() {
        entityManager.persistAndFlush(validPass);

        Optional<Pass> potentialPass = repository.findByVendorIdAndPassId("unknown vendor id", "unknown pass id");

        Assertions.assertThat(potentialPass).isEqualTo(Optional.empty());
    }
}