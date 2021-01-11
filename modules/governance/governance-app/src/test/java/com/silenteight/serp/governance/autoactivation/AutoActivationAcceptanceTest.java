package com.silenteight.serp.governance.autoactivation;

import com.silenteight.serp.governance.activation.ActivationService;
import com.silenteight.serp.governance.activation.dto.ActivationRequest;
import com.silenteight.serp.governance.decisiongroup.DecisionGroupCreated;
import com.silenteight.serp.governance.decisiontree.DecisionTreeFacade;
import com.silenteight.serp.governance.decisiontree.DecisionTreeId;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AutoActivationAcceptanceTest {

  private AutoActivationFacade autoActivationFacade;

  @Mock
  private DecisionTreeFacade decisionTreeFacade;

  @Mock
  private ActivationService activationService;

  @BeforeEach
  void setUp() {
    var activateDecisionGroupForDefaultDecisionTreeUseCase =
        new ActivateDecisionGroupForDefaultDecisionTreeUseCase(
            decisionTreeFacade,
            activationService);
    autoActivationFacade =
        new AutoActivationFacade(activateDecisionGroupForDefaultDecisionTreeUseCase);
  }

  @Test
  void whenStoring_decisionGroupIsAutomaticallyActivatedInSelectedDecisionTree() {
    // given
    long treeId = 2L;
    long decisionGroupId = 1L;
    when(decisionTreeFacade.findDefaultTreeForNewGroups())
        .thenReturn(Optional.of(new DecisionTreeId(treeId)));

    // when
    Result result = autoActivationFacade.activateDecisionGroupForDefaultDecisionTreeUseCase(
        new DecisionGroupCreated(decisionGroupId));

    // then
    assertThat(result).isEqualTo(Result.SUCCESS);
    verify(activationService).activate(ActivationRequest.of(treeId, decisionGroupId));
  }

  @Test
  void whenStoring_decisionGroupIsNotActivatedIfNoDecisionTreeIsSelected() {
    // given
    DecisionGroupCreated decisionGroupCreated = new DecisionGroupCreated(1L);
    when(decisionTreeFacade.findDefaultTreeForNewGroups()).thenReturn(Optional.empty());

    // when
    Result result = autoActivationFacade.activateDecisionGroupForDefaultDecisionTreeUseCase(
        decisionGroupCreated);

    // then
    assertThat(result).isEqualTo(Result.FAILURE);
    verifyNoInteractions(activationService);
  }

}
