package com.silenteight.serp.governance.model.archive;

import lombok.NonNull;

import com.silenteight.serp.governance.changerequest.cancel.CancelChangeRequestUseCase;
import com.silenteight.serp.governance.changerequest.list.ListChangeRequestsQuery;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ModelsArchivedConfiguration {

  @Bean
  ModelsArchivedUseCase modelsArchivedUseCase(
      @NonNull ListChangeRequestsQuery listChangeRequestsQuery,
      @NonNull CancelChangeRequestUseCase cancelChangeRequestUseCase) {

    return new ModelsArchivedUseCase(listChangeRequestsQuery, cancelChangeRequestUseCase);
  }
}
