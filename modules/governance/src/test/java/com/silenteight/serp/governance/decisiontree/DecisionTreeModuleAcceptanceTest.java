package com.silenteight.serp.governance.decisiontree;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.*;

class DecisionTreeModuleAcceptanceTest {

  private static final String TREE_NAME = "tree";
  private static final String OTHER_TREE_NAME = "other tree";

  private DecisionTreeFacade facade;
  private TestingInMemoryDecisionTreeRepository repository;

  @BeforeEach
  void setUp() {
    repository = new TestingInMemoryDecisionTreeRepository();
    var getOrCreateDecisionTreeUseCase = new GetOrCreateDecisionTreeUseCase(repository);
    facade = new DecisionTreeFacade(repository, getOrCreateDecisionTreeUseCase);
  }

  @Test
  @DisplayName("Method ensureAtLeastOneDecisionTreeExists should create tree when none exists")
  void treeDoesntExistForGivenFeaturesVector_ensureMethodCreatesOne() {
    facade.ensureAtLeastOneDecisionTreeExists();

    assertThat(repository.count()).isEqualTo(1);
  }

  @Test
  @DisplayName("Method ensureAtLeastOneDecisionTreeExists shouldn't create tree"
      + "when there's one")
  void treeExistsForGivenFeaturesVector_ensureMethodDoesntCreateOne() {
    long createdTreeId = facade.getOrCreate(TREE_NAME);

    facade.ensureAtLeastOneDecisionTreeExists();

    assertThat(repository.getIds()).containsOnly(createdTreeId);
  }

  @Test
  void giveNoDecisionTrees_getOrCreatePersistsTree() {
    long treeId = facade.getOrCreate(TREE_NAME);

    assertThat(repository.getIds()).containsOnly(treeId);
  }

  @Test
  void treeIdExists_getOrCreatePersistsTree() {
    long treeId = facade.getOrCreate(TREE_NAME);

    assertThat(repository.existsById(treeId)).isTrue();
  }

  @Test
  void givenTreeExists_getOrCreateGetsExistingTree() {
    long existingTreeId = facade.getOrCreate(TREE_NAME);

    long actualTreeId = facade.getOrCreate(TREE_NAME);

    assertThat(actualTreeId).isEqualTo(existingTreeId);
  }

  @Test
  void afterGettingOrCreatingTwoTreesWithSameName_repoContainsOnlyOneEntity() {
    facade.getOrCreate(TREE_NAME);
    facade.getOrCreate(TREE_NAME);

    assertThat(repository.count()).isEqualTo(1);
  }

  @Test
  void afterImportingTheTree_itIsDefaultForAutomaticDecisionGroupActivation() {
    long treeId = facade.getOrCreate(TREE_NAME);

    DecisionTreeId defaultTree = facade.findDefaultTreeForNewGroups().orElseGet(null);
    assertThat(defaultTree.getId()).isEqualTo(treeId);
  }

  @Test
  void afterImportingTheTree_allOldTressStopToBeDefault() {
    long treeId = facade.getOrCreate(TREE_NAME);
    facade.getOrCreate(OTHER_TREE_NAME);

    DecisionTreeId defaultTree = facade.findDefaultTreeForNewGroups().orElseGet(null);
    assertThat(defaultTree.getId()).isNotEqualTo(treeId);
  }

  private static class TestingInMemoryDecisionTreeRepository
      extends InMemoryDecisionTreeRepository {

    Collection<Long> getIds() {
      return getInternalStore()
          .values()
          .stream()
          .map(DecisionTree::getId)
          .collect(toList());
    }
  }
}
