package com.silenteight.serp.governance.changerequest.attachment.add;

import com.silenteight.serp.governance.changerequest.attachment.domain.ChangeRequestAttachmentsService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class AddAttachmentsConfiguration {

  @Bean
  AddAttachmentsUseCase addAttachmentsUseCase(
      ChangeRequestAttachmentsService changeRequestAttachmentsService) {

    return new AddAttachmentsUseCase(changeRequestAttachmentsService);
  }
}
