package com.silenteight.serp.governance.policy.solve;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.governance.api.v1.*;
import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.serp.governance.common.signature.CanonicalFeatureVector;
import com.silenteight.serp.governance.common.signature.CanonicalFeatureVectorFactory;
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
        .map(featureVector -> process(featureCollection, featureVector))
        .map(this::asSolutionResponse)
        .collect(toList());

    return SolveFeaturesResponse.newBuilder()
        .addAllSolutions(solutionResponses)
        .build();
  }

  private SolveResponse process(FeatureCollection featureCollection, FeatureVector featureVector) {
    List<String> featureNames = asFeatureNames(featureCollection.getFeatureList());
    List<String> featureValues = new ArrayList<>(featureVector.getFeatureValueList());
    Map<String, String> featureValuesByName = asFeatureValues(featureNames, featureValues);
    SolveResponse solution = solvingService.solve(stepsConfigurationProvider, featureValuesByName);

    // TODO(anowicki): WEB-500: hide this behind service or use canonical form in solvingService
    CanonicalFeatureVector canonicalFeatureVector =
        canonicalFeatureVectorFactory.fromNamesAndValues(featureNames, featureValues);

    FeatureVectorSolvedEvent event = FeatureVectorSolvedEvent.newBuilder()
        .setId(fromJavaUuid(randomUUID()))
        .setCorrelationId(fromJavaUuid(randomUUID()))
        .setCreatedAt(toTimestamp(timeSource.now()))
        .setFeatureCollection(featureCollection)
        .setFeatureVector(featureVector)
        .setFeatureVectorSignature(canonicalFeatureVector.getVectorSignature().getValue())
        .setFeatureVectorSolution(solution.getSolution())
        .build();
    featureVectorSolvedMessageGateway.send(event);

    return solution;
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

  private static List<String> asFeatureNames(
      List<com.silenteight.governance.api.v1.Feature> features) {

    return features
        .stream()
        .map(com.silenteight.governance.api.v1.Feature::getName)
        .collect(toList());
  }

  private SolutionResponse asSolutionResponse(SolveResponse solveResponse) {
    SolutionResponse.Builder builder = SolutionResponse.newBuilder()
        .setFeatureVectorSolution(solveResponse.getSolution());

    if (solveResponse.getStepId() != null)
      builder.setStepId(fromJavaUuid(solveResponse.getStepId()));
    // TODO(anowicki): WEB-403: .setFeatureVectorSignature()
    return builder.build();
  }
}
