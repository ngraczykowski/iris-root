package com.silenteight.payments.bridge.ae.alertregistration.adapter.jdbc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@EnableConfigurationProperties
@ActiveProfiles("test")
@SpringBootTest(classes = CurrentAnalysisQueryProperties.class)
@TestPropertySource(locations = "classpath:application.yml")
class CurrentAnalysisQueryPropertiesIT {


  @Autowired
  private CurrentAnalysisQueryProperties currentAnalysisQueryProperties;

  @Test
  @DisplayName("Should read configuration properly ")
  public void shouldReadConfigurationProperly() {

    final Duration newAnalysisInterval = currentAnalysisQueryProperties.getNewAnalysisInterval();

    assertNotNull(newAnalysisInterval);
    assertEquals(30, newAnalysisInterval.toMinutes());

  }
}
