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
import com.silenteight.hsbc.datasource.extractors.geolocation.GeoPlaceOfBirthConfigurer;
import com.silenteight.hsbc.datasource.extractors.geolocation.GeoResidenciesConfigurer;
import com.silenteight.hsbc.datasource.extractors.historical.HistoricalDecisionsQueryConfigurer;
import com.silenteight.hsbc.datasource.extractors.ispep.IsPepQueryConfigurer;
import com.silenteight.hsbc.datasource.extractors.name.NameQueryConfigurer;
import com.silenteight.hsbc.datasource.feature.allowedlist.AllowListCommonApFeature;
import com.silenteight.hsbc.datasource.feature.allowedlist.AllowListCommonNameFeature;
import com.silenteight.hsbc.datasource.feature.allowedlist.AllowListCommonWpFeature;
import com.silenteight.hsbc.datasource.feature.allowedlist.AllowListInvalidAlertFeature;
import com.silenteight.hsbc.datasource.feature.country.*;
import com.silenteight.hsbc.datasource.feature.date.DateOfBirthFeature;
import com.silenteight.hsbc.datasource.feature.document.OtherDocumentFeature;
import com.silenteight.hsbc.datasource.feature.document.PassportNumberFeature;
import com.silenteight.hsbc.datasource.feature.gender.GenderFeature;
import com.silenteight.hsbc.datasource.feature.geolocation.GeoPlaceOfBirthFeature;
import com.silenteight.hsbc.datasource.feature.geolocation.GeoResidencyFeature;
import com.silenteight.hsbc.datasource.feature.historical.HistoricalIsApTpMarkedFeature;
import com.silenteight.hsbc.datasource.feature.historical.HistoricalIsCaseTpMarkedFeature;
import com.silenteight.hsbc.datasource.feature.historical.HistoricalIsTpMarkedFeature;
import com.silenteight.hsbc.datasource.feature.ispep.IsPepFeature;
import com.silenteight.hsbc.datasource.feature.ispep.IsPepFeatureV2;
import com.silenteight.hsbc.datasource.feature.name.NameFeature;
import com.silenteight.hsbc.datasource.feature.nationaliddocument.NationalIdDocumentFeature;

import java.util.Map;

import static com.silenteight.hsbc.datasource.feature.Feature.*;
import static java.util.Map.entry;
import static java.util.Map.ofEntries;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FeatureModel {

  private static final Map<Feature, Retriever> model;

  static {
    model = ofEntries(
        entry(ALLOW_LIST_COMMON_AP, new AllowListCommonApFeature()),
        entry(ALLOW_LIST_COMMON_NAME, new AllowListCommonNameFeature()),
        entry(ALLOW_LIST_COMMON_WP, new AllowListCommonWpFeature()),
        entry(ALLOW_LIST_INVALID_ALERT, new AllowListInvalidAlertFeature()),
        entry(GENDER, new GenderFeature()),
        entry(NAME, new NameFeature(new NameQueryConfigurer().create())),
        entry(IS_PEP, new IsPepFeature(new IsPepQueryConfigurer().create())),
        entry(
            IS_PEP_V2,
            new IsPepFeatureV2(
                new com.silenteight.hsbc.datasource.extractors.ispepV2.IsPepQueryConfigurer().create())),
        entry(
            GEO_PLACE_OF_BIRTH,
            new GeoPlaceOfBirthFeature(new GeoPlaceOfBirthConfigurer().create())),
        entry(
            GEO_RESIDENCIES,
            new GeoResidencyFeature(new GeoResidenciesConfigurer().create(), new NameQueryConfigurer().create())),
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
            new NationalIdDocumentFeature(new NationalIdDocumentQueryConfigurer().create())),
        entry(
            PASSPORT_NUMBER_DOCUMENT,
            new PassportNumberFeature(new PassportNumberDocumentQueryConfigurer().create())),
        entry(
            OTHER_DOCUMENT,
            new OtherDocumentFeature(new OtherDocumentQueryConfigurer().create())),
        entry(DATE_OF_BIRTH, new DateOfBirthFeature()),
        entry(
            HISTORICAL_IS_AP_TP_MARKED,
            new HistoricalIsApTpMarkedFeature(new HistoricalDecisionsQueryConfigurer().create())),
        entry(
            HISTORICAL_IS_CASE_TP_MARKED,
            new HistoricalIsCaseTpMarkedFeature(new HistoricalDecisionsQueryConfigurer().create())),
        entry(
            HISTORICAL_IS_TP_MARKED,
            new HistoricalIsTpMarkedFeature(new HistoricalDecisionsQueryConfigurer().create()))
    );
  }

  public static Retriever getFeatureRetriever(String featureName) {
    var feature = getByFullName(featureName);
    return model.get(feature);
  }
}
