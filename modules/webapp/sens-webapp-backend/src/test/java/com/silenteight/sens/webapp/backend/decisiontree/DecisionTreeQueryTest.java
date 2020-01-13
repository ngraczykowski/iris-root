package com.silenteight.sens.webapp.backend.decisiontree;

import com.silenteight.sens.webapp.backend.decisiontree.dto.DecisionTreeDto;
import com.silenteight.sens.webapp.backend.decisiontree.dto.DecisionTreesDto;
import com.silenteight.sens.webapp.backend.decisiontree.dto.StatusDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.*;

class DecisionTreeQueryTest {

  private Fixtures fixtures = new Fixtures();
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
    repository.save(fixtures.firstDecisionTree);
    repository.save(fixtures.secondDecisionTree);
    repository.save(fixtures.thirdDecisionTree);

    // when
    DecisionTreesDto decisionTrees = underTest.list();

    // then
    assertThat(decisionTrees.getTotal()).isEqualTo(3L);
    assertThat(decisionTrees.getResults()).containsExactly(
        fixtures.firstDecisionTree, fixtures.secondDecisionTree, fixtures.thirdDecisionTree);
  }

  private static class Fixtures {

    private static final long ID_1 = 1L;
    private static final long ID_2 = 2L;
    private static final long ID_3 = 3L;
    private static final String NAME_1 = "name-1";
    private static final String NAME_2 = "name-2";
    private static final String NAME_3 = "name-3";
    private static final String STATUS_NAME = "status-name";
    private static final List<String> ACTIVATIONS = asList("activation-1", "activation-2");

    DecisionTreeDto firstDecisionTree = DecisionTreeDto
        .builder()
        .id(ID_1)
        .name(NAME_1)
        .status(createStatus())
        .activations(emptyList())
        .build();

    DecisionTreeDto secondDecisionTree = DecisionTreeDto
        .builder()
        .id(ID_2)
        .name(NAME_2)
        .status(createStatus())
        .activations(ACTIVATIONS)
        .build();

    DecisionTreeDto thirdDecisionTree = DecisionTreeDto
        .builder()
        .id(ID_3)
        .name(NAME_3)
        .status(createStatus())
        .activations(ACTIVATIONS)
        .build();

    private static StatusDto createStatus() {
      return StatusDto
          .builder()
          .name(STATUS_NAME)
          .build();
    }
  }
}
