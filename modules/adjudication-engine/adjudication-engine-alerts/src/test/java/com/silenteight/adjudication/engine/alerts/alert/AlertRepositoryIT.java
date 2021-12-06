package com.silenteight.adjudication.engine.alerts.alert;

import com.silenteight.adjudication.engine.testing.RepositoryTestConfiguration;
import com.silenteight.sep.base.testing.BaseDataJpaTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static com.silenteight.adjudication.engine.alerts.alert.AlertFixtures.randomAlertEntity;
import static org.assertj.core.api.Assertions.*;

@ContextConfiguration(classes = RepositoryTestConfiguration.class)
class AlertRepositoryIT extends BaseDataJpaTest {

  @Autowired
  private AlertRepository repository;

  @Test
  void persistAndFindWithEntityManagerReturnsSameAlert() {
    var alert = randomAlertEntity();

    alert = entityManager.persistAndFlush(alert);
    entityManager.clear();

    compareAlertInDatabase(alert);
  }

  @Test
  void savesAlertToRepository() {
    var alert = randomAlertEntity();

    alert = repository.save(alert);
    entityManager.flush();
    entityManager.clear();

    compareAlertInDatabase(alert);
  }

  private void compareAlertInDatabase(AlertEntity alert) {
    var foundAlert = entityManager.find(AlertEntity.class, alert.getId());

    assertThat(foundAlert).usingRecursiveComparison().ignoringFields("id").isEqualTo(alert);
  }

  @Test
  void deleteAlertFromRepository() {
    var alert = randomAlertEntity();

    alert = repository.save(alert);
    entityManager.flush();

    int deletedAlertCount = repository.deleteAllByIdIn(List.of(alert.getId()));
    entityManager.flush();
    entityManager.clear();

    assertThat(deletedAlertCount).isEqualTo(1);
    checkIfAlertIsNotInDatabase(alert.getId());
  }

  private void checkIfAlertIsNotInDatabase(long alertId) {
    var foundAlert = entityManager.find(AlertEntity.class, alertId);
    Assertions.assertNull(foundAlert);
  }
}
