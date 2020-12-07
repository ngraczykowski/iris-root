package com.silenteight.sens.webapp.backend.changerequest.domain;

import com.silenteight.sep.base.testing.BaseDataJpaTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static com.silenteight.sens.webapp.backend.changerequest.domain.ChangeRequestState.APPROVED;
import static com.silenteight.sens.webapp.backend.changerequest.domain.ChangeRequestState.CANCELLED;
import static com.silenteight.sens.webapp.backend.changerequest.domain.ChangeRequestState.PENDING;
import static com.silenteight.sens.webapp.backend.changerequest.domain.ChangeRequestState.REJECTED;
import static java.math.BigInteger.valueOf;
import static java.time.OffsetDateTime.now;
import static java.util.UUID.fromString;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.data.domain.Sort.Direction.DESC;

@TestPropertySource("classpath:data-test.properties")
class ChangeRequestRepositoryIT extends BaseDataJpaTest {

  private static final String BUSINESS_OPERATOR_USERNAME = "business_operator";
  private static final String BUSINESS_OPERATOR_COMMENT = "This is comment from Business Operator";

  @Autowired
  private ChangeRequestRepository repository;

  @Test
  void changeRequestSavedToDatabase() {
    // given
    UUID bulkChangeId = fromString("de1afe98-0b58-4941-9791-4e081f9b8139");
    ChangeRequest changeRequest = new ChangeRequest(bulkChangeId, BUSINESS_OPERATOR_USERNAME,
        BUSINESS_OPERATOR_COMMENT,
        now());

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
  void givenChangeRequestsInDifferentStates_listTwoLastClosed() {
    // given
    OffsetDateTime now = now();
    ChangeRequest pendingChangeRequest = makeChangeRequestWithState(now, PENDING);
    ChangeRequest approvedChangeRequest = makeChangeRequestWithState(now.minusSeconds(1), APPROVED);
    ChangeRequest rejectedChangeRequest = makeChangeRequestWithState(now, REJECTED);
    ChangeRequest cancelledChangeRequest =
        makeChangeRequestWithState(now.minusSeconds(2), CANCELLED);
    repository.save(pendingChangeRequest);
    repository.save(approvedChangeRequest);
    repository.save(rejectedChangeRequest);
    repository.save(cancelledChangeRequest);

    // when
    List<ChangeRequest> result = repository.findAllByStateIn(
        Set.of(APPROVED, REJECTED), PageRequest.of(0, 2, DESC, "createdAt"));

    // then
    assertThat(result.size()).isEqualTo(2);
    assertThat(result).containsExactly(rejectedChangeRequest, approvedChangeRequest);
  }

  @Test
  void givenChangeRequests_notFindByDifferentChangeRequestId() {
    // given
    ChangeRequest changeRequest = makePendingChangeRequest(
        fromString("de1afe98-0b58-4941-9791-4e081f9b8139"));
    ChangeRequest savedChangeRequest = repository.save(changeRequest);
    long differentChangeRequestId = savedChangeRequest.getId() + 1L;

    // when, then
    assertThat(repository.findById(differentChangeRequestId)).isEmpty();
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
    assertThat(result).isNotEmpty().get().isEqualTo(savedChangeRequest);
  }

  @Test
  void saveApprovedChangeRequest() {
    // given
    ChangeRequest changeRequest = makePendingChangeRequest(
        fromString("de1afe98-0b58-4941-9791-4e081f9b8140"));
    ChangeRequest savedChangeRequest = repository.save(changeRequest);
    String approverComment = "approver comment";
    String approverUsername = "approver1";
    changeRequest.approve(approverUsername, approverComment, now());
    repository.save(savedChangeRequest);

    // when
    Optional<ChangeRequest> maybeResult = repository.findById(savedChangeRequest.getId());

    // then
    assertThat(maybeResult).isNotEmpty().get().satisfies(result -> {
      assertThat(result.getDeciderComment()).isEqualTo(approverComment);
      assertThat(result.getDecidedBy()).isEqualTo(approverUsername);
    });
  }

  private static ChangeRequest makePendingChangeRequest(UUID bulkChangeId) {
    return new ChangeRequest(bulkChangeId, BUSINESS_OPERATOR_USERNAME, BUSINESS_OPERATOR_COMMENT, now());
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
        new ChangeRequest(bulkChangeId, BUSINESS_OPERATOR_USERNAME, BUSINESS_OPERATOR_COMMENT, now());
    changeRequest.setState(state);

    return changeRequest;
  }

  private static ChangeRequest makeChangeRequestWithState(
      OffsetDateTime createdAt, ChangeRequestState state) {
    ChangeRequest changeRequest =
        new ChangeRequest(randomUUID(), BUSINESS_OPERATOR_USERNAME, BUSINESS_OPERATOR_COMMENT, createdAt);
    changeRequest.setState(state);

    return changeRequest;
  }
}
