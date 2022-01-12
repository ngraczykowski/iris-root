package com.silenteight.warehouse.common.web.request;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.silenteight.warehouse.common.web.request.AlertResource.fromResourceName;
import static com.silenteight.warehouse.common.web.request.AlertResource.toResourceName;
import static java.lang.String.format;
import static java.util.UUID.fromString;
import static org.assertj.core.api.Assertions.*;

class AlertResourceTest {

  private static final UUID ALERT_ID = fromString("83d8f219-9a1a-44b4-8149-70fdc0279d49");
  private static final String ALERT_NAME = format("alerts/%s", ALERT_ID);

  @Test
  void toResourceNameShouldReturnAlertName() {
    assertThat(toResourceName(ALERT_ID)).isEqualTo(ALERT_NAME);
  }

  @Test
  void fromResourceNameShouldReturnAlertId() {
    assertThat(fromResourceName(ALERT_NAME)).isEqualTo(ALERT_ID);
  }
}
