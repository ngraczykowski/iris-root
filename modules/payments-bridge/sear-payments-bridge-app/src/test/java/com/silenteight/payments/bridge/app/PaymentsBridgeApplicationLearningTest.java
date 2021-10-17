package com.silenteight.payments.bridge.app;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.mock.ae.MockAlertUseCase;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningRequest;
import com.silenteight.payments.bridge.svb.learning.reader.port.HandleLearningAlertsUseCase;
import com.silenteight.sep.base.testing.containers.PostgresContainer.PostgresTestInitializer;
import com.silenteight.sep.base.testing.containers.RabbitContainer.RabbitTestInitializer;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Disabled
@SpringBootTest(classes = PaymentsBridgeApplication.class)
@ContextConfiguration(initializers = { RabbitTestInitializer.class, PostgresTestInitializer.class })
@Slf4j
@ActiveProfiles({ "mockae", "mockaws", "mockdatasource", "mockgovernance", "test" })
class PaymentsBridgeApplicationLearningTest {

  @Autowired
  HandleLearningAlertsUseCase handleLearningDataUseCase;

  @Test
  void shouldProcessLearningCsv() {
    handleLearningDataUseCase.readAlerts(new LearningRequest("bucket", "object"));
    assertThat(MockAlertUseCase.getCreatedAlertsCount()).isEqualTo(163 + 1);
  }
}
