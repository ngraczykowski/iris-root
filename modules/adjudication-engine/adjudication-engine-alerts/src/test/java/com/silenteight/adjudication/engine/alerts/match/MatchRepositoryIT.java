package com.silenteight.adjudication.engine.alerts.match;

import com.silenteight.adjudication.engine.alerts.match.MatchRepository.LatestSortIndex;
import com.silenteight.adjudication.engine.testing.RepositoryTestConfiguration;
import com.silenteight.sep.base.testing.BaseDataJpaTest;

import org.assertj.core.api.AbstractOptionalAssert;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ContextConfiguration(classes = RepositoryTestConfiguration.class)
@Sql
class MatchRepositoryIT extends BaseDataJpaTest {

  private static final long ALERT_ID_1 = 123;
  private static final long ALERT_ID_2 = 124;

  @Autowired private MatchRepository repository;

  @Test
  void savesMatchToRepository() {
    MatchEntity match = givenMatch(ALERT_ID_1, 1);
    compareMatchInDatabase(match);
  }

  private void compareMatchInDatabase(MatchEntity match) {
    var foundAlert = entityManager.find(MatchEntity.class, match.getId());

    assertThat(foundAlert)
        .usingRecursiveComparison()
        .ignoringFields("id", "alertedAt", "createdAt")
        .isEqualTo(match);
  }

  @Test
  void shouldNotFindAnySortIndexForEmptyRepository() {
    assertThatLastSortIndex(ALERT_ID_1).isEmpty();
  }

  @Test
  void shouldFindLatestSortIndex() {
    givenMatch(ALERT_ID_1, 1);
    givenMatch(ALERT_ID_1, 2);
    assertThatLastSortIndex(ALERT_ID_1).isNotEmpty().hasValue(2);

    givenMatch(ALERT_ID_1, 20);
    assertThatLastSortIndex(ALERT_ID_1).isNotEmpty().hasValue(20);
  }

  @Test
  void shouldFindLatestSortIndexesForMultipleAlerts() {
    givenMatch(ALERT_ID_1, 1);
    givenMatch(ALERT_ID_1, 2);

    givenMatch(ALERT_ID_2, 4);

    var latestSortIndexes =
        repository.findLatestSortIndexByAlertIds(List.of(ALERT_ID_1, ALERT_ID_2));

    assertThat(latestSortIndexes)
        .hasSize(2)
        .anySatisfy(
            a -> {
              assertThat(a.getAlertId()).isEqualTo(ALERT_ID_1);
              assertThat(a.getSortIndex()).isEqualTo(2);
            })
        .anySatisfy(
            a -> {
              assertThat(a.getAlertId()).isEqualTo(ALERT_ID_2);
              assertThat(a.getSortIndex()).isEqualTo(4);
            });
  }

  private AbstractOptionalAssert<?, Integer> assertThatLastSortIndex(long alertId) {
    return assertThat(
        repository
            .findLatestSortIndexByAlertIds(List.of(alertId))
            .map(LatestSortIndex::getSortIndex)
            .findFirst());
  }

  @NotNull
  private MatchEntity givenMatch(long alertId, int sortIndex) {
    final var match =
        MatchEntity.builder()
            .alertId(alertId)
            .clientMatchIdentifier("test match " + sortIndex)
            .label("test label", "test value")
            .sortIndex(sortIndex)
            .build();

    var matches = repository.saveAll(() -> List.of(match).iterator());
    entityManager.flush();
    entityManager.clear();
    return matches.iterator().next();
  }
}
