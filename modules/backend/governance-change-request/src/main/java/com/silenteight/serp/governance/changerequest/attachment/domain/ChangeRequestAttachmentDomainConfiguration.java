package com.silenteight.serp.governance.changerequest.attachment.domain;

import com.silenteight.serp.governance.changerequest.domain.ChangeRequestService;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan
@EnableJpaRepositories
class ChangeRequestAttachmentDomainConfiguration {

  @Bean
  ChangeRequestAttachmentsService changeRequestAttachmentsService(
      ChangeRequestAttachmentRepository repository,
      ChangeRequestService changeRequestService) {

    return new ChangeRequestAttachmentsService(repository, changeRequestService);
  }

  @Bean
  ChangeRequestAttachmentsQuery changeRequestAttachmentsQuery(
      ChangeRequestAttachmentRepository repository) {

    return new ChangeRequestAttachmentsQuery(repository);
  }
}
