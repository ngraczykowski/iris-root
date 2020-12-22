package com.silenteight.serp.governance.activation;

import com.silenteight.serp.governance.activation.dto.ActivationRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.*;

class ActivationServiceTest {

  private static final long DECISION_TREE_ID_1 = 321L;
  private static final long DECISION_TREE_ID_2 = 123L;
  private static final long DECISION_GROUP_ID_1 = 987L;
  private static final long DECISION_GROUP_ID_2 = 789L;
  private ActivationService activationService;
  private TestInMemoryActivationRepository activationRepository;

  @BeforeEach
  void setUp() {
    activationRepository = new TestInMemoryActivationRepository();
    ActivationConfiguration configuration = new ActivationConfiguration(activationRepository);
    activationService = configuration.activationService();
  }

  @Test
  void activateOneTreeForDecisionGroup_recordWithDecisionTreeAndDecisionGroup() {
    activationService.activate(ActivationRequest.of(DECISION_TREE_ID_1, DECISION_GROUP_ID_1));

    assertThat(activationRepository.getAll())
        .extracting(Activation::getDecisionGroupId)
        .containsOnly(DECISION_GROUP_ID_1);
  }

  @Test
  void deactivateDecisionGroupFromTree_removedRecordWithActivation() {
    activationService.activate(ActivationRequest.of(DECISION_TREE_ID_1, DECISION_GROUP_ID_1));

    activationService.deactivate(DECISION_TREE_ID_1, DECISION_GROUP_ID_1);

    assertThat(activationRepository.getAll()).isEmpty();
  }

  @Test
  void bulkActivationForDecisionTree_allActivationsAreSaved() {
    activationService.activate(
        ActivationRequest.of(DECISION_TREE_ID_1, asList(DECISION_GROUP_ID_1, DECISION_GROUP_ID_2)));

    assertThat(activationRepository.getAll())
        .extracting(Activation::getDecisionGroupId)
        .containsOnly(DECISION_GROUP_ID_1, DECISION_GROUP_ID_2);
  }

  @Test
  void activatesAlreadyActivatedGroupInAnotherTree_activationIsReplaced() {
    activationService.activate(ActivationRequest.of(DECISION_TREE_ID_1, DECISION_GROUP_ID_1));
    activationService.activate(ActivationRequest.of(DECISION_TREE_ID_2, DECISION_GROUP_ID_1));

    assertThat(activationRepository.getAll())
        .extracting(Activation::getDecisionTreeId)
        .containsOnly(DECISION_TREE_ID_2);
  }

  private static class TestInMemoryActivationRepository extends InMemoryActivationRepository {

    Collection<Activation> getAll() {
      return getInternalStore().values();
    }
  }
}
