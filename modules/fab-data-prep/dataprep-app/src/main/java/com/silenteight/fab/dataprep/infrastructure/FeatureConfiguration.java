package com.silenteight.fab.dataprep.infrastructure;

import com.silenteight.fab.dataprep.domain.feature.*;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ParseContext;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeatureConfiguration {

  @Bean
  ParseContext parseContext() {
    return JsonPath.using(com.jayway.jsonpath.Configuration
        .builder()
        .mappingProvider(new JacksonMappingProvider())
        .jsonProvider(new JacksonJsonNodeJsonProvider())
        .build());
  }

  @Bean
  @ConditionalOnProperty("feeding.features.gender-feature.enabled")
  FabFeature genderFeature() {
    return new GenderFeature();
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
}
