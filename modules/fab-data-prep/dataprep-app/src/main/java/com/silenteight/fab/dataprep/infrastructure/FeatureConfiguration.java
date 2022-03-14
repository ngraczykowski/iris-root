package com.silenteight.fab.dataprep.infrastructure;

import com.silenteight.fab.dataprep.domain.feature.*;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.ParseContext;
import com.jayway.jsonpath.TypeRef;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class FeatureConfiguration {

  public static final TypeRef<List<String>> LIST_OF_STRINGS = new TypeRef<>() {};

  @Bean
  ParseContext parseContext() {
    return JsonPath.using(com.jayway.jsonpath.Configuration
        .builder()
        .options(Option.SUPPRESS_EXCEPTIONS, Option.ALWAYS_RETURN_LIST)
        .mappingProvider(new JacksonMappingProvider())
        .jsonProvider(new JacksonJsonNodeJsonProvider())
        .build());
  }

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
}
