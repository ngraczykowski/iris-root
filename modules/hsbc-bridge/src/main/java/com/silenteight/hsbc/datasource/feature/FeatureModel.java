package com.silenteight.hsbc.datasource.feature;

import java.util.HashMap;
import java.util.Map;

import static com.silenteight.hsbc.datasource.feature.Feature.GENDER;
import static com.silenteight.hsbc.datasource.feature.Feature.NATIONALITY_ID;

public class FeatureModel {

  private static final Map<Feature, FeatureValuesRetriever> model;

  static {
    model = new HashMap<>() {{
      put(GENDER, new GenderFeature());
      put(NATIONALITY_ID, new NationalityIdFeature());

      // TODO add remaining features
    }};
  }

  public static FeatureValuesRetriever getFeatureRetriever(String featureName) {
    var feature = Feature.getByName(featureName);
    return model.get(feature);
  }
}
