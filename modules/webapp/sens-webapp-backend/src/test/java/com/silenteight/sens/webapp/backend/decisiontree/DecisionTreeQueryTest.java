package com.silenteight.sens.webapp.backend.decisiontree;

import com.silenteight.sens.webapp.backend.decisiontree.dto.DecisionTreesDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.silenteight.sens.webapp.backend.decisiontree.DecisionTreeDtoFixtures.ACTIVE;
import static com.silenteight.sens.webapp.backend.decisiontree.DecisionTreeDtoFixtures.INACTIVE;
import static org.assertj.core.api.Assertions.*;

class DecisionTreeQueryTest {

  private InMemoryDecisionTreeRepository repository;
  private DecisionTreeQuery underTest;

  @BeforeEach
  void setUp() {
    repository = new InMemoryDecisionTreeRepository();
    underTest = new DecisionTreeQuery(repository);
  }

  @Test
  void shouldListNoDecisionTrees() {
    // when
    DecisionTreesDto decisionTrees = underTest.list();

    // then
    assertThat(decisionTrees.getTotal()).isEqualTo(0L);
    assertThat(decisionTrees.getResults()).isEmpty();
  }

  @Test
  void shouldListDecisionTrees() {
    // given
    repository.save(INACTIVE);
    repository.save(ACTIVE);

    // when
    DecisionTreesDto decisionTrees = underTest.list();

    // then
    assertThat(decisionTrees.getTotal()).isEqualTo(2L);
    assertThat(decisionTrees.getResults()).containsExactly(INACTIVE, ACTIVE);
  }
}
