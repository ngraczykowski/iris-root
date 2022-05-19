package com.silenteight.connector.ftcc.callback.response;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.connector.ftcc.callback.newdecision.DecisionMapperUseCase;
import com.silenteight.connector.ftcc.callback.newdecision.DestinationStatus;
import com.silenteight.connector.ftcc.callback.outgoing.RecommendationsDeliveredPublisher;
import com.silenteight.connector.ftcc.request.details.MessageDetailsQuery;
import com.silenteight.connector.ftcc.request.details.dto.MessageDetailsDto;
import com.silenteight.connector.ftcc.request.details.dto.StatusDto;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

import static org.springframework.context.annotation.AdviceMode.ASPECTJ;
import static org.springframework.context.annotation.AdviceMode.PROXY;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class ResponseProcessorConfiguration {

  @EnableAsync(mode = ASPECTJ)
  @Import(value = {
      ResponseConfiguration.class, GrpcRecommendationConfiguration.class })
  static class TestConfigAspectJ extends TestConfig {
  }

  @EnableAsync(mode = PROXY)
  @Import(value = {
      ResponseConfiguration.class, GrpcRecommendationConfiguration.class })
  static class TestConfigProxy extends TestConfig {
  }

  static class TestConfig {

    @MockBean private RestTemplate restTemplate;

    @Bean
    MessageDetailsQuery messageDetailsQuery() {
      return batchId -> List.of(MessageDetailsDto
          .builder()
          .batchId(batchId)
          .id(UUID.randomUUID())
          .messageID("Firco MESSAGEID;)")
          .businessUnit("")
          .systemID("Firco SYSTEMID")
          .unit("UNIT!")
          .currentStatus(StatusDto
              .builder()
              .id("CurrentStatus ID")
              .checksum("CurrentStatus checksum")
              .name("CurrentStatus name")
              .routingCode("CurrentStatus Code")
              .build())
          .build());
    }

    @Bean
    DecisionMapperUseCase decisionMapperUseCase() {
      return request -> DestinationStatus.builder().build();
    }

    @Bean
    ResponseCreator responseCreator(DecisionMapperUseCase decisionMapperUseCase) {
      return new ResponseCreator(
          decisionMapperUseCase,
          new RecommendationSenderProperties("localhost:8080", "admin", "pass",
              Duration.ofSeconds(10), Duration.ofSeconds(10), "/path", "pass",
              "Manual investigation"));
    }

    @Bean
    ClientRequestDtoBuilder callbackRequestBuilder(
        ResponseCreator responseCreator, MessageDetailsService messageDetailsService) {
      return new ClientRequestDtoBuilder(responseCreator, messageDetailsService);
    }

    @Bean
    ResponseProcessor responseProcessor(
        ClientRequestDtoBuilder clientRequestDtoBuilder, RecommendationSender recommendationSender,
        RecommendationClientApi recommendationClientApi,
        RecommendationsDeliveredPublisher recommendationsDeliveredPublisher) {
      return new ResponseProcessor(
          clientRequestDtoBuilder, recommendationSender, recommendationClientApi,
          recommendationsDeliveredPublisher);
    }

    @Bean
    RecommendationsDeliveredPublisher recommendationsDeliveredPublisher() {
      return event -> {

      };
    }
  }
}
