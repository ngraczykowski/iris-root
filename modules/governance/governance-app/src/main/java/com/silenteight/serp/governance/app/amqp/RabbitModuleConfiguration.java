package com.silenteight.serp.governance.app.amqp;

import com.silenteight.sep.base.common.protocol.MessageRegistry;
import com.silenteight.sep.base.common.protocol.MessageRegistryFactory;
import com.silenteight.serp.governance.decisiongroup.DecisionGroupService;
import com.silenteight.serp.governance.featuregroup.FeatureGroupService;
import com.silenteight.serp.governance.featureset.FeatureSetService;
import com.silenteight.serp.governance.model.ModelService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
class RabbitModuleConfiguration {

  @Bean
  @Primary
  MessageRegistry messageRegistryOverwrite() {
    MessageRegistryFactory factory = new MessageRegistryFactory(
        "com.silenteight.governance.api",
        "com.google.protobuf",
        "com.google.rpc",
        "com.google.type"
    );

    return factory.create();
  }

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
