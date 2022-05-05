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

  static final boolean AGENT_RESPONSE_ENABLED_DEFAULT = true;

  @NestedConfigurationProperty
  @Valid
  @NotNull
  private PendingRecommendation pendingRecommendation = new PendingRecommendation();

  @NestedConfigurationProperty
  @Valid
  @NotNull
  private AgentExchange agentExchange = new AgentExchange();

  @NestedConfigurationProperty
  @Valid
  @NotNull
  private AlertExpired alertExpired = new AlertExpired();

  @NestedConfigurationProperty
  @Valid
  @NotNull
  private PiiExpired piiExpired = new PiiExpired();

  @NestedConfigurationProperty
  @Valid
  @NotNull
  private DeleteAgentExchange deleteAgentExchange = new DeleteAgentExchange();

  @NestedConfigurationProperty
  @Valid
  @NotNull
  private AgentResponse agentResponse = new AgentResponse();

  @NestedConfigurationProperty
  @Valid
  @NotNull
  private Category category = new Category();

  @NestedConfigurationProperty
  @Valid
  @NotNull
  private CommentInput commentInput = new CommentInput();

  @NestedConfigurationProperty
  @Valid
  @NotNull
  private AnalysisCancelled analysisCancelled = new AnalysisCancelled();

  String[] getEventInternalInboundQueueNames() {
    return new String[] {
        pendingRecommendation.getInboundQueueName(),
        agentExchange.getInboundQueueName(),
        category.getInboundQueueName(),
        commentInput.getInboundQueueName(),
        analysisCancelled.getInboundQueueName(),
        };
  }

  String getAgentResponseInboundQueueName() {
    return agentResponse.getInboundQueueName();
  }

  String getDataRetentionInboundQueueName() {
    return alertExpired.getInboundQueueName();
  }

  String getPiiExpiredInboundQueueName() {
    return piiExpired.getInboundQueueName();
  }

  String getAnalysisCancelledInboundQueueName() {
    return analysisCancelled.getInboundQueueName();
  }

  @Data
  static class PendingRecommendation {

    @NotBlank
    private String inboundQueueName = PENDING_RECOMMENDATION_QUEUE_NAME;
  }

  @Data
  static class AgentExchange {

    @NotBlank
    private String inboundQueueName = AGENT_EXCHANGE_QUEUE_NAME;
  }

  @Data
  static class DeleteAgentExchange {

    @NotBlank
    private String inboundQueueName = DELETE_AGENT_EXCHANGE_QUEUE_NAME;
  }

  @Data
  static class AgentResponse {

    @NotBlank
    private String inboundQueueName = AGENT_RESPONSE_QUEUE_NAME;

    private boolean enabled = AGENT_RESPONSE_ENABLED_DEFAULT;
  }

  @Data
  static class AlertExpired {

    @NotBlank
    private String inboundQueueName = ALERT_EXPIRED_QUEUE_NAME;
  }

  @Data
  static class PiiExpired {

    @NotBlank
    private String inboundQueueName = PII_EXPIRED_QUEUE_NAME;
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

  @Data
  static class AnalysisCancelled {

    @NotBlank
    private String inboundQueueName = ANALYSIS_CANCELLED_QUEUE_NAME;
  }
}
