package com.silenteight.sens.webapp.backend.changerequest.domain;

import com.silenteight.sens.webapp.common.testing.BaseDataJpaTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;
import java.util.UUID;

import static java.math.BigInteger.valueOf;
import static java.util.UUID.fromString;
import static org.assertj.core.api.Assertions.*;

@TestPropertySource("classpath:data-test.properties")
class ChangeRequestRepositoryIT extends BaseDataJpaTest {

  private static final UUID BULK_CHANGE_ID = fromString("de1afe98-0b58-4941-9791-4e081f9b8139");
  private static final String MAKER_USERNAME = "maker";
  private static final String MAKER_COMMENT = "This is comment from Maker";

  @Autowired
  private ChangeRequestRepository repository;

  @Test
  void changeRequestSavedToDatabase() {
    // given
    ChangeRequest changeRequest = new ChangeRequest(BULK_CHANGE_ID, MAKER_USERNAME, MAKER_COMMENT);

    // when
    repository.save(changeRequest);

    // then
    Object count = entityManager
        .getEntityManager()
        .createNativeQuery("SELECT count(change_request_id) from webapp_change_request")
        .getSingleResult();
    assertThat(count).isEqualTo(valueOf(1));
  }

  @Test
  void givenChangeRequest_notFindByDifferentBulkChangeId() {
    // given
    ChangeRequest changeRequest = new ChangeRequest(BULK_CHANGE_ID, MAKER_USERNAME, MAKER_COMMENT);
    UUID differentBulkChangeId = fromString("30131be0-7405-41f1-b79e-fe109a5d2a41");

    // when
    repository.save(changeRequest);

    // then
    Optional<ChangeRequest> result = repository.findByBulkChangeId(differentBulkChangeId);
    assertThat(result).isEmpty();
  }

  @Test
  void givenChangeRequest_findByTheSameBulkChangeId() {
    // given
    ChangeRequest changeRequest = new ChangeRequest(BULK_CHANGE_ID, MAKER_USERNAME, MAKER_COMMENT);
    repository.save(changeRequest);

    // when
    Optional<ChangeRequest> result = repository.findByBulkChangeId(BULK_CHANGE_ID);

    // then
    assertThat(result).isNotEmpty();
    assertThat(result.get()).isEqualTo(changeRequest);
  }
}
