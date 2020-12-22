package com.silenteight.serp.governance.decisiontree;

import com.silenteight.sep.base.testing.BaseDataJpaTest;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;
import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;

@ContextConfiguration(classes = { DecisionTreeModuleConfiguration.class })
class DecisionTreeRepositoryTestIT extends BaseDataJpaTest {

  private static final String TREE_NAME_1 = "tree1";
  private static final String TREE_NAME_2 = "tree2";

  @Autowired
  private DecisionTreeRepository repository;

  @Autowired
  private EntityManager entityManager;

  @Test
  void twoDifferentTreesInRepo_findByNameWorksCorrectly() {
    repository.save(new DecisionTree(TREE_NAME_1));
    repository.save(new DecisionTree(TREE_NAME_2));

    Optional<DecisionTree> treeOpt = repository.findByName(TREE_NAME_1);
    assertThat(treeOpt).isNotEmpty();
  }

  @Test
  void cantAddTwoTreesWithSameName() {
    repository.save(new DecisionTree(TREE_NAME_1));

    ThrowingCallable when = () -> repository.save(new DecisionTree(TREE_NAME_1));

    assertThatThrownBy(when).isInstanceOf(RuntimeException.class);
  }

  @Test
  void setAllDefaultForNewGroupsToFalse() {
    // given
    repository.save(new DecisionTree(TREE_NAME_1).enableForAutomaticDecisionGroupActivation());
    repository.save(new DecisionTree(TREE_NAME_2));

    // when
    repository.setAllDefaultForNewGroupsToFalse();
    entityManager.clear();

    // and
    DecisionTree tree1 = repository.findByName(TREE_NAME_1).orElseGet(null);
    DecisionTree tree2 = repository.findByName(TREE_NAME_2).orElseGet(null);

    // then
    assertThat(tree1.isDefaultForNewGroups()).isFalse();
    assertThat(tree2.isDefaultForNewGroups()).isFalse();
  }

  @Test
  void treeDefaultForNewGroupsExists_findByDefaultForNewGroupsReturnsIt() {
    // given
    repository.save(new DecisionTree(TREE_NAME_1).enableForAutomaticDecisionGroupActivation());

    // when
    Optional<DecisionTreeId> treeId = repository.findByDefaultForNewGroups();

    // then
    assertThat(treeId).isNotEmpty();
  }

  @Test
  void treeDefaultForNewGroupsDoesNotExist_findByDefaultForNewGroupsReturnsNothing() {
    // given
    repository.save(new DecisionTree(TREE_NAME_1));

    // when
    Optional<DecisionTreeId> treeId = repository.findByDefaultForNewGroups();

    // then
    assertThat(treeId).isEmpty();
  }
}
