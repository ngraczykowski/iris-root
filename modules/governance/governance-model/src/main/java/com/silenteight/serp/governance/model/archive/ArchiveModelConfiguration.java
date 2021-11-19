package com.silenteight.serp.governance.model.archive;

import lombok.NonNull;

import com.silenteight.serp.governance.model.archive.amqp.ModelsArchivedMessageGateway;
import com.silenteight.serp.governance.model.get.ModelDetailsQuery;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ArchiveModelConfiguration {

  @Bean
  PolicyArchivedEventHandler policyArchivedEventHandler(
      @NonNull ModelDetailsQuery modelDetailsQuery,
      @NonNull ModelsArchivedMessageGateway messageGateway) {

    return new PolicyArchivedEventHandler(modelDetailsQuery, messageGateway);
  }
}
