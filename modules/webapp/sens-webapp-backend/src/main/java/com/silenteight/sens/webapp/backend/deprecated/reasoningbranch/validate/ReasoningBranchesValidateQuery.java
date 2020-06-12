package com.silenteight.sens.webapp.backend.deprecated.reasoningbranch.validate;

import java.util.List;

public interface ReasoningBranchesValidateQuery {

  List<BranchIdAndSignatureDto> findIdsByTreeIdAndBranchIds(long treeId, List<Long> branchIds);

  List<BranchIdAndSignatureDto> findIdsByTreeIdAndFeatureVectorSignatures(
      long treeId, List<String> featureVectorSignatures);
}
