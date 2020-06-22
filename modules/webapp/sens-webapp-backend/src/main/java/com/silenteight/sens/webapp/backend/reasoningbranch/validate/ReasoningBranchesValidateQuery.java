package com.silenteight.sens.webapp.backend.reasoningbranch.validate;

import com.silenteight.sens.webapp.backend.reasoningbranch.validate.dto.BranchIdAndSignatureDto;

import java.util.List;

public interface ReasoningBranchesValidateQuery {

  List<BranchIdAndSignatureDto> findIdsByTreeIdAndBranchIds(long treeId, List<Long> branchIds);

  List<BranchIdAndSignatureDto> findIdsByTreeIdAndFeatureVectorSignatures(
      long treeId, List<String> featureVectorSignatures);
}
