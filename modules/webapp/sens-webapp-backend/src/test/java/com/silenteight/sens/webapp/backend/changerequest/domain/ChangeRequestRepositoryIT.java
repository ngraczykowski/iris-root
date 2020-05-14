package com.silenteight.sens.webapp.backend.changerequest.domain;

import com.silenteight.sens.webapp.common.testing.BaseDataJpaTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

import static java.math.BigInteger.valueOf;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.*;

@TestPropertySource("classpath:data-test.properties")
class ChangeRequestRepositoryIT extends BaseDataJpaTest {

  @Autowired
  private ChangeRequestRepository repository;

  @Test
  void changeRequestSavedToDatabase() {
    // given
    String makerUsername = "maker";
    String makerComment = "This is comment from Maker";

    // when
    repository.save(new ChangeRequest(randomUUID(), makerUsername, makerComment));

    // then
    Object count = entityManager
        .getEntityManager()
        .createNativeQuery("SELECT count(change_request_id) from webapp_change_request")
        .getSingleResult();
    assertThat(count).isEqualTo(valueOf(1));
  }
}
