package com.silenteight.serp.governance.policy.importing;

import com.silenteight.serp.governance.policy.domain.PolicyService;
import com.silenteight.serp.governance.policy.domain.dto.ImportPolicyRequest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static com.silenteight.proto.governance.v1.api.FeatureVectorSolution.SOLUTION_FALSE_POSITIVE;
import static com.silenteight.serp.governance.policy.domain.StepType.BUSINESS_LOGIC;
import static java.util.Collections.singletonList;
import static java.util.UUID.fromString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImportPolicyUseCaseTest {

  @Mock
  private ImportedPolicyRootParser importedPolicyRootParser;

  @Mock
  private PolicyService policyService;

  @InjectMocks
  private ImportPolicyUseCase underTest;

  @Test
  void createPolicy() {
    // given
    InputStream inputStream = mock(InputStream.class);
    ImportedPolicyRoot root = createImportedPolicyRoot();
    ImportPolicyCommand command = ImportPolicyCommand.builder()
        .inputStream(inputStream)
        .build();

    when(importedPolicyRootParser.parse(any(InputStream.class))).thenReturn(root);

    // when
    underTest.apply(command);

    // then
    verify(policyService).doImport(any(ImportPolicyRequest.class));
  }

  private static ImportedPolicyRoot createImportedPolicyRoot() {
    Map<String, List<String>> features = Map.of(
        "nameAgent", List.of("MATCH", "NEAR_MATCH"),
        "dateAgent", List.of("EXACT", "NEAR"));

    ImportedFeatureLogic importedFeatureLogic = new ImportedFeatureLogic();
    importedFeatureLogic.setCount(2);
    importedFeatureLogic.setFeatures(features);

    ImportedStep importedStep = new ImportedStep();
    importedStep.setSolution(SOLUTION_FALSE_POSITIVE);
    importedStep.setStepId(fromString("de1afe98-0b58-4941-9791-4e081f9b8139"));
    importedStep.setName("step-1");
    importedStep.setDescription("This is step description");
    importedStep.setType(BUSINESS_LOGIC);
    importedStep.setFeatureLogics(singletonList(importedFeatureLogic));

    ImportedPolicy importedPolicy = new ImportedPolicy();
    importedPolicy.setPolicyId(fromString("01256804-1ce1-4d52-94d4-d1876910f272"));
    importedPolicy.setName("policy-name");
    importedPolicy.setSteps(singletonList(importedStep));

    ImportedPolicyRoot root = new ImportedPolicyRoot();
    root.setPolicy(importedPolicy);

    return root;
  }
}
