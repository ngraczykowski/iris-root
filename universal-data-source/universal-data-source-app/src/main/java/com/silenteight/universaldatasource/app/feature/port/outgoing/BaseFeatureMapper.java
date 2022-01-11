package com.silenteight.universaldatasource.app.feature.port.outgoing;


import lombok.extern.slf4j.Slf4j;

import com.silenteight.universaldatasource.app.feature.model.MatchFeatureOutput;
import com.silenteight.universaldatasource.app.feature.model.MatchFeatureOutput.MatchInput;
import com.silenteight.universaldatasource.app.feature.port.incoming.BatchFeatureInputResponse;
import com.silenteight.universaldatasource.common.protobuf.JsonToStructConverter;

import com.google.protobuf.Message;
import com.google.protobuf.Message.Builder;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public abstract class BaseFeatureMapper<T extends Message> implements FeatureMapper {

  private final String className;

  protected BaseFeatureMapper(Class<T> featureInputType) {
    this.className = featureInputType.getCanonicalName();
  }

  @Override
  public String getType() {
    return className;
  }

  /**
   * Maps the following protobuf messages structure:
   * <code><pre>
   *   message BatchGetMatch&lt;AGENT>InputsResponse {
   *     repeated &lt;AGENT>Input &lt;AGENT>_inputs = 1;
   *   }
   *
   *   message &lt;AGENT>Input {
   *     string match = 1;
   *     repeated &lt;AGENT>FeatureInput &lt;AGENT>_feature_inputs = 2;
   *   }
   *
   *   message &lt;AGENT>FeatureInput {
   *     string feature = 1;
   *
   *     // THE REST OF FEATURE INPUT FIELDS...
   *   }
   * </pre></code>
   */
  @Override
  public BatchFeatureInputResponse map(MatchFeatureOutput matchFeatureOutput) {
    // message BatchGetMatch<AGENT>InputsResponse
    var batchResponseBuilder = createBatchResponseBuilder();
    // repeated <AGENT>Input <AGENT>_inputs = 1
    var repeatedMatchInputField = batchResponseBuilder.getDescriptorForType().findFieldByNumber(1);
    // message <AGENT>Input
    var matchInputBuilder = batchResponseBuilder.newBuilderForField(repeatedMatchInputField);
    // string match = 1
    var matchNameField = matchInputBuilder.getDescriptorForType().findFieldByNumber(1);
    // repeated <AGENT>FeatureInput <AGENT>_feature_inputs = 2
    var maybeRepeatedFeatureInputsField =
        matchInputBuilder.getDescriptorForType().findFieldByNumber(2);
    // message <AGENT>FeatureInput
    var featureInputBuilder = matchInputBuilder.newBuilderForField(maybeRepeatedFeatureInputsField);
    // string feature = 1
    var featureNameField = featureInputBuilder.getDescriptorForType().findFieldByNumber(1);

    for (var matchInput : matchFeatureOutput.getMatchInputs()) {
      // Set match name, i.e., `<AGENT>Input.match`.
      matchInputBuilder.setField(matchNameField, matchInput.getMatch());

      var missingFeatures = new HashSet<>(matchInput.getRequestedFeatures());
      for (var agentInput : matchInput.getAgentInputs()) {
        missingFeatures.remove(agentInput.getFeature());
        // Parse JSON from agentInput into <AGENT>FeatureInput message builder.
        JsonToStructConverter.map(featureInputBuilder, agentInput.getAgentInputJson());
        // Add agent input, i.e., `<AGENT>Input.<AGENT>_inputs`.
        if (maybeRepeatedFeatureInputsField.isRepeated()) {
          matchInputBuilder.addRepeatedField(
              maybeRepeatedFeatureInputsField, featureInputBuilder.build());
        } else {
          matchInputBuilder.setField(maybeRepeatedFeatureInputsField, featureInputBuilder.build());
        }
        // TODO(ahaczewski): Map the original feature name to mapped one (move from
        //  FeatureExtractor.editAgentInputFeatureName).
        // Clear builder, preparing for the next feature.
        featureInputBuilder.clear();
      }

      if (log.isTraceEnabled()) {
        log.trace(
            "Match: {} is missing some of the feature inputs {} in UDS - default features inputs.",
            matchInput.getMatch(), missingFeatures);
      }
      for (var missingFeature : missingFeatures) {
        // Set feature name, i.e., `<AGENT>FeatureInput.feature`.
        featureInputBuilder.setField(featureNameField, missingFeature);
        // Add agent input, i.e., `<AGENT>Input.<AGENT>_inputs`.
        if (maybeRepeatedFeatureInputsField.isRepeated()) {
          matchInputBuilder.addRepeatedField(
              maybeRepeatedFeatureInputsField, featureInputBuilder.build());
        } else {
          matchInputBuilder.setField(maybeRepeatedFeatureInputsField, featureInputBuilder.build());
        }
        // TODO(ahaczewski): Map the original feature name to mapped one (move from
        //  FeatureExtractor.editAgentInputFeatureName).
        // Clear builder, preparing for the next feature.
        featureInputBuilder.clear();
      }

      // Add match input, i.e., `BatchGetMatch<AGENT>InputsResponse.<AGENT>_inputs`.
      batchResponseBuilder.addRepeatedField(repeatedMatchInputField, matchInputBuilder.build());
      // Clear builder, preparing for the next match.
      matchInputBuilder.clear();
    }

    return new BatchFeatureInputResponse(batchResponseBuilder.build());
  }

  @Override
  public BatchFeatureInputResponse createEmptyResponse(
      Collection<String> matches, List<String> features) {

    var emptyMatchInputs = matches
        .stream()
        .map(m -> MatchInput
            .builder()
            .match(m)
            .requestedFeatures(features)
            .agentInputs(List.of())
            .build())
        .collect(Collectors.toList());

    return map(new MatchFeatureOutput(getType(), emptyMatchInputs));
  }

  protected abstract Builder createBatchResponseBuilder();

  protected abstract Builder createInputBuilder();

  protected abstract Builder getDefaultFeatureInput();
}
