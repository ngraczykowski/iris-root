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
import com.silenteight.hsbc.datasource.extractors.name.NameQueryConfigurer;
import com.silenteight.hsbc.datasource.extractors.newsage.NewsAgeConfigurer;
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
import com.silenteight.hsbc.datasource.feature.name.NameFeature;
import com.silenteight.hsbc.datasource.feature.nationaliddocument.NationalIdDocumentFeature;
import com.silenteight.hsbc.datasource.feature.newsage.NewsAgeFeature;

import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FeatureModel {

  private static final Map<Feature, Retriever> model;

  static {
    model = Map.ofEntries(
        Map.entry(Feature.ALLOW_LIST_COMMON_AP, new AllowListCommonApFeature()),
        Map.entry(Feature.ALLOW_LIST_COMMON_NAME, new AllowListCommonNameFeature()),
        Map.entry(Feature.ALLOW_LIST_COMMON_WP, new AllowListCommonWpFeature()),
        Map.entry(Feature.ALLOW_LIST_INVALID_ALERT, new AllowListInvalidAlertFeature()),
        Map.entry(Feature.GENDER, new GenderFeature()),
        Map.entry(Feature.NAME, new NameFeature(new NameQueryConfigurer().create())),
        Map.entry(
            Feature.IS_PEP,
            new IsPepFeature(
                new com.silenteight.hsbc.datasource.extractors.ispep.IsPepQueryConfigurer().create())),
        Map.entry(
            Feature.GEO_PLACE_OF_BIRTH,
            new GeoPlaceOfBirthFeature(new GeoPlaceOfBirthConfigurer().create())),
        Map.entry(
            Feature.GEO_RESIDENCIES,
            new GeoResidencyFeature(new GeoResidenciesConfigurer().create(), new NameQueryConfigurer().create())),
        Map.entry(
            Feature.NATIONALITY_COUNTRY,
            new NationalityCountryFeature(new NationalityCountryQueryConfigurer().create())),
        Map.entry(
            Feature.RESIDENCY_COUNTRY,
            new ResidencyCountryFeature(new ResidencyCountryFeatureQueryConfigurer().create())),
        Map.entry(Feature.INCORPORATION_COUNTRY, new IncorporationCountryFeature()),
        Map.entry(
            Feature.REGISTRATION_COUNTRY,
            new RegistrationCountryFeature(
                new RegistrationCountryFeatureQueryConfigurer().create())),
        Map.entry(Feature.OTHER_COUNTRY, new OtherCountryFeature(new OtherCountryQueryConfigurer().create())),
        Map.entry(
            Feature.NATIONAL_ID_DOCUMENT,
            new NationalIdDocumentFeature(new NationalIdDocumentQueryConfigurer().create())),
        Map.entry(
            Feature.PASSPORT_NUMBER_DOCUMENT,
            new PassportNumberFeature(new PassportNumberDocumentQueryConfigurer().create())),
        Map.entry(
            Feature.OTHER_DOCUMENT,
            new OtherDocumentFeature(new OtherDocumentQueryConfigurer().create())),
        Map.entry(Feature.DATE_OF_BIRTH, new DateOfBirthFeature()),
        Map.entry(
            Feature.HISTORICAL_IS_AP_TP_MARKED,
            new HistoricalIsApTpMarkedFeature(new HistoricalDecisionsQueryConfigurer().create())),
        Map.entry(
            Feature.HISTORICAL_IS_CASE_TP_MARKED,
            new HistoricalIsCaseTpMarkedFeature(new HistoricalDecisionsQueryConfigurer().create())),
        Map.entry(
            Feature.HISTORICAL_IS_TP_MARKED,
            new HistoricalIsTpMarkedFeature(new HistoricalDecisionsQueryConfigurer().create())),
        Map.entry(
            Feature.NEWS_AGE,
            new NewsAgeFeature(new NewsAgeConfigurer().create()))
    );
  }

  public static Retriever getFeatureRetriever(String featureName) {
    var feature = Feature.getByFullName(featureName);
    return model.get(feature);
  }
}
