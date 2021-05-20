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
@ConfigurationProperties(prefix = "ae.analysis.integration.outbound")
class AnalysisOutboundAmqpIntegrationProperties {

  @NestedConfigurationProperty
  @Valid
  @NotNull
  private Event event = new Event();

  @NestedConfigurationProperty
  @Valid
  @NotNull
  private EventInternal eventInternal = new EventInternal();

  @NestedConfigurationProperty
  @Valid
  @NotNull
  private Agent agent = new Agent();

  @Data
  static class Event {

    @NotBlank
    private String outboundExchangeName = EVENT_EXCHANGE_NAME;

    @NotNull
    private String recommendationsGeneratedRoutingKey = RECOMMENDATIONS_GENERATED_ROUTING_KEY;
  }

  @Data
  static class EventInternal {

    @NotBlank
    private String outboundExchangeName = EVENT_INTERNAL_EXCHANGE_NAME;

    @NotNull
    private String addedAnalysisDatasetsRoutingKey = ADDED_ANALYSIS_DATASETS_ROUTING_KEY;

    @NotNull
    private String pendingRecommendationsRoutingKey = PENDING_RECOMMENDATIONS_ROUTING_KEY;

    @NotNull
    private String matchCategoriesUpdatedRoutingKey = MATCH_CATEGORIES_UPDATED_ROUTING_KEY;

    @NotNull
    private String commentInputsUpdatedRoutingKey = COMMENT_INPUTS_UPDATED_ROUTING_KEY;

    @NotNull
    private String matchFeaturesUpdatedRoutingKey = MATCH_FEATURES_UPDATED_ROUTING_KEY;

    @NotNull
    private String matchesSolvedRoutingKey = MATCHES_SOLVED_ROUTING_KEY;
  }

  @Data
  static class Agent {

    @NotBlank
    private String outboundExchangeName = AGENT_REQUEST_EXCHANGE_NAME;
  }
}
