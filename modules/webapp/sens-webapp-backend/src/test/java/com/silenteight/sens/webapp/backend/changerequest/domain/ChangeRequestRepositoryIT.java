package com.silenteight.sens.webapp.backend.changerequest.domain;

import com.silenteight.sens.webapp.common.testing.BaseDataJpaTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.silenteight.sens.webapp.backend.changerequest.domain.ChangeRequestState.APPROVED;
import static com.silenteight.sens.webapp.backend.changerequest.domain.ChangeRequestState.PENDING;
import static com.silenteight.sens.webapp.backend.changerequest.domain.ChangeRequestState.REJECTED;
import static java.math.BigInteger.valueOf;
import static java.util.UUID.fromString;
import static org.assertj.core.api.Assertions.*;

@TestPropertySource("classpath:data-test.properties")
class ChangeRequestRepositoryIT extends BaseDataJpaTest {

  private static final String MAKER_USERNAME = "maker";
  private static final String MAKER_COMMENT = "This is comment from Maker";

  @Autowired
  private ChangeRequestRepository repository;

  @Test
  void changeRequestSavedToDatabase() {
    // given
    UUID bulkChangeId = fromString("de1afe98-0b58-4941-9791-4e081f9b8139");
    ChangeRequest changeRequest = new ChangeRequest(bulkChangeId, MAKER_USERNAME, MAKER_COMMENT,
        OffsetDateTime.now());

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
  void givenChangeRequestsInDifferentStates_listPending() {
    // given
    ChangeRequest pendingChangeRequest =
        makePendingChangeRequest(fromString("de1afe98-0b58-4941-9791-4e081f9b8139"));
    ChangeRequest approvedChangeRequest =
        makeApprovedChangeRequest(fromString("30131be0-7405-41f1-b79e-fe109a5d2a41"));
    ChangeRequest rejectedChangeRequest =
        makeRejectedChangeRequest(fromString("ecbadfff-164b-4751-b131-7177438e9903"));
    repository.save(pendingChangeRequest);
    repository.save(approvedChangeRequest);
    repository.save(rejectedChangeRequest);

    // when
    List<ChangeRequest> result = repository.findAllByState(PENDING);

    // then
    assertThat(result.size()).isEqualTo(1);
    assertThat(result).containsExactly(pendingChangeRequest);
  }

  @Test
  void givenChangeRequests_notFindByDifferentChangeRequestId() {
    // given
    ChangeRequest changeRequest = makePendingChangeRequest(
        fromString("de1afe98-0b58-4941-9791-4e081f9b8139"));
    ChangeRequest savedChangeRequest = repository.save(changeRequest);
    long differentChangeRequestId = savedChangeRequest.getId() + 1L;

    // when
    Optional<ChangeRequest> result = repository.findById(differentChangeRequestId);

    // then
    assertThat(result).isEmpty();
  }

  @Test
  void givenChangeRequest_findByTheSameChangeRequestId() {
    // given
    ChangeRequest changeRequest = makePendingChangeRequest(
        fromString("de1afe98-0b58-4941-9791-4e081f9b8139"));
    ChangeRequest savedChangeRequest = repository.save(changeRequest);

    // when
    Optional<ChangeRequest> result = repository.findById(savedChangeRequest.getId());

    // then
    assertThat(result).isNotEmpty();
    assertThat(result.get()).isEqualTo(savedChangeRequest);
  }

  private static ChangeRequest makePendingChangeRequest(UUID bulkChangeId) {
    return new ChangeRequest(bulkChangeId, MAKER_USERNAME, MAKER_COMMENT, OffsetDateTime.now());
  }

  private static ChangeRequest makeApprovedChangeRequest(UUID bulkChangeId) {
    return makeChangeRequestWithState(bulkChangeId, APPROVED);
  }

  private static ChangeRequest makeRejectedChangeRequest(UUID bulkChangeId) {
    return makeChangeRequestWithState(bulkChangeId, REJECTED);
  }

  private static ChangeRequest makeChangeRequestWithState(
      UUID bulkChangeId, ChangeRequestState state) {
    ChangeRequest changeRequest =
        new ChangeRequest(bulkChangeId, MAKER_USERNAME, MAKER_COMMENT, OffsetDateTime.now());
    changeRequest.setState(state);

    return changeRequest;
  }
}
