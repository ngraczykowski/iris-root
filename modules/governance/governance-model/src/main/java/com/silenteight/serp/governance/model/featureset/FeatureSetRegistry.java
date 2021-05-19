package com.silenteight.serp.governance.model.featureset;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.model.api.v1.Feature;
import com.silenteight.serp.governance.agent.domain.AgentsRegistry;
import com.silenteight.serp.governance.agent.domain.dto.AgentDto;
import com.silenteight.serp.governance.model.NonResolvableResourceException;
import com.silenteight.serp.governance.model.provide.grpc.PolicyFeatureProvider;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;

import static java.lang.String.format;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toUnmodifiableList;
import static java.util.stream.Collectors.toUnmodifiableMap;

@Slf4j
@RequiredArgsConstructor
public class FeatureSetRegistry
    implements CurrentFeatureSetProvider, PolicyFeatureProvider {

  public static final String DEFAULT_FEATURE_SET = "featureSets/1";

  @NonNull
  private final String source;

  @NonNull
  private final ObjectMapper objectMapper;

  @NonNull
  private final ResourceLoader resourceLoader;

  @NonNull
  private final AgentsRegistry agentsRegistry;

  @NonNull
  private final ConcurrentHashMap<String, FeatureSetJson> featureSets =
      new ConcurrentHashMap<>();

  @Override
  public FeatureSetDto getCurrentFeatureSet() {
    return getFeatureSet(DEFAULT_FEATURE_SET);
  }

  public FeatureSetDto getFeatureSet(String configName) {
    FeatureSetJson featureSet = Optional.ofNullable(featureSets.get(configName))
        .orElseThrow(() -> new NonResolvableResourceException(configName));

    List<FeatureDto> features = featureSet.getFeatures()
        .stream()
        .map(this::resolve)
        .collect(toUnmodifiableList());

    return FeatureSetDto.builder()
        .name(featureSet.getName())
        .features(features)
        .build();
  }

  private FeatureDto resolve(FeatureJson feature) {
    AgentDto agentDto = resolveAgent(feature.getAgentConfig());

    if (!agentDto.canHandleFeature(feature.getName())) {
      String errorMessage = format("Agent '%s' is not capable of solving feature '%s'",
          agentDto.getName(), feature.getName());
      throw new InvalidFeatureSetException(errorMessage);
    }

    return FeatureDto.builder()
        .name(feature.getName())
        .agentConfig(feature.getAgentConfig())
        .values(agentDto.getSolutions())
        .build();
  }

  private AgentDto resolveAgent(String agentName) {
    return agentsRegistry.getSingleAgent(agentName)
        .orElseThrow(() -> new NonResolvableFeatureSetException(agentName));
  }

  @PostConstruct
  void init() {
    try (InputStream inputStream = resourceLoader.getResource(source).getInputStream()) {
      featureSets.putAll(parseFeatureSets(inputStream));
      log.debug("Feature sets loaded from file: "
          + " source=" + source
          + " entriesCount=" + featureSets.size());
    } catch (Exception e) {
      log.error("Loading feature sets failed, could not load file: " + source, e);
    }
  }

  private Map<String, FeatureSetJson> parseFeatureSets(InputStream inputStream)
      throws IOException {

    TypeReference<List<FeatureSetJson>> agentConfigsWrapperType = new TypeReference<>() {};
    List<FeatureSetJson> featureSetsWrapper =
        objectMapper.readValue(inputStream, agentConfigsWrapperType);

    return featureSetsWrapper.stream()
        .collect(toUnmodifiableMap(FeatureSetJson::getName, identity()));
  }

  @Override
  public List<Feature> resolveFeatures(List<String> features) {
    return getCurrentFeatureSet()
        .getFeatures()
        .stream()
        .filter(feature -> features.contains(feature.getName()))
        .map(FeatureSetRegistry::toFeature)
        .collect(toList());
  }

  private static Feature toFeature(FeatureDto featureDto) {
    return Feature
        .newBuilder()
        .setName(featureDto.getName())
        .setAgentConfig(featureDto.getAgentConfig())
        .build();
  }
}
