package com.silenteight.serp.governance.ingest.repackager;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

@Configuration
@EnableConfigurationProperties(RepackagerProperties.class)
class RepackagerConfiguration {

  @Bean
  IngestDataValidator ingestDataValidator(@Valid RepackagerProperties properties) {
    return new IngestDataValidator(properties.getFvSignatureKey());
  }

  @Bean
  IngestDataToSolvedEventRepackagerService ingestDataToSolvedEventRepackagerService(
      @Valid RepackagerProperties properties) {

    return new IngestDataToSolvedEventRepackagerService(
        properties.getFeatureOrCategoryRegex(),
        properties.getPrefixAndSuffixRegex(),
        properties.getFvSignatureKey());
  }
}
