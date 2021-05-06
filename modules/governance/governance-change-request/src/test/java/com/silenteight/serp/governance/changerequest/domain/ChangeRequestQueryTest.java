package com.silenteight.serp.governance.changerequest.domain;

import com.silenteight.sep.base.testing.BaseDataJpaTest;
import com.silenteight.serp.governance.changerequest.closed.dto.ClosedChangeRequestDto;
import com.silenteight.serp.governance.changerequest.details.dto.ChangeRequestDetailsDto;
import com.silenteight.serp.governance.changerequest.pending.dto.PendingChangeRequestDto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.silenteight.serp.governance.changerequest.domain.ChangeRequestState.APPROVED;
import static com.silenteight.serp.governance.changerequest.domain.ChangeRequestState.CANCELLED;
import static com.silenteight.serp.governance.changerequest.fixture.ChangeRequestFixtures.CHANGE_REQUEST_ID;
import static com.silenteight.serp.governance.changerequest.fixture.ChangeRequestFixtures.CREATED_BY;
import static com.silenteight.serp.governance.changerequest.fixture.ChangeRequestFixtures.CREATOR_COMMENT;
import static com.silenteight.serp.governance.changerequest.fixture.ChangeRequestFixtures.MODEL_NAME;
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
    List<PendingChangeRequestDto> result = underTest.listPending();

    // then
    assertThat(result.size()).isEqualTo(1);
    PendingChangeRequestDto changeRequest = result.get(0);
    assertThat(changeRequest.getId()).isEqualTo(CHANGE_REQUEST_ID);
    assertThat(changeRequest.getCreatedAt()).isNotNull();
    assertThat(changeRequest.getCreatedBy()).isEqualTo(CREATED_BY);
    assertThat(changeRequest.getComment()).isEqualTo(CREATOR_COMMENT);
    assertThat(changeRequest.getModelName()).isEqualTo(MODEL_NAME);
  }

  @Test
  void shouldListClosedChangeRequests() {
    // given
    String decider = "jdoe";
    ChangeRequest changeRequest = persistChangeRequest();
    changeRequest.cancel(decider);

    // when
    List<ClosedChangeRequestDto> result = underTest.listClosed();

    // then
    assertThat(result.size()).isEqualTo(1);
    ClosedChangeRequestDto closedChangeRequest = result.get(0);
    assertThat(closedChangeRequest.getId()).isEqualTo(CHANGE_REQUEST_ID);
    assertThat(closedChangeRequest.getCreatedAt()).isNotNull();
    assertThat(closedChangeRequest.getCreatedBy()).isEqualTo(CREATED_BY);
    assertThat(closedChangeRequest.getCreatorComment()).isEqualTo(CREATOR_COMMENT);
    assertThat(closedChangeRequest.getDecidedBy()).isEqualTo(decider);
    assertThat(closedChangeRequest.getDecidedAt()).isNotNull();
    assertThat(closedChangeRequest.getDeciderComment()).isNull();
    assertThat(closedChangeRequest.getState()).isEqualTo(CANCELLED.name());
  }

  @Test
  void shouldGetChangeRequestDetails() {
    // given
    String decider = "jdoe";
    String deciderComment = "Looks good";
    ChangeRequest changeRequest = persistChangeRequest();
    changeRequest.approve(decider, deciderComment);

    // when
    ChangeRequestDetailsDto result = underTest.details(CHANGE_REQUEST_ID);

    // then
    assertThat(result.getId()).isEqualTo(CHANGE_REQUEST_ID);
    assertThat(result.getCreatedAt()).isNotNull();
    assertThat(result.getCreatedBy()).isEqualTo(CREATED_BY);
    assertThat(result.getCreatorComment()).isEqualTo(CREATOR_COMMENT);
    assertThat(result.getModelName()).isEqualTo(MODEL_NAME);
    assertThat(result.getDecidedBy()).isEqualTo(decider);
    assertThat(result.getDecidedAt()).isNotNull();
    assertThat(result.getDeciderComment()).isEqualTo(deciderComment);
    assertThat(result.getState()).isEqualTo(APPROVED.name());
  }

  private ChangeRequest persistChangeRequest() {
    return repository.save(
        new ChangeRequest(CHANGE_REQUEST_ID, MODEL_NAME, CREATED_BY, CREATOR_COMMENT));
  }
}
