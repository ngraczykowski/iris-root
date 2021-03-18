package com.silenteight.adjudication.engine.alerts.alert;

import com.silenteight.adjudication.engine.testing.RepositoryTestConfiguration;
import com.silenteight.sep.base.testing.BaseDataJpaTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.*;

@ContextConfiguration(classes = RepositoryTestConfiguration.class)
class AlertRepositoryIT extends BaseDataJpaTest {

  @Autowired
  private AlertRepository repository;

  @Test
  void persistAndFindWithEntityManagerReturnsSameAlert() {
    var alert = AlertEntity.builder()
        .clientAlertIdentifier("AE_BTCH_PEPL!1391FFFD-2E8A4AC7-B0E596E6-2FE1CEC1")
        .alertedAt(OffsetDateTime.now().minusDays(1))
        .label("batch_type", "AE_BTCH_PEPL")
        .build();

    entityManager.persistAndFlush(alert);
    entityManager.clear();

    var foundAlert = entityManager.find(AlertEntity.class, alert.getId());

    assertThat(foundAlert).usingRecursiveComparison().ignoringFields("id").isEqualTo(alert);
  }
}
