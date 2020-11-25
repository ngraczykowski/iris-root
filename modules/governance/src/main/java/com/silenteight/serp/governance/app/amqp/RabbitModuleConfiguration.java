package com.silenteight.serp.governance.app.amqp;

import com.silenteight.serp.governance.decisiongroup.DecisionGroupService;
import com.silenteight.serp.governance.featuregroup.FeatureGroupService;
import com.silenteight.serp.governance.featureset.FeatureSetService;
import com.silenteight.serp.governance.model.ModelService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class RabbitModuleConfiguration {

  @Bean
  ModelReceiver modelReceiver(ModelService modelService, FeatureSetService featureSetService) {
    return new ModelReceiver(modelService, featureSetService);
  }

  @Bean
  FeatureGroupReceiver featureGroupReceiver(FeatureGroupService featureGroupService) {
    return new FeatureGroupReceiver(featureGroupService);
  }

  @Bean
  DecisionGroupReceiver decisionGroupReceiver(DecisionGroupService decisionGroupService) {
    return new DecisionGroupReceiver(decisionGroupService);
  }

}
