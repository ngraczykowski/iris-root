package com.silenteight.adjudication.engine.analysis.service.integration;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.silenteight.adjudication.engine.analysis.service.integration.AmqpDefaults.*;

@Data
@Validated
@ConfigurationProperties(prefix = "ae.analysis.integration.inbound")
class AnalysisInboundAmqpIntegrationProperties {

  @NestedConfigurationProperty
  @Valid
  @NotNull
  private PendingRecommendations pendingRecommendations = new PendingRecommendations();

  @NestedConfigurationProperty
  @Valid
  @NotNull
  private AgentExchange agentExchange = new AgentExchange();

  @NestedConfigurationProperty
  @Valid
  @NotNull
  private Category category = new Category();

  @NestedConfigurationProperty
  @Valid
  @NotNull
  private CommentInput commentInput = new CommentInput();

  @Data
  static class PendingRecommendations {

    @NotBlank
    private String inboundQueueName = PENDING_RECOMMENDATIONS_QUEUE_NAME;
  }

  @Data
  static class AgentExchange {

    @NotBlank
    private String inboundQueueName = AGENT_EXCHANGE_QUEUE_NAME;
  }

  @Data
  static class Category {

    @NotBlank
    private String inboundQueueName = CATEGORY_QUEUE_NAME;
  }

  @Data
  static class CommentInput {

    @NotBlank
    private String inboundQueueName = COMMENT_INPUT_QUEUE_NAME;
  }

  @Data
  static class MatchFeature {

    @NotBlank
    private String inboundQueueName = MATCH_FEATURE_QUEUE_NAME;
  }
}
