package com.silenteight.serp.governance.policy.solve;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.serp.governance.common.signature.CanonicalFeatureVector;
import com.silenteight.serp.governance.common.signature.CanonicalFeatureVectorFactory;
import com.silenteight.serp.governance.common.signature.Signature;
import com.silenteight.serp.governance.policy.solve.amqp.FeatureVectorSolvedMessageGateway;
import com.silenteight.serp.governance.policy.solve.dto.SolveResponse;
import com.silenteight.solving.api.v1.*;
import com.silenteight.solving.api.v1.FeatureVectorSolvedEvent.Builder;

import java.util.*;

import static com.silenteight.solving.api.utils.Timestamps.toTimestamp;
import static com.silenteight.solving.api.utils.Uuids.fromJavaUuid;
import static java.util.UUID.fromString;
import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.IntStream.range;
import static org.apache.commons.lang3.StringUtils.substringAfter;

@RequiredArgsConstructor
public class SolveUseCase {

  private static final String POLICY_NAME_RESOURCE_PREFIX = "policies/";

  @NonNull
  private final StepsSupplierProvider stepsSupplierProvider;
  @NonNull
  private final SolvingService solvingService;
  @NonNull
  private final FeatureVectorSolvedMessageGateway featureVectorSolvedMessageGateway;
  @NonNull
  private final CanonicalFeatureVectorFactory canonicalFeatureVectorFactory;
  @NonNull
  private final TimeSource timeSource;

  public BatchSolveFeaturesResponse solve(BatchSolveFeaturesRequest request) {
    UUID policyId = retrievePolicyId(request.getPolicyName());
    StepsSupplier stepsSupplier = stepsSupplierProvider.getStepsSupplier(policyId);
    FeatureCollection featureCollection = request.getFeatureCollection();
    List<SolutionResponse> solutionResponses = request
        .getFeatureVectorsList()
        .stream()
        .map(featureVector -> solveSingle(stepsSupplier, featureCollection, featureVector))
        .collect(toList());

    return BatchSolveFeaturesResponse.newBuilder()
        .addAllSolutions(solutionResponses)
        .build();
  }

  private static UUID retrievePolicyId(String policyName) {
    return fromString(substringAfter(policyName, POLICY_NAME_RESOURCE_PREFIX));
  }

  private SolutionResponse solveSingle(
      StepsSupplier stepsSupplier,
      FeatureCollection featureCollection,
      FeatureVector featureVector) {

    List<String> featureNames = asFeatureNames(featureCollection.getFeatureList());
    List<String> featureValues = new ArrayList<>(featureVector.getFeatureValueList());
    Map<String, String> featureValuesByName = asFeatureValues(featureNames, featureValues);
    SolveResponse response = solvingService.solve(stepsSupplier, featureValuesByName);

    // TODO(anowicki): WEB-500: hide this behind service or use canonical form in solvingService
    CanonicalFeatureVector canonicalFeatureVector =
        canonicalFeatureVectorFactory.fromNamesAndValues(featureNames, featureValues);

    emitEvent(featureCollection, featureVector,
        canonicalFeatureVector.getVectorSignature(), response);

    return asSolutionResponse(canonicalFeatureVector.getVectorSignature(), response);
  }

  private static List<String> asFeatureNames(List<Feature> features) {
    return features
        .stream()
        .map(Feature::getName)
        .collect(toList());
  }

  // TODO(anowicki): WEB-500-Duplicate(CanonicalFeatureVectorFactory).
  private static Map<String, String> asFeatureValues(
      List<String> featureNames, List<String> featureValues) {

    Iterator<String> keyIterator = featureNames.iterator();
    Iterator<String> valueIterator = featureValues.iterator();

    return range(0, featureNames.size())
        .boxed()
        .collect(toMap(i -> keyIterator.next(), i -> valueIterator.next()));
  }

  private void emitEvent(
      FeatureCollection featureCollection,
      FeatureVector featureVector,
      Signature signature,
      SolveResponse solveResponse) {

    Builder eventBuilder = FeatureVectorSolvedEvent.newBuilder()
        .setId(fromJavaUuid(randomUUID()))
        .setCorrelationId(fromJavaUuid(randomUUID()))
        .setCreatedAt(toTimestamp(timeSource.now()))
        .setFeatureCollection(featureCollection)
        .setFeatureVector(featureVector)
        .setFeatureVectorSignature(signature.getValue())
        .setFeatureVectorSolution(solveResponse.getSolution());

    if (solveResponse.getStepId() != null)
      eventBuilder.setStepId(fromJavaUuid(solveResponse.getStepId()));

    featureVectorSolvedMessageGateway.send(eventBuilder.build());
  }

  private static SolutionResponse asSolutionResponse(
      Signature signature, SolveResponse solveResponse) {

    SolutionResponse.Builder responseBuilder = SolutionResponse.newBuilder()
        .setFeatureVectorSolution(solveResponse.getSolution())
        .setFeatureVectorSignature(signature.getValue());

    if (solveResponse.getStepId() != null)
      responseBuilder.setStepId(fromJavaUuid(solveResponse.getStepId()));

    return responseBuilder.build();
  }
}
