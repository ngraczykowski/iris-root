package com.silenteight.serp.governance.policy.transfer.importing;

import com.silenteight.serp.governance.policy.domain.PolicyService;
import com.silenteight.serp.governance.policy.domain.dto.ConfigurePolicyRequest;
import com.silenteight.serp.governance.policy.transfer.dto.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.InputStream;
import java.util.List;

import static com.silenteight.serp.governance.policy.domain.Condition.IS;
import static com.silenteight.serp.governance.policy.domain.StepType.BUSINESS_LOGIC;
import static com.silenteight.solving.api.v1.FeatureVectorSolution.SOLUTION_FALSE_POSITIVE;
import static java.util.Collections.singletonList;
import static java.util.List.of;
import static java.util.UUID.fromString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImportPolicyUseCaseTest {

  @Mock
  private TransferredPolicyRootDtoParser transferredPolicyRootParser;

  @Mock
  private PolicyService policyService;

  @InjectMocks
  private ImportPolicyUseCase underTest;

  @Test
  void createPolicy() {
    // given
    InputStream inputStream = mock(InputStream.class);
    String importedBy = "asmith";
    TransferredPolicyRootDto root = createTransferredPolicyRoot();
    ImportPolicyCommand command = ImportPolicyCommand.builder()
        .inputStream(inputStream)
        .importedBy(importedBy)
        .build();

    when(transferredPolicyRootParser.parse(any(InputStream.class))).thenReturn(root);

    // when
    underTest.apply(command);

    // then
    verify(policyService).doImport(any(ConfigurePolicyRequest.class));
  }

  private static TransferredPolicyRootDto createTransferredPolicyRoot() {
    List<TransferredMatchConditionDto> transferredMatchConditions = of(
        new TransferredMatchConditionDto("nameAgent", IS, of("MATCH", "NEAR_MATCH")),
        new TransferredMatchConditionDto("dateAgent", IS, of("EXACT", "NEAR")));

    TransferredFeatureLogicDto transferredFeatureLogics = new TransferredFeatureLogicDto();
    transferredFeatureLogics.setToFulfill(2);
    transferredFeatureLogics.setMatchConditions(transferredMatchConditions);

    TransferredStepDto transferredStep = new TransferredStepDto();
    transferredStep.setSolution(SOLUTION_FALSE_POSITIVE);
    transferredStep.setStepId(fromString("de1afe98-0b58-4941-9791-4e081f9b8139"));
    transferredStep.setName("step-1");
    transferredStep.setDescription("This is step description");
    transferredStep.setType(BUSINESS_LOGIC);
    transferredStep.setFeatureLogics(singletonList(transferredFeatureLogics));

    TransferredPolicyDto transferredPolicy = new TransferredPolicyDto();
    transferredPolicy.setPolicyId(fromString("01256804-1ce1-4d52-94d4-d1876910f272"));
    transferredPolicy.setName("policy-name");
    transferredPolicy.setSteps(singletonList(transferredStep));

    TransferredPolicyRootDto root = new TransferredPolicyRootDto();
    root.setPolicy(transferredPolicy);

    return root;
  }
}
