package com.silenteight.serp.governance.policy.solve;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.governance.api.v1.*;
import com.silenteight.governance.api.v1.FeatureVectorSolvedEvent.Builder;
import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.serp.governance.common.signature.CanonicalFeatureVector;
import com.silenteight.serp.governance.common.signature.CanonicalFeatureVectorFactory;
import com.silenteight.serp.governance.common.signature.Signature;
import com.silenteight.serp.governance.policy.solve.amqp.FeatureVectorSolvedMessageGateway;
import com.silenteight.serp.governance.policy.solve.dto.SolveResponse;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.silenteight.governance.api.utils.Timestamps.toTimestamp;
import static com.silenteight.governance.api.utils.Uuids.fromJavaUuid;
import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.IntStream.range;

@RequiredArgsConstructor
public class SolveUseCase {

  @NonNull
  private final StepsConfigurationSupplier stepsConfigurationProvider;
  @NonNull
  private final SolvingService solvingService;
  @NonNull
  private final FeatureVectorSolvedMessageGateway featureVectorSolvedMessageGateway;
  @NonNull
  private final CanonicalFeatureVectorFactory canonicalFeatureVectorFactory;
  @NonNull
  private final TimeSource timeSource;

  public SolveFeaturesResponse solve(SolveFeaturesRequest request) {
    FeatureCollection featureCollection = request.getFeatureCollection();
    List<SolutionResponse> solutionResponses = request.getFeatureVectorsList().stream()
        .map(featureVector -> solveSingle(featureCollection, featureVector))
        .collect(toList());

    return SolveFeaturesResponse.newBuilder()
        .addAllSolutions(solutionResponses)
        .build();
  }

  private SolutionResponse solveSingle(
      FeatureCollection featureCollection, FeatureVector featureVector) {

    List<String> featureNames = asFeatureNames(featureCollection.getFeatureList());
    List<String> featureValues = new ArrayList<>(featureVector.getFeatureValueList());
    Map<String, String> featureValuesByName = asFeatureValues(featureNames, featureValues);
    SolveResponse response = solvingService.solve(stepsConfigurationProvider, featureValuesByName);

    // TODO(anowicki): WEB-500: hide this behind service or use canonical form in solvingService
    CanonicalFeatureVector canonicalFeatureVector =
        canonicalFeatureVectorFactory.fromNamesAndValues(featureNames, featureValues);

    emitEvent(featureCollection, featureVector,
        canonicalFeatureVector.getVectorSignature(), response);

    return asSolutionResponse(canonicalFeatureVector.getVectorSignature(), response);
  }

  private static List<String> asFeatureNames(
      List<com.silenteight.governance.api.v1.Feature> features) {

    return features
        .stream()
        .map(com.silenteight.governance.api.v1.Feature::getName)
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
      FeatureCollection featureCollection, FeatureVector featureVector, Signature signature,
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
