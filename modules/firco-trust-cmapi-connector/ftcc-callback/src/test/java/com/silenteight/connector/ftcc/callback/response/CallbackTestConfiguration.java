package com.silenteight.connector.ftcc.callback.response;

import com.silenteight.connector.ftcc.callback.CallbackModule;
import com.silenteight.connector.ftcc.callback.handler.BatchCompletedHandler;
import com.silenteight.connector.ftcc.callback.handler.BatchCompletedRepository;
import com.silenteight.connector.ftcc.callback.handler.BatchCompletedService;
import com.silenteight.connector.ftcc.request.details.MessageDetailsQuery;
import com.silenteight.recommendation.api.library.v1.RecommendationServiceClient;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = CallbackModule.class)
class CallbackTestConfiguration {

  @MockBean
  RecommendationServiceClient recommendationServiceClient;

  @MockBean
  MessageDetailsQuery messageDetailsQuery;

  @MockBean
  RabbitTemplate rabbitTemplate;

  @MockBean
  BatchCompletedHandler batchCompletedHandler;

  @MockBean
  BatchCompletedService batchCompletedService;

  @MockBean
  BatchCompletedRepository batchCompletedRepository;

}
