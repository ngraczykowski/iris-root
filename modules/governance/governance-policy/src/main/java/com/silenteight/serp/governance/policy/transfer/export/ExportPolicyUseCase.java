package com.silenteight.serp.governance.policy.transfer.export;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.policy.domain.dto.TransferredPolicyRootDto;

import java.util.UUID;

@RequiredArgsConstructor
public class ExportPolicyUseCase {

  @NonNull
  private final PolicyExportQuery policyExportQuery;

  public TransferredPolicyRootDto apply(@NonNull UUID policyId) {
    return policyExportQuery.getTransferablePolicy(policyId);
  }
}
