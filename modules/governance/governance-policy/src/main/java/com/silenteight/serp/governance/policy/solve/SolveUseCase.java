package com.silenteight.serp.governance.policy.solve;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.serp.governance.common.signature.CanonicalFeatureVector;
import com.silenteight.serp.governance.common.signature.CanonicalFeatureVectorFactory;
import com.silenteight.serp.governance.common.signature.Signature;
import com.silenteight.serp.governance.policy.solve.amqp.FeatureVectorSolvedMessageGateway;
import com.silenteight.serp.governance.policy.solve.dto.SolveResponse;
import com.silenteight.solving.api.v1.*;
import com.silenteight.solving.api.v1.FeatureVectorSolvedEvent.Builder;

import com.google.protobuf.Struct;
import com.google.protobuf.Value;

import java.util.*;

import static com.silenteight.serp.governance.policy.common.PolicyResource.fromResourceName;
import static com.silenteight.serp.governance.policy.common.PolicyResource.toResourceName;
import static com.silenteight.solving.api.utils.Timestamps.toTimestamp;
import static com.silenteight.solving.api.utils.Uuids.fromJavaUuid;
import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.IntStream.range;

@RequiredArgsConstructor
@Slf4j
public class SolveUseCase {

  private static final String SOLVED_MASSAGE =
      "Solved feature (featureNames = '{}', featureValues = '{}', signature = '{}') "
          + "as '{}' with step '{}'.";
  private static final String SOLVED_NO_STEP_MASSAGE =
      "Solved feature (featureNames = '{}', featureValues = '{}', signature = '{}') "
          + "as '{}' - no corresponding step in the policy.";
  private static final String FEATURE_VECTOR_SIGNATURE_REASON_FIELD = "feature_vector_signature";
  private static final String POLICY_REASON_FIELD = "policy";
  private static final String POLICY_TITLE_REASON_FIELD = "policy_title";
  private static final String STEP_REASON_FIELD = "step";
  private static final String STEP_TITLE_REASON_FIELD = "step_title";

  @NonNull
  private final StepsSupplierProvider stepsSupplierProvider;
  @NonNull
  private final SolvingService solvingService;
  @NonNull
  private final FeatureVectorSolvedMessageGateway featureVectorSolvedMessageGateway;
  @NonNull
  private final CanonicalFeatureVectorFactory canonicalFeatureVectorFactory;
  @NonNull
  private final PolicyTitleQuery policyTitleQuery;
  @NonNull
  private final TimeSource timeSource;

  public BatchSolveFeaturesResponse solve(BatchSolveFeaturesRequest request) {
    log.info("Solving {} features using policy {}.",
             request.getFeatureVectorsCount(), request.getPolicyName());
    UUID policyId = fromResourceName(request.getPolicyName());
    String policyName = policyTitleQuery.getTitle(policyId);
    StepsSupplier stepsSupplier = stepsSupplierProvider.getStepsSupplier(policyId);
    FeatureCollection featureCollection = request.getFeatureCollection();
    List<SolutionResponse> solutionResponses = request
        .getFeatureVectorsList()
        .stream()
        .map(featureVector -> solveSingle(
            policyId, policyName, stepsSupplier, featureCollection, featureVector))
        .collect(toList());

    log.info("Solved {} features using policy {}.",
             request.getFeatureVectorsCount(), request.getPolicyName());

    return BatchSolveFeaturesResponse.newBuilder()
        .addAllSolutions(solutionResponses)
        .build();
  }

  private SolutionResponse solveSingle(
      UUID policyId,
      String policyName,
      StepsSupplier stepsSupplier,
      FeatureCollection featureCollection,
      FeatureVector featureVector) {

    List<String> featureNames = asFeatureNames(featureCollection.getFeatureList());
    List<String> featureValues = new ArrayList<>(featureVector.getFeatureValueList());

    log.debug("Solving feature (featureNames = '{}', featureValues = '{}').",
              featureNames, featureValues);

    CanonicalFeatureVector canonicalFeatureVector =
        canonicalFeatureVectorFactory.fromNamesAndValues(featureNames, featureValues);

    Map<String, String> featureValuesByName = asFeatureValues(featureNames, featureValues);
    SolveResponse response = solvingService.solve(stepsSupplier, featureValuesByName);

    emitEvent(featureCollection, featureVector,
        canonicalFeatureVector.getVectorSignature(), response);

    logSolved(featureNames, featureValues, canonicalFeatureVector.getVectorSignature(), response);
    return asSolutionResponse(
        policyId, policyName, canonicalFeatureVector.getVectorSignature(), response);
  }

  private void logSolved(
      List<String> featureNames,
      List<String> featureValues,
      Signature signature,
      SolveResponse response) {

    if (response.getStepId() == null)
      log.info(
          SOLVED_NO_STEP_MASSAGE, featureNames, featureValues, signature, response.getSolution());
    else
      log.info(
          SOLVED_MASSAGE,
          featureNames,
          featureValues,
          signature,
          response.getSolution(),
          response.getStepId());
  }

  private static List<String> asFeatureNames(List<Feature> features) {
    return features
        .stream()
        .map(Feature::getName)
        .collect(toList());
  }

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

    FeatureVectorSolvedEvent event = eventBuilder.build();
    log.debug("Sending FV event: {}", event);

    featureVectorSolvedMessageGateway.send(event);
  }

  private static SolutionResponse asSolutionResponse(
      UUID policyId, String policyName, Signature signature, SolveResponse solveResponse) {

    SolutionResponse.Builder responseBuilder = SolutionResponse.newBuilder()
        .setFeatureVectorSolution(solveResponse.getSolution())
        .setFeatureVectorSignature(signature.getValue());

    Map<String, Value> reasonFields = new HashMap<>();
    reasonFields.put(
        FEATURE_VECTOR_SIGNATURE_REASON_FIELD, asStringValue(signature.asString()));
    reasonFields.put(POLICY_REASON_FIELD, asStringValue(toResourceName(policyId)));
    reasonFields.put(POLICY_TITLE_REASON_FIELD, asStringValue(policyName));

    if (solveResponse.getStepId() != null)
      responseBuilder.setStepId(fromJavaUuid(solveResponse.getStepId()));

    reasonFields.put(STEP_REASON_FIELD, asStringValue(solveResponse.getStepName()));
    reasonFields.put(STEP_TITLE_REASON_FIELD, asStringValue(solveResponse.getStepTitle()));

    responseBuilder.setReason(asReason(reasonFields));

    return responseBuilder.build();
  }

  private static Value asStringValue(String value) {
    if (value == null)
      return Value.getDefaultInstance();

    return Value.newBuilder()
        .setStringValue(value)
        .build();
  }

  private static Struct asReason(Map<String, Value> fields) {
    return Struct.newBuilder()
        .putAllFields(fields)
        .build();
  }
}
