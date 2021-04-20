package com.silenteight.hsbc.datasource.feature;

import java.util.Map;

import static com.silenteight.hsbc.datasource.feature.Feature.GENDER;
import static com.silenteight.hsbc.datasource.feature.Feature.NATIONALITY_COUNTRY;
import static com.silenteight.hsbc.datasource.feature.Feature.NATIONALITY_ID;

public class FeatureModel {

  private static final Map<Feature, FeatureValuesRetriever> model;

  static {
    model = Map.of(
        GENDER, new GenderFeature(),
        NATIONALITY_ID, new NationalityIdFeature(),
        NATIONALITY_COUNTRY, new NationalityCountryFeature()
    );
    // TODO add remaining features;
  }

  public static FeatureValuesRetriever getFeatureRetriever(String featureName) {
    var feature = Feature.getByName(featureName);
    return model.get(feature);
  }
}
