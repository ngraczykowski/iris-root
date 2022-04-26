package com.silenteight.connector.ftcc.callback.outgoing;

import com.silenteight.proto.recommendation.api.v1.RecommendationsDelivered;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static java.util.UUID.randomUUID;
import static org.mockito.Mockito.*;

@SpringJUnitConfig
class RecommendationsDeliveredRabbitPublisherTest {

  @Mock
  private RabbitTemplate rabbitTemplate;

  @ParameterizedTest(name = "When analysisName={0}, batchName={1} then should pass")
  @CsvSource({
      "analysis/1,batches/c215a83e-f083-4a16-a073-d70a4600dfce",
      "randomAnalysisString,randomBatchString" })
  void verifyBuildingEvent(String analysisName, String batchName) {
    Assertions.assertDoesNotThrow(() -> RecommendationsDeliveredEvent.builder()
        .analysisName(analysisName)
        .batchName(batchName)
        .alertType("solving")
        .build());
  }

  @DisplayName("Send message using exchange")
  @Test
  void sendMessageUsingExchange() {
    var recommendationsDeliveredPublisher = recommendationPublisher();

    Assertions.assertDoesNotThrow(
        () -> recommendationsDeliveredPublisher.publish(RecommendationsDeliveredEvent.builder()
            .analysisName("analysis/1")
            .batchName("batches/" + randomUUID())
            .alertType("solving")
            .build()));
    verify(rabbitTemplate, times(1)).convertAndSend(
        eq("rabbit-exchange"), eq(""), any(RecommendationsDelivered.class));
  }

  private RecommendationsDeliveredRabbitPublisher recommendationPublisher() {
    var properties = new RecommendationsDeliveredAmqpProperties("rabbit-exchange");
    return new RecommendationsDeliveredRabbitPublisher(rabbitTemplate, properties);
  }
}
