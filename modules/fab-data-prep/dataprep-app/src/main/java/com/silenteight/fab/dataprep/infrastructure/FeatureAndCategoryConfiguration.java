package com.silenteight.fab.dataprep.infrastructure;

import com.silenteight.fab.dataprep.domain.category.*;
import com.silenteight.fab.dataprep.domain.feature.*;
import com.silenteight.fab.dataprep.infrastructure.grpc.CategoriesConfigurationProperties;

import com.jayway.jsonpath.ParseContext;
import com.jayway.jsonpath.TypeRef;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class FeatureAndCategoryConfiguration {

  public static final TypeRef<List<String>> LIST_OF_STRINGS = new TypeRef<>() {};

  private static final String CATEGORY_PREFIX = "categories/";

  @Bean
  @ConditionalOnProperty("feeding.features.gender-feature.enabled")
  FabFeature genderFeature(ParseContext parseContext) {
    return new GenderFeature(parseContext);
  }

  @Bean
  @ConditionalOnProperty("feeding.features.country-feature.enabled")
  FabFeature countryFeature(ParseContext parseContext) {
    return new CountryFeature(parseContext);
  }

  @Bean
  @ConditionalOnProperty("feeding.features.name-feature.enabled")
  FabFeature nameFeature(ParseContext parseContext) {
    return new NameFeature(parseContext);
  }

  @Bean
  @ConditionalOnProperty("feeding.features.date-feature.enabled")
  FabFeature dateFeature(ParseContext parseContext) {
    return new DateFeature(parseContext);
  }

  @Bean
  @ConditionalOnProperty("feeding.features.nationality-feature.enabled")
  FabFeature nationalityFeature(ParseContext parseContext) {
    return new NationalityFeature(parseContext);
  }

  @Bean
  @ConditionalOnProperty("feeding.features.passport-feature.enabled")
  FabFeature passportFeature(ParseContext parseContext) {
    return new PassportFeature(parseContext);
  }

  @Bean
  @ConditionalOnProperty("feeding.features.bic-feature.enabled")
  FabFeature bicFeature(ParseContext parseContext) {
    return new BicFeature(parseContext);
  }

  @Bean
  @ConditionalOnProperty("feeding.features.document-number-feature.enabled")
  FabFeature documentNumberFeature(ParseContext parseContext) {
    return new DocumentNumberFeature(parseContext);
  }

  @Bean
  @ConditionalOnProperty("feeding.features.national-id-feature.enabled")
  FabFeature nationalIdFeature(ParseContext parseContext) {
    return new NationalIdFeature(parseContext);
  }

  @Bean
  @ConditionalOnProperty("feeding.features.document-number-type-feature.enabled")
  FabFeature documentNumberTypeFeature() {
    return new DocumentNumberTypeFeature();
  }

  @Bean
  @ConditionalOnProperty("feeding.features.visa-document-expired-feature.enabled")
  FabFeature visaDocumentExpiredFeature() {
    return new VisaDocumentExpiredFeature();
  }

  @Bean
  @ConditionalOnProperty("feeding.categories.hitType.enabled")
  FabCategory hitTypeCategory(CategoriesConfigurationProperties configurationProperties) {
    String categoryName = "hitType";
    return new HitTypeCategory(
        configurationProperties.getCategories().get(categoryName), CATEGORY_PREFIX + categoryName);
  }

  @Bean
  @ConditionalOnProperty("feeding.categories.customerType.enabled")
  FabCategory customerTypeCategory(CategoriesConfigurationProperties configurationProperties) {
    String categoryName = "customerType";
    return new CustomerTypeCategory(
        configurationProperties.getCategories().get(categoryName), CATEGORY_PREFIX + categoryName
    );
  }

  @Bean
  @ConditionalOnProperty("feeding.categories.isHitOnWlName.enabled")
  FabCategory isHitOnWlName(
      CategoriesConfigurationProperties configurationProperties, ParseContext parseContext) {
    String categoryName = "isHitOnWlName";
    return new IsHitOnWlNameCategory(
        configurationProperties.getCategories().get(categoryName),
        CATEGORY_PREFIX + categoryName,
        parseContext);
  }

  @Bean
  @ConditionalOnProperty("feeding.categories.recordSourceType.enabled")
  FabCategory recordSourceType(
      CategoriesConfigurationProperties configurationProperties) {
    String categoryName = "recordSourceType";
    return new RecordSourceTypeCategory(
        configurationProperties.getCategories().get(categoryName), CATEGORY_PREFIX + categoryName);
  }

  @Bean
  @ConditionalOnProperty("feeding.categories.watchlistType.enabled")
  FabCategory watchlistType(
      CategoriesConfigurationProperties configurationProperties, ParseContext parseContext) {
    String categoryName = "watchlistType";
    return new WatchlistTypeCategory(
        configurationProperties.getCategories().get(categoryName),
        CATEGORY_PREFIX + categoryName,
        parseContext);
  }
}
