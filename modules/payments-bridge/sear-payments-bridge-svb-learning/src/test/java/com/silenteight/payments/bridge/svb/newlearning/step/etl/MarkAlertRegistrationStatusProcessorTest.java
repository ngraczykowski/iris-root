package com.silenteight.payments.bridge.svb.newlearning.step.etl;

import com.silenteight.payments.bridge.svb.newlearning.domain.AlertComposite;
import com.silenteight.payments.bridge.svb.newlearning.job.FindRegisteredAlertPortMock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.silenteight.payments.bridge.svb.newlearning.domain.AlertRegistrationStatus.REGISTERED;
import static com.silenteight.payments.bridge.svb.newlearning.domain.AlertRegistrationStatus.UNREGISTERED;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class MarkAlertRegistrationStatusProcessorTest {

  private MarkAlertRegistrationStatusProcessor markAlertRegistrationStatusProcessor;

  @BeforeEach
  void setUp() {
    markAlertRegistrationStatusProcessor =
        new MarkAlertRegistrationStatusProcessor(new FindRegisteredAlertPortMock());
  }

  @Test
  void shouldMarkAlertAsUnregistered() {
    var etlAlert = markAlertRegistrationStatusProcessor.process(AlertComposite.builder().build());

    assertThat(etlAlert).isNotNull();
    assertThat(etlAlert.getStatus()).isEqualTo(UNREGISTERED);
  }

  @Test
  void shouldMarkAlertAsRegistered() {
    var etlAlert = markAlertRegistrationStatusProcessor.process(
        AlertComposite.builder().systemId("DIN20190429085242-00061-24304")
            .messageId("87AB4899-BE5B-5E4F-E053-150A6C0A7A84").build());

    assertThat(etlAlert).isNotNull();
    assertThat(etlAlert.getStatus()).isEqualTo(REGISTERED);
  }
}
