package com.silenteight.sens.webapp.backend.decisiontree;

import com.silenteight.sens.webapp.backend.decisiontree.dto.DecisionTreeDetailsDto;
import com.silenteight.sens.webapp.backend.decisiontree.dto.DecisionTreesDto;
import com.silenteight.sens.webapp.backend.decisiontree.exception.DecisionTreeNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.silenteight.sens.webapp.backend.decisiontree.DecisionTreeDtoFixtures.ACTIVE;
import static com.silenteight.sens.webapp.backend.decisiontree.DecisionTreeDtoFixtures.INACTIVE;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DecisionTreeQueryTest {

  private InMemoryDecisionTreeRepository repository;
  private DecisionTreeQuery underTest;

  @BeforeEach
  void setUp() {
    repository = new InMemoryDecisionTreeRepository();
    underTest = new DecisionTreeQuery(repository);
  }

  @Test
  void emptyDecisionTreesListWhenDecisionTreesNotAvailable() {
    // when
    DecisionTreesDto decisionTrees = underTest.list();

    // then
    assertThat(decisionTrees.getTotal()).isEqualTo(0L);
    assertThat(decisionTrees.getResults()).isEmpty();
  }

  @Test
  void decisionTreesListWhenDecisionTreesAvailable() {
    // given
    repository.save(INACTIVE);
    repository.save(ACTIVE);

    // when
    DecisionTreesDto decisionTrees = underTest.list();

    // then
    assertThat(decisionTrees.getTotal()).isEqualTo(2L);
    assertThat(decisionTrees.getResults()).containsExactlyInAnyOrder(INACTIVE, ACTIVE);
  }

  @Test
  void throwDecisionTreeNotFoundExceptionWhenDecisionTreeNotAvailable() {
    // when, then
    assertThrows(DecisionTreeNotFoundException.class, () -> underTest.details(INACTIVE.getId()));
  }

  @Test
  void decisionTreeDetailsWhenDecisionTreeAvailable() {
    // given
    repository.save(ACTIVE);
    DecisionTreeDetailsDto expectedDetails = DecisionTreeDetailsDto
        .builder()
        .id(ACTIVE.getId())
        .name(ACTIVE.getName())
        .status(ACTIVE.getStatus())
        .activations(ACTIVE.getActivations())
        .build();

    // when
    DecisionTreeDetailsDto details = underTest.details(ACTIVE.getId());

    // then
    assertThat(details).isEqualTo(expectedDetails);
  }
}
