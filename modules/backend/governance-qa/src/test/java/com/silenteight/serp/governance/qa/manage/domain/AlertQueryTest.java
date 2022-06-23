package com.silenteight.serp.governance.qa.manage.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.silenteight.serp.governance.qa.AlertFixture.*;
import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AlertQueryTest {

  private InMemoryAlertRepository alertRepository;
  private AlertQuery underTest;

  @BeforeEach
  void setUp() {
    alertRepository = new InMemoryAlertRepository();
    underTest = new AlertQuery(alertRepository);
  }

  @Test
  void findByAlertNameShouldReturnOneAlertName() {
    //given
    List<String> alertNames = of(ALERT_NAME);
    alertRepository.save(getAlert(ALERT_NAME));
    alertRepository.save(getAlert(ALERT_NAME_2));
    Optional<Alert> savedAlert = alertRepository.findByAlertName(ALERT_NAME);
    //when
    List<Long> alertIds = underTest.findIdsForAlertsNames(alertNames);
    //then
    assertTrue(savedAlert.isPresent());
    assertEquals(of(savedAlert.get().getId()), alertIds);
  }

  private Alert getAlert(String alertName) {
    Alert alert = new Alert();
    alert.setAlertName(alertName);
    return alert;
  }
}
