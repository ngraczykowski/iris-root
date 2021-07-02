package com.silenteight.hsbc.datasource.feature;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.hsbc.datasource.extractors.country.NationalityCountryQueryConfigurer;
import com.silenteight.hsbc.datasource.extractors.country.OtherCountryQueryConfigurer;
import com.silenteight.hsbc.datasource.extractors.country.RegistrationCountryFeatureQueryConfigurer;
import com.silenteight.hsbc.datasource.extractors.country.ResidencyCountryFeatureQueryConfigurer;
import com.silenteight.hsbc.datasource.extractors.document.NationalIdDocumentQueryConfigurer;
import com.silenteight.hsbc.datasource.extractors.document.OtherDocumentQueryConfigurer;
import com.silenteight.hsbc.datasource.extractors.document.PassportNumberDocumentQueryConfigurer;
import com.silenteight.hsbc.datasource.extractors.ispep.IsPepQueryConfigurer;
import com.silenteight.hsbc.datasource.extractors.name.NameQueryConfigurer;
import com.silenteight.hsbc.datasource.feature.allowedlist.AllowListFeature;
import com.silenteight.hsbc.datasource.feature.country.NationalityCountryFeature;
import com.silenteight.hsbc.datasource.feature.country.OtherCountryFeature;
import com.silenteight.hsbc.datasource.feature.country.ResidencyCountryFeature;
import com.silenteight.hsbc.datasource.feature.dob.DateOfBirthFeature;
import com.silenteight.hsbc.datasource.feature.gender.GenderFeature;
import com.silenteight.hsbc.datasource.feature.incorporationcountry.IncorporationCountryFeature;
import com.silenteight.hsbc.datasource.feature.ispep.IsPepFeature;
import com.silenteight.hsbc.datasource.feature.name.NameFeature;
import com.silenteight.hsbc.datasource.feature.nationaliddocument.NationalIdFeature;
import com.silenteight.hsbc.datasource.feature.otherdocument.OtherDocumentFeature;
import com.silenteight.hsbc.datasource.feature.passportnumberdocument.PassportNumberFeature;
import com.silenteight.hsbc.datasource.feature.registrationcountry.RegistrationCountryFeature;

import java.util.Map;

import static com.silenteight.hsbc.datasource.feature.Feature.*;
import static java.util.Map.entry;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FeatureModel {

  private static final Map<Feature, Retriever> model;

  static {
    model = Map.ofEntries(
        entry(ALLOW_LIST, new AllowListFeature()),
        entry(GENDER, new GenderFeature()),
        entry(NAME, new NameFeature(new NameQueryConfigurer().create())),
        entry(IS_PEP, new IsPepFeature(new IsPepQueryConfigurer().create())),
        entry(
            NATIONALITY_COUNTRY,
            new NationalityCountryFeature(new NationalityCountryQueryConfigurer().create())),
        entry(
            RESIDENCY_COUNTRY,
            new ResidencyCountryFeature(new ResidencyCountryFeatureQueryConfigurer().create())),
        entry(INCORPORATION_COUNTRY, new IncorporationCountryFeature()),
        entry(
            REGISTRATION_COUNTRY,
            new RegistrationCountryFeature(
                new RegistrationCountryFeatureQueryConfigurer().create())),
        entry(OTHER_COUNTRY, new OtherCountryFeature(new OtherCountryQueryConfigurer().create())),
        entry(
            NATIONAL_ID_DOCUMENT,
            new NationalIdFeature(new NationalIdDocumentQueryConfigurer().create())),
        entry(
            PASSPORT_NUMBER_DOCUMENT,
            new PassportNumberFeature(new PassportNumberDocumentQueryConfigurer().create())),
        entry(
            OTHER_DOCUMENT,
            new OtherDocumentFeature(new OtherDocumentQueryConfigurer().create())),
        entry(DATE_OF_BIRTH, new DateOfBirthFeature())
    );
  }

  public static Retriever getFeatureRetriever(String featureName) {
    var feature = Feature.getByFullName(featureName);
    return model.get(feature);
  }
}
