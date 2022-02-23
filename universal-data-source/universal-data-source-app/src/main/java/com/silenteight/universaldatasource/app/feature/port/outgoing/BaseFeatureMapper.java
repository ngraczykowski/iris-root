package com.silenteight.universaldatasource.app.feature.port.outgoing;


import lombok.extern.slf4j.Slf4j;

import com.silenteight.universaldatasource.app.feature.mapper.FeatureInputMapper;
import com.silenteight.universaldatasource.app.feature.model.MatchFeatureOutput;
import com.silenteight.universaldatasource.app.feature.model.MatchFeatureOutput.AgentInput;
import com.silenteight.universaldatasource.app.feature.model.MatchFeatureOutput.MatchInput;
import com.silenteight.universaldatasource.app.feature.port.incoming.BatchFeatureInputResponse;
import com.silenteight.universaldatasource.common.protobuf.JsonToStructConverter;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.protobuf.Message;
import com.google.protobuf.Message.Builder;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public abstract class BaseFeatureMapper<T extends Message> implements FeatureMapper {

  private final String className;
  private final FeatureInputMapper featureInputMapper;

  protected BaseFeatureMapper(
      final Class<T> featureInputType,
      final FeatureInputMapper featureInputMapper) {
    this.className = featureInputType.getCanonicalName();
    this.featureInputMapper = featureInputMapper;
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
  public BatchFeatureInputResponse map(
      final MatchFeatureOutput matchFeatureOutput, final List<String> requestedFeatures) {
    // message BatchGetMatch<AGENT>InputsResponse
    var batchResponseBuilder = createBatchResponseBuilder();

    return new BatchFeatureInputResponseFactory(
        batchResponseBuilder, this.featureInputMapper).create(
        matchFeatureOutput, requestedFeatures);
  }

  static class BatchFeatureInputResponseFactory {

    public static final String AGENT_INPUT_FEATURE_KEY = "feature";

    private final Builder batchResponseBuilder;
    private final Builder matchInputBuilder;
    private final FeatureInputMapper featureInputMapper;

    public BatchFeatureInputResponseFactory(
        final Builder batchResponseBuilder,
        final FeatureInputMapper featureInputMapper
    ) {
      this.featureInputMapper = featureInputMapper;
      // repeated <AGENT>Input <AGENT>_inputs = 1
      var repeatedMatchInputField =
          batchResponseBuilder.getDescriptorForType().findFieldByNumber(1);
      // message <AGENT>Input

      this.matchInputBuilder = batchResponseBuilder.newBuilderForField(repeatedMatchInputField);
      this.batchResponseBuilder = batchResponseBuilder;
    }

    BatchFeatureInputResponse create(
        MatchFeatureOutput matchFeatureOutput, List<String> requestedFeatures) {
      var matchNameField = matchInputBuilder.getDescriptorForType().findFieldByNumber(1);
      for (var matchInput : matchFeatureOutput.getMatchInputs()) {
        // Set match name, i.e., `<AGENT>Input.match`.
        matchInputBuilder.setField(matchNameField, matchInput.getMatch());

        var missingFeatures = this.determineMissingFeature(matchInput, requestedFeatures);

        if (log.isTraceEnabled()) {
          log.trace(
              "Match: {} is missing some of the feature inputs {} in UDS - "
                  + "default features inputs.",
              matchInput.getMatch(), missingFeatures);
        }

        this.handleMissingFeatures(missingFeatures);

        var repeatedMatchInputField =
            batchResponseBuilder.getDescriptorForType().findFieldByNumber(1);

        // Add match input, i.e., `BatchGetMatch<AGENT>InputsResponse.<AGENT>_inputs`.
        batchResponseBuilder.addRepeatedField(repeatedMatchInputField, matchInputBuilder.build());
        // Clear builder, preparing for the next match.
        matchInputBuilder.clear();
      }

      return new BatchFeatureInputResponse(this.batchResponseBuilder.build());
    }

    private void handleMissingFeatures(Set<String> missingFeatures) {
      var maybeRepeatedFeatureInputsField =
          matchInputBuilder.getDescriptorForType().findFieldByNumber(2);
      var featureInputBuilder =
          matchInputBuilder.newBuilderForField(maybeRepeatedFeatureInputsField);
      var featureNameField = featureInputBuilder.getDescriptorForType().findFieldByNumber(1);
      for (var missingFeature : missingFeatures) {
        // Set feature name, i.e., `<AGENT>FeatureInput.feature`.
        featureInputBuilder.setField(featureNameField, missingFeature);
        // Add agent input, i.e., `<AGENT>Input.<AGENT>_inputs`.
        if (maybeRepeatedFeatureInputsField.isRepeated()) {
          matchInputBuilder.addRepeatedField(
              maybeRepeatedFeatureInputsField, featureInputBuilder.build());
        } else {
          matchInputBuilder.setField(
              maybeRepeatedFeatureInputsField, featureInputBuilder.build());
        }
        featureInputBuilder.clear();
      }
    }

    @NotNull
    private Set<String> determineMissingFeature(
        MatchInput matchInput, List<String> requestedFeatures) {
      var missingFeatures = new HashSet<>(requestedFeatures);
      var maybeRepeatedFeatureInputsField =
          matchInputBuilder.getDescriptorForType().findFieldByNumber(2);
      var featureInputBuilder =
          matchInputBuilder.newBuilderForField(maybeRepeatedFeatureInputsField);
      for (var agentInput : matchInput.getAgentInputs()) {
        mapInputAgent(agentInput);
        missingFeatures.remove(agentInput.getFeature());
        // Parse JSON from agentInput into <AGENT>FeatureInput message builder.
        JsonToStructConverter.map(featureInputBuilder, agentInput.getAgentInputJson().toString());
        // Add agent input, i.e., `<AGENT>Input.<AGENT>_inputs`.
        if (maybeRepeatedFeatureInputsField.isRepeated()) {
          matchInputBuilder.addRepeatedField(
              maybeRepeatedFeatureInputsField, featureInputBuilder.build());
        } else {
          matchInputBuilder.setField(
              maybeRepeatedFeatureInputsField, featureInputBuilder.build());
        }
        featureInputBuilder.clear();
      }
      return missingFeatures;
    }

    private void mapInputAgent(AgentInput agentInput) {
      var featureFromDatabase = agentInput.getFeature();
      var featureMapped = this.featureInputMapper.mapByValue(featureFromDatabase);
      final ObjectNode agentInputJson = agentInput.getAgentInputJson();
      agentInputJson.put(AGENT_INPUT_FEATURE_KEY, featureMapped);
    }
  }

  @Override
  public BatchFeatureInputResponse createEmptyResponse(
      Collection<String> matches, List<String> requestedFeatures) {

    var emptyMatchInputs = matches
        .stream()
        .map(m -> MatchInput
            .builder()
            .match(m)
            .agentInputs(List.of())
            .build())
        .collect(Collectors.toList());

    return map(new MatchFeatureOutput(getType(), emptyMatchInputs), requestedFeatures);
  }

  protected abstract Builder createBatchResponseBuilder();

  protected abstract Builder createInputBuilder();

  protected abstract Builder getDefaultFeatureInput();
}
