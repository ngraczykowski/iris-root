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
  void emptyDecisionTreesListDetailsWhenDecisionTreesNotAvailable() {
    // when
    DecisionTreesDto decisionTrees = underTest.list();

    // then
    assertThat(decisionTrees.getTotal()).isEqualTo(0L);
    assertThat(decisionTrees.getResults()).isEmpty();
  }

  @Test
  void decisionTreesListDetailsWhenDecisionTreesAvailable() {
    // given
    repository.save(INACTIVE);
    repository.save(ACTIVE);

    // when
    DecisionTreesDto decisionTrees = underTest.list();

    // then
    assertThat(decisionTrees.getTotal()).isEqualTo(2L);
    assertThat(decisionTrees.getResults()).containsExactly(INACTIVE, ACTIVE);
  }

  @Test
  void throwDecisionTreeNotFoundExceptionWhenDecisionTreeNotAvailable() {
    // when, then
    assertThrows(DecisionTreeNotFoundException.class, () -> underTest.details(INACTIVE.getId()));
  }

  @Test
  void decisionTreeDetailsWhenDecisionTreeAvailable() {
    // given
    repository.save(INACTIVE);
    DecisionTreeDetailsDto expectedDetails = DecisionTreeDetailsDto
        .builder()
        .id(INACTIVE.getId())
        .name(INACTIVE.getName())
        .status(INACTIVE.getStatus())
        .build();

    // when
    DecisionTreeDetailsDto details = underTest.details(INACTIVE.getId());

    // then
    assertThat(details).isEqualTo(expectedDetails);
  }
}
