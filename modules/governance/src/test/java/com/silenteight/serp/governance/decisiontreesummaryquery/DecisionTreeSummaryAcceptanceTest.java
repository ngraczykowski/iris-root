package com.silenteight.serp.governance.decisiontreesummaryquery;

import com.silenteight.proto.serp.v1.governance.DecisionTreeSummary;
import com.silenteight.serp.governance.decisiontreesummaryquery.TestInMemoryDecisionTreeQueryRepository.DecisionTreeToStore;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import javax.persistence.EntityNotFoundException;

import static java.util.Collections.emptyList;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DecisionTreeSummaryAcceptanceTest {

  private TestInMemoryDecisionTreeQueryRepository repository;

  private DecisionTreeSummaryFinder finderUnderTest;

  @BeforeEach
  void setUp() {
    repository = new TestInMemoryDecisionTreeQueryRepository();
    var conf = new DecisionTreeSummaryQueryConfiguration(repository);

    finderUnderTest = conf.decisionTreeSummaryFinder();
  }

  @Test
  void getByIdShouldReturnMappedTreeSummary() {
    storeTree(of("a", "b", "c"), "treeName", 1L);

    DecisionTreeSummary actual = finderUnderTest.getById(1L);

    assertThat(actual.getId()).isEqualTo(1L);
    assertThat(actual.getName()).isEqualTo("treeName");
    assertThat(actual.getDecisionGroupList()).containsExactly("a", "b", "c");
  }

  @Test
  void getAllShouldReturnAllTreeSummaries() {
    storeTree(of("a", "b", "c"), "treeName", 1L);
    storeTree(emptyList(), "treeName2", 2L);

    Collection<DecisionTreeSummary> actual = finderUnderTest.getAll();

    assertThat(actual).hasSize(2);
  }

  @Test
  void getByDecisionGroupNameShouldReturnActivatedTree() {
    storeTree(of("group1", "group2", "group3"), "treeName", 1L);
    storeTree(emptyList(), "treeName2", 2L);

    assertThat(finderUnderTest.getByDecisionGroupName("group1"))
        .isEqualTo(finderUnderTest.getByDecisionGroupName("group2"))
        .isEqualTo(finderUnderTest.getByDecisionGroupName("group3"))
        .extracting(DecisionTreeSummary::getId).isEqualTo(1L);
  }

  @Test
  void findByIdShouldReturnEmpty_whenThereIsNoSuchTree() {
    assertThat(finderUnderTest.findById(2L)).isEmpty();
  }

  @Test
  void findByDecisionGroupNameShouldReturnEmpty_whenThereIsNoSuchTree() {
    assertThat(finderUnderTest.findByDecisionGroupName("groupName")).isEmpty();
  }

  @Test
  void getByIdShouldThrowEntityNotFoundException_whenThereIsNoSuchTree() {
    assertThatThrownBy(() -> finderUnderTest.getById(1L))
        .isInstanceOf(EntityNotFoundException.class);
  }

  @Test
  void getByDecisionGroupNameShouldThrowEntityNotFoundException_whenThereIsNoActivatedTrees() {
    storeTree(emptyList(), "treeName", 1L);

    assertThrows(
        EntityNotFoundException.class,
        () -> finderUnderTest.getByDecisionGroupName("group"));
  }

  @Test
  void findAndGetByIdShouldReturnTheSameResult() {
    storeTree(of("a", "b", "c"), "treeName", 1L);

    assertThat(finderUnderTest.findById(1L))
        .hasValueSatisfying(a -> assertThat(a).isEqualTo(finderUnderTest.getById(1L)));
  }

  @Test
  void findAndGetByDecisionGroupShouldReturnTheSameResult() {
    storeTree(of("group1"), "treeName", 1L);

    assertThat(finderUnderTest.findByDecisionGroupName("group1"))
        .hasValueSatisfying(a -> assertThat(a)
            .isEqualTo(finderUnderTest.getByDecisionGroupName("group1")));
  }

  private void storeTree(List<String> groups, String name, long id) {
    DecisionTreeToStore decisionTree =
        DecisionTreeToStore.builder()
            .decisionTreeId(id)
            .decisionTreeName(name)
            .decisionGroupNames(groups)
            .build();
    repository.store(decisionTree);
  }
}
