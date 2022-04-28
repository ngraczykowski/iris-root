package com.silenteight.adjudication.engine.alerts.alert;

import com.silenteight.adjudication.engine.testing.RepositoryTestConfiguration;
import com.silenteight.sep.base.testing.BaseDataJpaTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.stream.Stream;
import javax.annotation.Nonnull;

import static com.silenteight.adjudication.engine.alerts.alert.AlertFixtures.randomAlertEntity;
import static java.util.stream.StreamSupport.stream;
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

    alert = getStream(repository.saveAll(List.of(alert))).findFirst().get();
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

    alert = getStream(repository.saveAll(List.of(alert))).findFirst().get();
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

  @Nonnull
  private static <T> Stream<T> getStream(Iterable<T> m) {
    return stream(m.spliterator(), false);
  }
}
