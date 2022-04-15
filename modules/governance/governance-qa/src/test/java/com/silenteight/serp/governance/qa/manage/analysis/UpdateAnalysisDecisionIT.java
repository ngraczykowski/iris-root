package com.silenteight.serp.governance.qa.manage.analysis;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.data.api.v2.QaAlert;
import com.silenteight.data.api.v2.QaAlert.State;
import com.silenteight.data.api.v2.QaDataIndexRequest;
import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest.TestRestConfiguration;
import com.silenteight.sep.base.testing.containers.PostgresContainer.PostgresTestInitializer;
import com.silenteight.sep.base.testing.containers.RabbitContainer.RabbitTestInitializer;
import com.silenteight.serp.governance.qa.QaIntegrationProperties;
import com.silenteight.serp.governance.qa.manage.analysis.create.CreateAlertWithDecisionUseCase;
import com.silenteight.serp.governance.qa.manage.analysis.update.dto.UpdateAnalysisDecisionDto;
import com.silenteight.serp.governance.qa.manage.domain.DecisionLevel;
import com.silenteight.serp.governance.qa.manage.domain.dto.CreateDecisionRequest;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.time.OffsetDateTime;
import javax.validation.Valid;

import static com.silenteight.sens.governance.common.testing.rest.TestRoles.QA;
import static com.silenteight.sens.governance.common.testing.rest.TestRoles.QA_ISSUE_MANAGER;
import static com.silenteight.serp.governance.qa.AlertFixture.ALERT_ID;
import static com.silenteight.serp.governance.qa.AlertFixture.ALERT_NAME;
import static com.silenteight.serp.governance.qa.DecisionFixture.COMMENT_FAILED;
import static com.silenteight.serp.governance.qa.DecisionFixture.LEVEL_ANALYSIS;
import static com.silenteight.serp.governance.qa.manage.domain.DecisionState.FAILED;
import static com.silenteight.serp.governance.qa.manage.domain.DecisionState.NEW;
import static io.restassured.http.ContentType.JSON;
import static java.lang.String.format;
import static java.time.OffsetDateTime.parse;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@SpringBootTest(classes = {
    UpdateAnalysisDecisionITConfiguration.class,
    TestRestConfiguration.class})
@ContextConfiguration(initializers = {
    RabbitTestInitializer.class,
    PostgresTestInitializer.class,
})
@AutoConfigureDataJpa
@TestPropertySource(properties = { "spring.config.additional-location=classpath:application.yml"})
@TestMethodOrder(OrderAnnotation.class)
class UpdateAnalysisDecisionIT extends BaseRestControllerTest {

  private static final String UPDATE_ANALYSIS_DECISION_URL = format("/v1/qa/0/alerts/%s", ALERT_ID);
  private static final String ALERTS_ANALYSIS_DETAILS_URL = format("/v1/qa/0/alerts/%s", ALERT_ID);
  private static final String ALERTS_VALIDATION_DETAILS_URL
      = format("/v1/qa/1/alerts/%s", ALERT_ID);
  private static final String DECISION_COMMENT_FAILED = "FAILED";
  private static final String USERNAME = "governance-app";
  private static final String EMPTY_COMMENT = "";
  private static final OffsetDateTime CREATED_AT = parse("2021-11-26T10:00:00.629+08:00");

  @Autowired
  @Valid
  QaIntegrationProperties properties;
  @Autowired
  CreateAlertWithDecisionUseCase createAlertWithDecisionUseCase;
  @Autowired
  RabbitTemplate rabbitTemplate;

  @Test
  @Order(1)
  @WithMockUser(username = USERNAME, authorities = {QA, QA_ISSUE_MANAGER})
  void createAnalysisDecisionShouldSendSendQaDataIndexRequest() {
    //given
    CreateDecisionRequest createDecisionRequest = getCreateDecisionRequest();
    //when
    createAlertWithDecisionUseCase.activate(createDecisionRequest);
    //then
    get(ALERTS_ANALYSIS_DETAILS_URL)
        .contentType(JSON)
        .statusCode(OK.value())
        .body("alertName", is(ALERT_NAME))
        .body("state", is(NEW.toString()))
        .body("decisionComment", nullValue())
        .body("decisionAt", nullValue())
        .body("addedAt", notNullValue());

    QaDataIndexRequest dataIndexRequest = receiveQaDataIndexRequest();
    assertThat(dataIndexRequest).isNotNull();
    assertThat(dataIndexRequest.getAlertsCount()).isEqualTo(1);
    QaAlert alert = dataIndexRequest.getAlerts(0);
    assertThat(alert.getState()).isEqualTo(State.NEW);
    assertThat(alert.getComment()).isEqualTo(EMPTY_COMMENT);
    assertThat(alert.getLevel()).isEqualTo(LEVEL_ANALYSIS.getValue());
  }

  private QaDataIndexRequest receiveQaDataIndexRequest() {
    return (QaDataIndexRequest) rabbitTemplate.receiveAndConvert(
        properties.getReceive().getQueueName());
  }

  @Test
  @Order(2)
  @WithMockUser(username = USERNAME, authorities = {QA, QA_ISSUE_MANAGER})
  void updateAnalysisDecisionShouldSendQaDataIndexRequest() {
    //given
    get(ALERTS_ANALYSIS_DETAILS_URL)
        .contentType(JSON)
        .statusCode(OK.value())
        .body("alertName", is(ALERT_NAME))
        .body("state", is(NEW.toString()))
        .body("decisionComment", nullValue())
        .body("decisionAt", nullValue())
        .body("addedAt", notNullValue());

    //when
    patch(UPDATE_ANALYSIS_DECISION_URL, getUpdateAnalysisDecisionDto())
        .statusCode(ACCEPTED.value());

    //then
    get(ALERTS_ANALYSIS_DETAILS_URL)
        .contentType(JSON)
        .statusCode(OK.value())
        .body("alertName", is(ALERT_NAME))
        .body("state", is(FAILED.toString()))
        .body("decisionComment", is(COMMENT_FAILED))
        .body("decisionAt", notNullValue())
        .body("addedAt", notNullValue());

    get(ALERTS_VALIDATION_DETAILS_URL)
        .contentType(JSON)
        .statusCode(OK.value())
        .body("alertName", is(ALERT_NAME))
        .body("state", is(NEW.toString()))
        .body("decisionComment", nullValue())
        .body("decisionAt", nullValue())
        .body("addedAt", notNullValue());

    QaDataIndexRequest dataIndexRequest = receiveQaDataIndexRequest();
    assertThat(dataIndexRequest).isNotNull();
    assertThat(dataIndexRequest.getAlertsCount()).isEqualTo(1);
    QaAlert alert = dataIndexRequest.getAlerts(0);
    assertThat(alert.getState()).isEqualTo(State.FAILED);
    assertThat(alert.getComment()).isEqualTo(COMMENT_FAILED);
    assertThat(alert.getLevel()).isEqualTo(LEVEL_ANALYSIS.getValue());
  }

  private UpdateAnalysisDecisionDto getUpdateAnalysisDecisionDto() {
    return UpdateAnalysisDecisionDto
        .builder()
        .state(FAILED)
        .comment(DECISION_COMMENT_FAILED)
        .build();
  }

  private CreateDecisionRequest getCreateDecisionRequest() {
    return CreateDecisionRequest.of(
        ALERT_NAME,
        NEW,
        DecisionLevel.ANALYSIS,
        USERNAME,
        CREATED_AT);
  }
}
