package com.silenteight.serp.governance.model.transfer.export;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.changerequest.approval.ModelApprovalQuery;
import com.silenteight.serp.governance.changerequest.approval.dto.ModelApprovalDto;
import com.silenteight.serp.governance.model.common.ModelResource;
import com.silenteight.serp.governance.model.domain.dto.ModelDto;
import com.silenteight.serp.governance.model.get.ModelDetailsQuery;
import com.silenteight.serp.governance.model.transfer.dto.TransferredModelDto;
import com.silenteight.serp.governance.model.transfer.dto.TransferredModelMetadataDto;
import com.silenteight.serp.governance.model.transfer.dto.TransferredModelRootDto;
import com.silenteight.serp.governance.policy.common.PolicyResource;
import com.silenteight.serp.governance.policy.domain.dto.TransferredPolicyRootDto;
import com.silenteight.serp.governance.policy.transfer.export.ExportPolicyUseCase;

import java.util.UUID;

import static com.silenteight.serp.governance.model.transfer.TransferredModelChecksumCalculator.calculate;

@RequiredArgsConstructor
public class ExportModelUseCase {

  @NonNull
  private final ModelDetailsQuery modelDetailsQuery;
  @NonNull
  private final ModelApprovalQuery modelApprovalQuery;
  @NonNull
  private final ExportPolicyUseCase exportPolicyUseCase;

  public TransferredModelRootDto apply(@NonNull UUID modelId) {
    ModelDto model = modelDetailsQuery.get(modelId);
    return toTransferredRoot(model);
  }

  private TransferredModelRootDto toTransferredRoot(ModelDto model) {
    TransferredModelDto transferredModel = toTransferredModel(model);

    TransferredModelRootDto root = new TransferredModelRootDto();
    root.setChecksum(calculate(transferredModel));
    root.setModel(transferredModel);
    return root;
  }

  private TransferredModelDto toTransferredModel(ModelDto model) {
    TransferredModelDto transferredModel = new TransferredModelDto();
    transferredModel.setMetadata(toTransferredMetadata(model));
    transferredModel.setPolicy(toTransferredPolicyRoot(model.getPolicy()));
    return transferredModel;
  }

  private TransferredModelMetadataDto toTransferredMetadata(ModelDto model) {
    ModelApprovalDto modelApproval = modelApprovalQuery.getApproval(model.getName());

    TransferredModelMetadataDto metadata = new TransferredModelMetadataDto();
    metadata.setModelId(ModelResource.fromResourceName(model.getName()));
    metadata.setApprovedAt(modelApproval.getApprovedAt().toInstant());
    metadata.setApprovedBy(modelApproval.getApprovedBy());
    return metadata;
  }

  private TransferredPolicyRootDto toTransferredPolicyRoot(String policy) {
    return exportPolicyUseCase.apply(PolicyResource.fromResourceName(policy));
  }
}
