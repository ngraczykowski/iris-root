package com.silenteight.serp.governance.file.domain;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan
@EnableJpaRepositories
class FileReferenceConfiguration {

  @Bean
  FileReferenceService fileReferenceService(FileReferenceRepository repository) {
    return new FileReferenceService(repository);
  }

  @Bean
  FileReferenceQuery fileReferenceQuery(FileReferenceRepository fileReferenceRepository) {
    return new FileReferenceQuery(fileReferenceRepository);
  }
}
