package com.silenteight.payments.bridge.svb.newlearning.step.etl;

import com.silenteight.payments.bridge.svb.migration.DecisionMapperConfiguration;
import com.silenteight.payments.bridge.svb.migration.DecisionMapperFactory;
import com.silenteight.payments.bridge.svb.newlearning.domain.ActionComposite;
import com.silenteight.payments.bridge.warehouse.index.model.learning.IndexAnalystDecision;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.OffsetDateTime;
import java.util.List;

@SpringBootTest(classes = {
    DecisionMapperConfiguration.class, DecisionMapperFactory.class,
    IndexAnalystDecisionHelper.class })
class IndexAnalystDecisionHelperTest {

  @Autowired private IndexAnalystDecisionHelper indexAnalystDecisionHelper;

  @Test
  @DisplayName("0 action should throw Exception")
  void passEmptyAction_shouldThrowException() {
    Assertions.assertThrows(
        Exception.class, () -> indexAnalystDecisionHelper.getDecision(List.of()));
  }

  @Test
  @DisplayName("1 action(with wrong statusName) should return 'analyst_decision_unknown' decision")
  void passActionShouldReturn_analyst_decision_unknown() {
    IndexAnalystDecision decision = indexAnalystDecisionHelper.getDecision(List.of(ActionComposite
        .builder()
        .actionComment("Random status")
        .statusName("RANDOMAASDFASDF_STATUS_1231213512")
        .actionDatetime(OffsetDateTime.now())
        .actionId(1L)
        .build()));
    Assertions.assertEquals("analyst_decision_unknown", decision.getDecision());
  }

  @Test
  @DisplayName(
      "2 actions(with correct statusName) should return 'analyst_decision_true_positive' decision")
  void passActionsShouldReturn_analyst_decision_true_positive() {
    IndexAnalystDecision decision = indexAnalystDecisionHelper.getDecision(List.of(
        ActionComposite
            .builder()
            .actionComment("current")
            .statusName("L3_BLOCK")
            .statusBehaviour("PENDING")
            .actionDatetime(OffsetDateTime.now())
            .actionId(1L)
            .build(),
        ActionComposite
            .builder()
            .actionComment("previous")
            .statusName("L2_BLOCK")
            .statusBehaviour("PENDING")
            .actionDatetime(OffsetDateTime.now().minusMinutes(1L))
            .actionId(2L)
            .build()

    ));
    Assertions.assertEquals("analyst_decision_true_positive", decision.getDecision());
  }

  @Test
  @DisplayName("2 actions(with current wrong statusName) should return 'analyst_decision_unknown'")
  void passActionsShoulReturn_analyst_decision_unknown() {
    IndexAnalystDecision decision = indexAnalystDecisionHelper.getDecision(List.of(
        ActionComposite
            .builder()
            .actionComment("current")
            .statusName("RANDOMAASDFASDF_STATUS_1231213512")
            .statusBehaviour("PENDING")
            .actionDatetime(OffsetDateTime.now())
            .actionId(1L)
            .build(),
        ActionComposite
            .builder()
            .actionComment("previous")
            .statusName("L2_BLOCK")
            .statusBehaviour("PENDING")
            .actionDatetime(OffsetDateTime.now().minusMinutes(1L))
            .actionId(2L)
            .build()
    ));
    Assertions.assertEquals("analyst_decision_unknown", decision.getDecision());
  }
}
