package com.silenteight.serp.governance.model.archive;

import lombok.NonNull;

import com.silenteight.serp.governance.changerequest.cancel.CancelChangeRequestUseCase;
import com.silenteight.serp.governance.changerequest.list.ListChangeRequestsQuery;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class GovernanceModelsArchivedConfiguration {

  @Bean
  GovernanceGovernanceModelsArchivedUseCase governanceModelsArchivedUseCase(
      @NonNull ListChangeRequestsQuery listChangeRequestsQuery,
      @NonNull CancelChangeRequestUseCase cancelChangeRequestUseCase) {

    return new GovernanceGovernanceModelsArchivedUseCase(listChangeRequestsQuery, cancelChangeRequestUseCase);
  }
}
