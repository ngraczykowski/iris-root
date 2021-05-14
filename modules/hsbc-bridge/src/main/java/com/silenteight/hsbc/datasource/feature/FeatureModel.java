package com.silenteight.hsbc.datasource.feature;

import com.silenteight.hsbc.datasource.extractors.country.NationalityCountryQueryConfigurer;
import com.silenteight.hsbc.datasource.extractors.country.ResidencyCountryFeatureQueryConfigurer;
import com.silenteight.hsbc.datasource.extractors.document.NationalIdDocumentQueryConfigurer;
import com.silenteight.hsbc.datasource.extractors.document.OtherDocumentQueryConfigurer;
import com.silenteight.hsbc.datasource.extractors.document.PassportNumberDocumentQueryConfigurer;
import com.silenteight.hsbc.datasource.feature.country.NationalityCountryFeature;
import com.silenteight.hsbc.datasource.feature.country.ResidencyCountryFeature;
import com.silenteight.hsbc.datasource.feature.dob.DateOfBirthFeature;
import com.silenteight.hsbc.datasource.feature.gender.GenderFeature;
import com.silenteight.hsbc.datasource.feature.incorporationcountry.IncorporationCountryFeature;
import com.silenteight.hsbc.datasource.feature.nationaliddocument.NationalIdFeature;
import com.silenteight.hsbc.datasource.feature.otherdocument.OtherDocumentFeature;
import com.silenteight.hsbc.datasource.feature.passportnumberdocument.PassportNumberFeature;
import com.silenteight.hsbc.datasource.feature.registrationcountry.RegistrationCountryFeature;

import java.util.Map;

import static com.silenteight.hsbc.datasource.feature.Feature.*;

public class FeatureModel {

  private static final Map<Feature, FeatureValuesRetriever> model;

  static {
    model = Map.of(
        GENDER, new GenderFeature(),
        NATIONAL_ID_DOCUMENT,
        new NationalIdFeature(new NationalIdDocumentQueryConfigurer().create()),
        PASSPORT_NUMBER_DOCUMENT,
        new PassportNumberFeature(new PassportNumberDocumentQueryConfigurer().create()),
        OTHER_DOCUMENT, new OtherDocumentFeature(new OtherDocumentQueryConfigurer().create()),
        NATIONALITY_COUNTRY, new NationalityCountryFeature(
            new NationalityCountryQueryConfigurer().create()
        ),
        RESIDENCY_COUNTRY, new ResidencyCountryFeature(
            new ResidencyCountryFeatureQueryConfigurer().getFactory()
        ),
        INCORPORATION_COUNTRY, new IncorporationCountryFeature(),
        REGISTRATION_COUNTRY, new RegistrationCountryFeature(),
        DATE_OF_BIRTH, new DateOfBirthFeature()
    );
    // TODO add remaining features;
  }

  public static FeatureValuesRetriever getFeatureRetriever(String featureName) {
    var feature = Feature.getByName(featureName);
    return model.get(feature);
  }
}
