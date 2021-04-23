package com.silenteight.hsbc.datasource.feature;

import com.silenteight.hsbc.datasource.extractors.country.NationalityCountryQueryConfigurer;
import com.silenteight.hsbc.datasource.extractors.document.DocumentQueryConfigurer;
import com.silenteight.hsbc.datasource.feature.country.NationalityCountryFeature;
import com.silenteight.hsbc.datasource.feature.gender.GenderFeature;
import com.silenteight.hsbc.datasource.feature.nationalityid.NationalityIdFeature;

import java.util.Map;

import static com.silenteight.hsbc.datasource.feature.Feature.GENDER;
import static com.silenteight.hsbc.datasource.feature.Feature.NATIONALITY_COUNTRY;
import static com.silenteight.hsbc.datasource.feature.Feature.NATIONALITY_ID;

public class FeatureModel {

  private static final Map<Feature, FeatureValuesRetriever> model;

  static {
    var documentQueryConfigurer = new DocumentQueryConfigurer();

    model = Map.of(
        GENDER, new GenderFeature(),
        NATIONALITY_ID, new NationalityIdFeature(
            documentQueryConfigurer.alertedPartyDocumentQuery(),
            documentQueryConfigurer.matchPartyDocumentQuery()
        ),
        NATIONALITY_COUNTRY, new NationalityCountryFeature(
            new NationalityCountryQueryConfigurer().create()
        )
    );
    // TODO add remaining features;
  }

  public static FeatureValuesRetriever getFeatureRetriever(String featureName) {
    var feature = Feature.getByName(featureName);
    return model.get(feature);
  }
}
