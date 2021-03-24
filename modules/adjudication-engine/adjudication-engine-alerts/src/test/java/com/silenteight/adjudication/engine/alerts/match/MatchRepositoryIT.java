package com.silenteight.adjudication.engine.alerts.match;

import com.silenteight.adjudication.engine.testing.RepositoryTestConfiguration;
import com.silenteight.sep.base.testing.BaseDataJpaTest;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.*;

@ContextConfiguration(classes = RepositoryTestConfiguration.class)
@Sql
class MatchRepositoryIT extends BaseDataJpaTest {

  private static final long ALERT_ID = 123;

  @Autowired
  private MatchRepository repository;

  @Test
  void savesMatchToRepository() {
    MatchEntity match = givenMatch(1);
    compareMatchInDatabase(match);
  }

  private void compareMatchInDatabase(MatchEntity match) {
    var foundAlert = entityManager.find(MatchEntity.class, match.getId());

    assertThat(foundAlert).usingRecursiveComparison().ignoringFields("id").isEqualTo(match);
  }

  @Test
  void shouldNotFindAnySortIndexForEmptyRepository() {
    assertThat(repository.findFirstByAlertIdOrderBySortIndexDesc(1)).isEmpty();
  }

  @Test
  void shouldFindLatestSortIndex() {
    givenMatch(1);
    givenMatch(2);

    assertThat(repository.findFirstByAlertIdOrderBySortIndexDesc(ALERT_ID))
        .isNotEmpty()
        .hasValueSatisfying(r -> assertThat(r.getSortIndex()).isEqualTo(2));

    givenMatch(20);

    assertThat(repository.findFirstByAlertIdOrderBySortIndexDesc(ALERT_ID))
        .isNotEmpty()
        .hasValueSatisfying(r -> assertThat(r.getSortIndex()).isEqualTo(20));
  }

  @NotNull
  private MatchEntity givenMatch(int sortIndex) {
    var match = MatchEntity.builder()
        .alertId(ALERT_ID)
        .clientMatchIdentifier("test match " + sortIndex)
        .label("test label", "test value")
        .sortIndex(sortIndex)
        .build();

    match = repository.save(match);
    entityManager.flush();
    entityManager.clear();
    return match;
  }
}
