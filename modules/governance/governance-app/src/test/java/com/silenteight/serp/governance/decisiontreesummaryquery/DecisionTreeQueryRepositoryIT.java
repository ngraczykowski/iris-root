package com.silenteight.serp.governance.decisiontreesummaryquery;

import com.silenteight.sep.base.testing.BaseDataJpaTest;
import com.silenteight.serp.governance.activation.ActivationModule;
import com.silenteight.serp.governance.activation.ActivationService;
import com.silenteight.serp.governance.activation.dto.ActivationRequest;
import com.silenteight.serp.governance.decisiongroup.DecisionGroupModule;
import com.silenteight.serp.governance.decisiongroup.DecisionGroupService;
import com.silenteight.serp.governance.decisiontree.DecisionTreeFacade;
import com.silenteight.serp.governance.decisiontree.DecisionTreeModule;
import com.silenteight.serp.governance.decisiontreesummaryquery.DecisionTreeQueryRepositoryIT.DecisionTreeQueryRepositoryTestConfiguration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.google.common.primitives.Longs.asList;
import static org.assertj.core.api.Assertions.*;

@ContextConfiguration(classes = { DecisionTreeQueryRepositoryTestConfiguration.class })
class DecisionTreeQueryRepositoryIT extends BaseDataJpaTest {

  @Autowired
  private DecisionTreeFacade decisionTreeService;
  @Autowired
  private DecisionGroupService decisionGroupService;
  @Autowired
  private ActivationService activationService;
  @Autowired
  private DecisionTreeQueryRepository underTest;

  @Test
  void findAllShouldReturnActivatedTree() {
    String treeName = "tree1";
    String group1Value = uniqueDecisionGroupName();
    String group2Value = uniqueDecisionGroupName();
    long decisionTreeId = decisionTreeService.getOrCreate(treeName);
    long group1 = decisionGroupService.store(group1Value).orElseThrow();
    long group2 = decisionGroupService.store(group2Value).orElseThrow();
    activationService.activate(ActivationRequest.of(decisionTreeId, asList(group1, group2)));

    List<DecisionTreeQuery> queries = underTest.findAll().collect(Collectors.toList());

    assertThat(queries)
        .hasSize(1)
        .first()
        .satisfies(q -> assertThat(q.getDecisionTreeId()).isEqualTo(decisionTreeId))
        .satisfies(q -> assertThat(q.getDecisionTreeName()).isEqualTo(treeName))
        .satisfies(q -> assertThat(q.getDecisionGroupNames())
            .containsExactlyInAnyOrder(group1Value, group2Value));
  }

  @Test
  void findAllShouldReturnInactiveTree() {
    String treeName = "tree1";
    long decisionTreeId = decisionTreeService.getOrCreate(treeName);

    List<DecisionTreeQuery> queries = underTest.findAll().collect(Collectors.toList());

    assertThat(queries)
        .hasSize(1)
        .first()
        .satisfies(q -> assertThat(q.getDecisionTreeId()).isEqualTo(decisionTreeId))
        .satisfies(q -> assertThat(q.getDecisionTreeName()).isEqualTo(treeName))
        .satisfies(q -> assertThat(q.getDecisionGroupNames()).isEmpty());
  }

  @Test
  void findByDecisionTreeIdShouldReturnTreeWithCorrectId() {
    String treeName = "tree1";
    long decisionTreeId = decisionTreeService.getOrCreate(treeName);
    decisionTreeService.getOrCreate("tree2");

    assertThat(underTest.findByDecisionTreeId(decisionTreeId))
        .hasValueSatisfying(t -> assertThat(t.getDecisionTreeId()).isEqualTo(decisionTreeId))
        .hasValueSatisfying(t -> assertThat(t.getDecisionTreeName()).isEqualTo(treeName));
  }

  @Test
  void whenThereAreNoTrees_returnEmptyWithoutException() {
    assertThat(underTest.findAll()).isEmpty();
    assertThat(underTest.findByDecisionTreeId(2L)).isEmpty();
  }

  @Test
  void findByDecisionGroupNameShouldReturnValidTree() {
    String treeName = "tree";
    String groupName1 = uniqueDecisionGroupName();
    String groupName2 = uniqueDecisionGroupName();
    long decisionTreeId = decisionTreeService.getOrCreate(treeName);
    long group1 = decisionGroupService.store(groupName1).orElseThrow();
    long group2 = decisionGroupService.store(groupName2).orElseThrow();
    activationService.activate(ActivationRequest.of(decisionTreeId, asList(group1, group2)));

    assertThat(underTest.findByDecisionGroupName(groupName1))
        .isEqualTo(underTest.findByDecisionGroupName(groupName2))
        .hasValueSatisfying(q -> assertThat(q.getDecisionTreeId()).isEqualTo(decisionTreeId));
  }

  @Test
  void findByDecisionGroupNameShouldReturnEmptyIfTreeNotActivated() {
    String treeName = "tree";
    String groupName = uniqueDecisionGroupName();
    decisionTreeService.getOrCreate(treeName);
    decisionGroupService.store(groupName).orElseThrow();

    Optional<DecisionTreeQuery> query = underTest.findByDecisionGroupName(groupName);

    assertThat(query).isEmpty();
  }

  /**
   * Generates unique group names to bypass the DecisionGroupCache
   *
   * @return unique decision group name
   */
  private static String uniqueDecisionGroupName() {
    return UUID.randomUUID().toString();
  }

  @EntityScan
  @EnableJpaRepositories
  @Configuration
  @ComponentScan(basePackageClasses = {
      DecisionTreeModule.class,
      ActivationModule.class,
      DecisionGroupModule.class
  })
  public static class DecisionTreeQueryRepositoryTestConfiguration {

  }
}
