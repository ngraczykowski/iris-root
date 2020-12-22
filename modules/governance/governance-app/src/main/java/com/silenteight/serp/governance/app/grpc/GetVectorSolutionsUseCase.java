package com.silenteight.serp.governance.app.grpc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.proto.serp.v1.api.GetVectorSolutionsRequest;
import com.silenteight.proto.serp.v1.api.VectorSolutionsResponse;
import com.silenteight.proto.serp.v1.governance.VectorSolution;
import com.silenteight.serp.governance.branchquery.VectorSolutionFinder;
import com.silenteight.serp.governance.decisiongroup.DecisionGroupService;

import com.google.protobuf.ByteString;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.List;
import javax.validation.ValidationException;

import static com.silenteight.sep.base.common.logging.LogContextUtils.logCollection;
import static com.silenteight.sep.base.common.logging.LogContextUtils.logObject;

@Slf4j
@RequiredArgsConstructor
class GetVectorSolutionsUseCase {

  private final VectorSolutionFinder solutionFinder;
  private final DecisionGroupService decisionGroupService;

  VectorSolutionsResponse activate(GetVectorSolutionsRequest request) {
    String decisionGroup = request.getDecisionGroup();
    logCollection("request.vectorSignaturesList", request.getVectorSignaturesList());
    logObject("request.decisionGroup", decisionGroup);
    if (log.isDebugEnabled())
      log.debug("Getting solution for decision group: decisionGroup={}", decisionGroup);
    // FIXME(ahaczewski): Use more appropriate exception.
    if (StringUtils.isBlank(decisionGroup))
      throw new ValidationException("decision_group is required");

    List<ByteString> vectorSignatures = request.getVectorSignaturesList();

    decisionGroupService.store(decisionGroup);

    Collection<VectorSolution> solutions = solutionFinder
        .findSolutions(decisionGroup, vectorSignatures);

    if (log.isDebugEnabled())
      log.debug("Found solutions: solutions={}", logSolutionsString(solutions));
    VectorSolutionsResponse.Builder vectorSolutions = VectorSolutionsResponse
        .newBuilder()
        .addAllSolutions(solutions);

    return vectorSolutions.build();
  }

  private static String logSolutionsString(Collection<VectorSolution> solutions) {
    StringBuilder builder = new StringBuilder();
    for (VectorSolution vectorSolution : solutions) {
      builder.append(String.format(
          "[DT:%d,FV:%d,solution:%s]",
          vectorSolution.getReasoningBranch().getDecisionTreeId(),
          vectorSolution.getReasoningBranch().getFeatureVectorId(),
          vectorSolution.getSolution()));
    }
    return builder.toString();
  }
}
