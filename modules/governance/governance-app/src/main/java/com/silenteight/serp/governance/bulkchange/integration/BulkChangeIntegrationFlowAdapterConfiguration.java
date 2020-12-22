package com.silenteight.serp.governance.bulkchange.integration;

import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.bulkchange.BulkChangeCommands;
import com.silenteight.serp.governance.bulkchange.audit.BulkChangeAuditable;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class BulkChangeIntegrationFlowAdapterConfiguration {

  private final BulkChangeAuditable bulkChangeAuditable;
  private final BulkChangeCommands bulkChangeCommands;

  @Bean
  ApplyBulkChangeIntegrationFlowAdapter applyBulkChangeIntegrationFlowAdapter() {
    return new ApplyBulkChangeIntegrationFlowAdapter(bulkChangeAuditable, bulkChangeCommands);
  }

  @Bean
  CreateBulkChangeIntegrationFlowAdapter createBulkChangeIntegrationFlowAdapter() {
    return new CreateBulkChangeIntegrationFlowAdapter(bulkChangeAuditable, bulkChangeCommands);
  }

  @Bean
  RejectBulkChangeIntegrationFlowAdapter rejectBulkChangeIntegrationFlowAdapter() {
    return new RejectBulkChangeIntegrationFlowAdapter(bulkChangeAuditable, bulkChangeCommands);
  }

}
