package com.silenteight.serp.governance.changerequest.domain;

import com.silenteight.sep.base.testing.BaseDataJpaTest;
import com.silenteight.serp.governance.changerequest.approval.dto.ModelApprovalDto;
import com.silenteight.serp.governance.changerequest.domain.dto.ChangeRequestDto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Collection;

import static com.silenteight.serp.governance.changerequest.domain.ChangeRequestState.APPROVED;
import static com.silenteight.serp.governance.changerequest.domain.ChangeRequestState.PENDING;
import static com.silenteight.serp.governance.changerequest.domain.ChangeRequestState.REJECTED;
import static com.silenteight.serp.governance.changerequest.fixture.ChangeRequestFixtures.CHANGE_REQUEST_ID;
import static com.silenteight.serp.governance.changerequest.fixture.ChangeRequestFixtures.CREATED_BY;
import static com.silenteight.serp.governance.changerequest.fixture.ChangeRequestFixtures.CREATOR_COMMENT;
import static com.silenteight.serp.governance.changerequest.fixture.ChangeRequestFixtures.MODEL_NAME;
import static java.time.OffsetDateTime.now;
import static java.util.Set.of;
import static org.assertj.core.api.Assertions.*;

@Transactional
@ContextConfiguration(classes = { ChangeRequestTestConfiguration.class })
@TestPropertySource("classpath:data-test.properties")
@AutoConfigureJsonTesters
class ChangeRequestQueryTest extends BaseDataJpaTest {

  @Autowired
  ChangeRequestQuery underTest;

  @Autowired
  ChangeRequestRepository repository;

  @Test
  void shouldListPendingChangeRequests() {
    // given
    persistChangeRequest();

    // when
    Collection<ChangeRequestDto> result = underTest.list(of(PENDING));

    // then
    assertThat(result.size()).isEqualTo(1);
    ChangeRequestDto pendingChangeRequest = result.iterator().next();
    assertThat(pendingChangeRequest.getId()).isEqualTo(CHANGE_REQUEST_ID);
    assertThat(pendingChangeRequest.getCreatedAt()).isNotNull();
    assertThat(pendingChangeRequest.getCreatedBy()).isEqualTo(CREATED_BY);
    assertThat(pendingChangeRequest.getCreatorComment()).isEqualTo(CREATOR_COMMENT);
    assertThat(pendingChangeRequest.getModelName()).isEqualTo(MODEL_NAME);
  }

  @Test
  void shouldListClosedChangeRequests() {
    // given
    String decider = "jdoe";
    String deciderComment = "Not correct";
    ChangeRequest changeRequest = persistChangeRequest();
    changeRequest.reject(decider, deciderComment);

    // when
    Collection<ChangeRequestDto> result = underTest.list(of(APPROVED, REJECTED));

    // then
    assertThat(result.size()).isEqualTo(1);
    ChangeRequestDto closedChangeRequest = result.iterator().next();
    assertThat(closedChangeRequest.getId()).isEqualTo(CHANGE_REQUEST_ID);
    assertThat(closedChangeRequest.getCreatedAt()).isNotNull();
    assertThat(closedChangeRequest.getCreatedBy()).isEqualTo(CREATED_BY);
    assertThat(closedChangeRequest.getCreatorComment()).isEqualTo(CREATOR_COMMENT);
    assertThat(closedChangeRequest.getDecidedBy()).isEqualTo(decider);
    assertThat(closedChangeRequest.getDecidedAt()).isNotNull();
    assertThat(closedChangeRequest.getDeciderComment()).isEqualTo(deciderComment);
    assertThat(closedChangeRequest.getState()).isEqualTo(REJECTED);
  }

  @Test
  void shouldGetChangeRequestDetails() {
    // given
    String decider = "jdoe";
    String deciderComment = "Looks good";
    ChangeRequest changeRequest = persistChangeRequest();
    changeRequest.approve(decider, deciderComment);

    // when
    ChangeRequestDto result = underTest.details(CHANGE_REQUEST_ID);

    // then
    assertThat(result.getId()).isEqualTo(CHANGE_REQUEST_ID);
    assertThat(result.getCreatedAt()).isNotNull();
    assertThat(result.getCreatedBy()).isEqualTo(CREATED_BY);
    assertThat(result.getCreatorComment()).isEqualTo(CREATOR_COMMENT);
    assertThat(result.getModelName()).isEqualTo(MODEL_NAME);
    assertThat(result.getDecidedBy()).isEqualTo(decider);
    assertThat(result.getDecidedAt()).isNotNull();
    assertThat(result.getDeciderComment()).isEqualTo(deciderComment);
    assertThat(result.getState()).isEqualTo(APPROVED);
  }

  @Test
  void shouldGetModelApproval() {
    // given
    String decider = "jdoe";
    String deciderComment = "Looks good";
    ChangeRequest changeRequest = persistChangeRequest();
    OffsetDateTime timeBeforeApproval = now();
    changeRequest.approve(decider, deciderComment);

    // when
    ModelApprovalDto result = underTest.getApproval(MODEL_NAME);

    // then
    assertThat(result.getApprovedAt()).isAfter(timeBeforeApproval);
    assertThat(result.getApprovedBy()).isEqualTo(decider);
  }

  private ChangeRequest persistChangeRequest() {
    return repository.save(
        new ChangeRequest(CHANGE_REQUEST_ID, MODEL_NAME, CREATED_BY, CREATOR_COMMENT));
  }
}
