package com.silenteight.serp.governance.model.transfer.export;

import com.silenteight.serp.governance.changerequest.approval.ModelApprovalQuery;
import com.silenteight.serp.governance.model.get.ModelDetailsQuery;
import com.silenteight.serp.governance.model.transfer.dto.TransferredModelDto;
import com.silenteight.serp.governance.model.transfer.dto.TransferredModelRootDto;
import com.silenteight.serp.governance.policy.domain.dto.TransferredPolicyDto;
import com.silenteight.serp.governance.policy.domain.dto.TransferredPolicyRootDto;
import com.silenteight.serp.governance.policy.transfer.export.ExportPolicyUseCase;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.serp.governance.model.transfer.export.ExportModelFixtures.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExportModelUseCaseTest {

  @Mock
  private ModelDetailsQuery modelDetailsQuery;

  @Mock
  private ModelApprovalQuery modelApprovalQuery;

  @Mock
  private ExportPolicyUseCase exportPolicyUseCase;

  @InjectMocks
  private ExportModelUseCase underTest;

  @Test
  void exportModel() {
    // given
    when(modelDetailsQuery.get(MODEL_ID)).thenReturn(MODEL);
    when(modelApprovalQuery.getApproval(MODEL_RESOURCE_NAME)).thenReturn(MODEL_APPROVAL);
    when(exportPolicyUseCase.apply(POLICY_ID)).thenReturn(transferredPolicyRoot());

    // when
    TransferredModelRootDto result = underTest.applyById(MODEL_ID);

    // then
    TransferredModelDto model = result.getModel();
    assertThat(model.getModelId()).isEqualTo(MODEL_ID);
    assertThat(model.getApprovedBy()).isEqualTo(APPROVED_BY);
    assertThat(model.getPolicyCreatedBy()).isEqualTo(POLICY_CREATED_BY);
    TransferredPolicyRootDto root = model.getPolicy();
    TransferredPolicyDto policy = root.getPolicy();
    assertThat(policy.getPolicyId()).isEqualTo(POLICY_ID);
    assertThat(policy.getName()).isEqualTo(POLICY_NAME);
    assertThat(policy.getDescription()).isEqualTo(POLICY_DESCRIPTION);
  }

  @Test
  void checksumShouldBeTheSameForSameModel() {
    when(modelDetailsQuery.get(MODEL_ID)).thenReturn(MODEL);
    when(modelApprovalQuery.getApproval(MODEL_RESOURCE_NAME)).thenReturn(MODEL_APPROVAL);
    when(exportPolicyUseCase.apply(POLICY_ID)).thenReturn(transferredPolicyRoot());
    TransferredModelRootDto firstResult = underTest.applyById(MODEL_ID);

    when(modelDetailsQuery.get(MODEL_ID)).thenReturn(MODEL);
    when(modelApprovalQuery.getApproval(MODEL_RESOURCE_NAME)).thenReturn(MODEL_APPROVAL);
    when(exportPolicyUseCase.apply(POLICY_ID)).thenReturn(transferredPolicyRoot());
    TransferredModelRootDto secondResult = underTest.applyById(MODEL_ID);

    assertThat(firstResult.getChecksum()).isEqualTo(secondResult.getChecksum());
  }
}
