package com.silenteight.sens.webapp.backend.domain.decisiontree;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.*;

class DecisionTreeServiceTest {

  private Fixtures fixtures = new Fixtures();
  private InMemoryDecisionTreeRepository repository;
  private DecisionTreeService underTest;

  @BeforeEach
  void setUp() {
    repository = new InMemoryDecisionTreeRepository();
    underTest = new DecisionTreeService(repository);
  }

  @Test
  void shouldListNoDecisionTrees() {
    // when
    List<DecisionTreeView> decisionTrees = underTest.list();

    // then
    assertThat(decisionTrees).isEmpty();
  }

  @Test
  void shouldListDecisionTrees() {
    // given
    repository.save(fixtures.firstDecisionTree);
    repository.save(fixtures.thirdDecisionTree);
    repository.save(fixtures.secondDecisionTree);


    // when
    List<DecisionTreeView> decisionTrees = underTest.list();

    // then
    assertThat(decisionTrees).containsExactly(
        fixtures.secondDecisionTree, fixtures.thirdDecisionTree, fixtures.firstDecisionTree);
  }

  private static class Fixtures {

    private static final long ID_1 = 1L;
    private static final long ID_2 = 2L;
    private static final long ID_3 = 3L;
    private static final String NAME_1 = "name-1";
    private static final String NAME_2 = "name-2";
    private static final String NAME_3 = "name-3";
    private static final List<String> ACTIVATIONS = asList("activation-1", "activation-2");

    DecisionTreeView firstDecisionTree = DecisionTreeView
        .builder()
        .id(ID_1)
        .name(NAME_1)
        .activations(emptyList())
        .build();

    DecisionTreeView secondDecisionTree = DecisionTreeView
        .builder()
        .id(ID_2)
        .name(NAME_2)
        .activations(ACTIVATIONS)
        .build();

    DecisionTreeView thirdDecisionTree = DecisionTreeView
        .builder()
        .id(ID_3)
        .name(NAME_3)
        .activations(ACTIVATIONS)
        .build();
  }
}
