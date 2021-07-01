package com.silenteight.serp.governance.model.transfer.export;

import com.silenteight.serp.governance.changerequest.approval.ModelApprovalQuery;
import com.silenteight.serp.governance.model.get.ModelDetailsQuery;
import com.silenteight.serp.governance.model.transfer.export.amqp.PolicyPromotedMessageGateway;
import com.silenteight.serp.governance.policy.transfer.export.ExportPolicyUseCase;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ExportModelConfiguration {

  @Bean
  ExportModelUseCase exportModelUseCase(
      ModelDetailsQuery modelDetailsQuery,
      ModelApprovalQuery modelApprovalQuery,
      ExportPolicyUseCase exportPolicyUseCase) {

    return new ExportModelUseCase(modelDetailsQuery, modelApprovalQuery, exportPolicyUseCase);
  }

  @Bean
  SendPromoteMessageUseCase sendPromoteMessageUseCase(PolicyPromotedMessageGateway messageGateway) {
    return new SendPromoteMessageUseCase(messageGateway);
  }
}
