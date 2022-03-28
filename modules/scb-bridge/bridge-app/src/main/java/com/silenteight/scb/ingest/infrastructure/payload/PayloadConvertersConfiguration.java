package com.silenteight.scb.ingest.infrastructure.payload;

import com.silenteight.scb.ingest.domain.payload.PayloadConverter;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Validator;

@Configuration
class PayloadConvertersConfiguration {

  @Bean
  public PayloadConverter payloadConverter(Validator validator, ObjectMapper objectMapper) {
    var configuredObjectMapper = objectMapper.copy()
        .configure(JsonReadFeature.ALLOW_JAVA_COMMENTS.mappedFeature(), true)
        .setSerializationInclusion(Include.NON_NULL);

    return new PayloadConverter(validator, configuredObjectMapper);
  }
}
