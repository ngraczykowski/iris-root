package com.silenteight.sens.webapp.backend.reasoningbranch.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import javax.annotation.Nullable;
import javax.validation.constraints.AssertTrue;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BranchIdsAndSignaturesDto {

  @Nullable
  private List<Long> branchIds;

  @Nullable
  private List<String> featureVectorSignatures;

  @AssertTrue(message = "must be provided")
  private boolean isBranchIdsOrFeatureVectorSignatures() {
    return isNotEmpty(branchIds) && isEmpty(featureVectorSignatures) ||
        isNotEmpty(featureVectorSignatures) && isEmpty(branchIds);
  }
}
