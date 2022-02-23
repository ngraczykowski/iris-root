package com.silenteight.universaldatasource.app.feature.mapper;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

@Service
@EnableConfigurationProperties(FeatureMapperConfigurationProperties.class)
public class FeatureInputMapper {

  public static final String FEATURE_NAME_PREFIX = "features";
  private final Map<String, String> featureInputMapping;

  FeatureInputMapper(
      final FeatureMapperConfigurationProperties featureMapperConfigurationProperties) {
    this.featureInputMapping = this.featureMapping(featureMapperConfigurationProperties);
  }

  public String mapByKey(final String feature) {
    return this.featureInputMapping.getOrDefault(feature, feature);
  }

  public List<String> mapByKeys(final List<String> features) {
    return features.stream()
        .map(this::mapByKey)
        .collect(Collectors.toList());
  }

  public String mapByValue(final String feature) {
    for (final Entry<String, String> entry : this.featureInputMapping.entrySet()) {
      if (entry.getValue().equalsIgnoreCase(feature)) {
        return entry.getKey();
      }
    }

    return feature;
  }

  private Map<String, String> featureMapping(
      final FeatureMapperConfigurationProperties featureMapperConfigurationProperties) {
    var featuresMapping = new HashMap<String, String>();
    final Map<String, String> mapping = featureMapperConfigurationProperties.getMapping();
    if (mapping != null) {
      for (var entry : mapping.entrySet()) {
        featuresMapping.put(createFeatureName(entry.getKey()), createFeatureName(entry.getValue()));
      }
    }
    return featuresMapping;
  }

  private String createFeatureName(String feature) {
    return FEATURE_NAME_PREFIX + "/" + feature;
  }
}
