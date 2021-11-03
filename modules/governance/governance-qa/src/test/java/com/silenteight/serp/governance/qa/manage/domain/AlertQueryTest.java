package com.silenteight.serp.governance.qa.manage.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.silenteight.serp.governance.qa.AlertFixture.DISCRIMINATOR;
import static com.silenteight.serp.governance.qa.AlertFixture.DISCRIMINATOR_2;
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
  void findByDiscriminatorsShouldReturnOneDiscriminator() {
    //given
    List<String> discriminators = of(DISCRIMINATOR);
    alertRepository.save(getAlert(DISCRIMINATOR));
    alertRepository.save(getAlert(DISCRIMINATOR_2));
    Optional<Alert> savedAlert = alertRepository.findByDiscriminator(DISCRIMINATOR);
    //when
    List<Long> alertIds = underTest.findIdsForDiscriminators(discriminators);
    //then
    assertTrue(savedAlert.isPresent());
    assertEquals(of(savedAlert.get().getId()), alertIds);
  }

  private Alert getAlert(String discriminator) {
    Alert alert = new Alert();
    alert.setDiscriminator(discriminator);
    return alert;
  }
}
