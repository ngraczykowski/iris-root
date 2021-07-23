package com.silenteight.serp.governance.model.used;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.model.common.ModelResource;
import com.silenteight.serp.governance.model.domain.dto.ModelDto;
import com.silenteight.serp.governance.model.get.ModelDetailsQuery;
import com.silenteight.serp.governance.policy.common.PolicyResource;
import com.silenteight.serp.governance.policy.details.PolicyDetailsQuery;
import com.silenteight.serp.governance.policy.domain.PolicyService;
import com.silenteight.serp.governance.policy.domain.dto.PolicyDto;

import java.util.UUID;

import static com.silenteight.serp.governance.policy.domain.dto.MarkPolicyAsUsedRequest.of;

@RequiredArgsConstructor
public class MarkModelAsUsedOnProductionUseCase {

  @NonNull
  private final ModelDetailsQuery modelDetailsQuery;
  @NonNull
  private final PolicyDetailsQuery policyDetailsQuery;
  @NonNull
  private final PolicyService policyService;

  public void applyByName(@NonNull String model) {
    apply(ModelResource.fromResourceName(model));
  }

  public void applyByVersion(String version) {
    apply(modelDetailsQuery.getModelIdByVersion(version));
  }

  private void apply(UUID id) {
    ModelDto savedModel = modelDetailsQuery.get(id);
    PolicyDto policyDetails = policyDetailsQuery.details(
        PolicyResource.fromResourceName(savedModel.getPolicy()));
    policyService.markPolicyAsUsed(of(policyDetails.getId(), policyDetails.getUpdatedBy()));
  }
}
