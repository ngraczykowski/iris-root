package com.silenteight.universaldatasource.app.feature.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

class FeatureInputMapperTest {


  private FeatureInputMapper configureFeatureMapper() {
    FeatureMapperConfigurationProperties configurationProperties =
        new FeatureMapperConfigurationProperties(Map.of("geo2", "geo"));

    return new FeatureInputMapper(configurationProperties);
  }

  @Test
  @DisplayName("When mapping is not found neither key nor value")
  public void whenMappingIsNotFound() {
    // given
    var featureMapper = configureFeatureMapper();

    // when
    var featureInput = "features/name";
    var mappedFeatureKey = featureMapper.mapByKey(featureInput);
    var mappedFeatureValue = featureMapper.mapByValue(featureInput, List.of(featureInput));

    //then
    Assertions.assertEquals(featureInput, mappedFeatureKey);
    Assertions.assertEquals(featureInput, mappedFeatureValue);
  }

  @Test
  @DisplayName("When only mapping value is found but not the key")
  public void whenMappingKeyIsNotFound() {
    // given
    var featureMapper = configureFeatureMapper();

    // when
    var mappedFeatureKey = featureMapper.mapByKey("features/geo2");
    var mappedFeatureValue = featureMapper.mapByValue("features/geo", List.of("features/geo3"));

    //then
    Assertions.assertEquals("features/geo", mappedFeatureKey);
    Assertions.assertEquals("features/geo", mappedFeatureValue);
  }

  @Test
  @DisplayName("When only mapping is found")
  public void whenMappingIsOnlyFound() {
    // given
    var featureMapper = configureFeatureMapper();

    // when
    var mappedFeatureKey = featureMapper.mapByKey("features/geo2");
    var mappedFeatureValue = featureMapper.mapByValue("features/geo", List.of("features/geo2"));

    //then
    Assertions.assertEquals("features/geo", mappedFeatureKey);
    Assertions.assertEquals("features/geo2", mappedFeatureValue);
  }
}
