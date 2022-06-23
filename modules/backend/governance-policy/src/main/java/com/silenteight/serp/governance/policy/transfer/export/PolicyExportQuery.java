package com.silenteight.serp.governance.policy.transfer.export;

import lombok.NonNull;

import com.silenteight.serp.governance.policy.domain.dto.TransferredPolicyRootDto;

import java.util.UUID;

public interface PolicyExportQuery {

  TransferredPolicyRootDto getTransferablePolicy(@NonNull UUID policyId);
}
