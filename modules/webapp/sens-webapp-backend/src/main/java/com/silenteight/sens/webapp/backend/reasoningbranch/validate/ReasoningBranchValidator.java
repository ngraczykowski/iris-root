package com.silenteight.sens.webapp.backend.reasoningbranch.validate;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.backend.reasoningbranch.BranchIdsNotFoundException;
import com.silenteight.sens.webapp.backend.reasoningbranch.FeatureVectorSignaturesNotFoundException;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.collections4.CollectionUtils.disjunction;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

@RequiredArgsConstructor
public class ReasoningBranchValidator {

  @NonNull
  private final ReasoningBranchesValidateQuery reasoningBranchesQuery;

  public Map<Long, String> validate(
      long treeId, List<Long> branchIds, List<String> featureVectorSignatures) {
    return isNotEmpty(branchIds) ?
       validateIds(treeId, branchIds) :
       validateSignatures(treeId, featureVectorSignatures);
  }

  public Map<Long, String> validateIds(long treeId, List<Long> branchIds) {
    List<BranchIdAndSignatureDto> existingBranchIdsAndSignatures =
        reasoningBranchesQuery.findIdsByTreeIdAndBranchIds(treeId, branchIds);

    validateNoDifferencesInIds(branchIds, toBranchIds(existingBranchIdsAndSignatures));
    return mapOf(existingBranchIdsAndSignatures);
  }

  private List<Long> toBranchIds(List<BranchIdAndSignatureDto> existingBranchIdsAndSignatures) {
    return existingBranchIdsAndSignatures
        .stream()
        .map(BranchIdAndSignatureDto::getReasoningBranchId)
        .collect(toList());
  }

  private void validateNoDifferencesInIds(
      List<Long> validatedBranchIds, List<Long> existingBranchIds) {
    Collection<Long> nonExistingBranchIds = disjunction(validatedBranchIds, existingBranchIds);
    if (!nonExistingBranchIds.isEmpty())
      throw new BranchIdsNotFoundException(nonExistingBranchIds);
  }

  public Map<Long, String> validateSignatures(long treeId, List<String> featureVectorSignatures) {
    List<BranchIdAndSignatureDto> existingBranches =
        reasoningBranchesQuery.findIdsByTreeIdAndFeatureVectorSignatures(
            treeId, featureVectorSignatures);

    validateNoDifferencesInSignatures(featureVectorSignatures, existingBranches);

    return mapOf(existingBranches);
  }

  private Map<Long, String> mapOf(List<BranchIdAndSignatureDto> existingBranches) {
    return existingBranches
        .stream()
        .collect(toMap(
            BranchIdAndSignatureDto::getReasoningBranchId,
            BranchIdAndSignatureDto::getFeatureVectorSignature));
  }

  private void validateNoDifferencesInSignatures(
      List<String> validatedFeatureVectorSignatures,
      List<BranchIdAndSignatureDto> existingBranches) {
    List<String> existingSignatures = existingBranches.stream()
        .map(BranchIdAndSignatureDto::getFeatureVectorSignature)
        .collect(toList());

    Collection<String> nonExistingSignatures =
        disjunction(validatedFeatureVectorSignatures, existingSignatures);

    if (!nonExistingSignatures.isEmpty())
      throw new FeatureVectorSignaturesNotFoundException(nonExistingSignatures);
  }
}
