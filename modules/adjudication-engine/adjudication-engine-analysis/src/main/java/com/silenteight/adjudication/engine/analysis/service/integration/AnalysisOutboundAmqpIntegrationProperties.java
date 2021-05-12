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
  private Events events = new Events();

  @NestedConfigurationProperty
  @Valid
  @NotNull
  private InternalEvents internalEvents = new InternalEvents();

  @Data
  static class Events {

    @NotBlank
    private String outboundExchangeName = EVENTS_EXCHANGE_NAME;

    @NotNull
    private String recommendationsGeneratedRoutingKey = RECOMMENDATIONS_GENERATED_ROUTING_KEY;
  }

  @Data
  static class InternalEvents {

    @NotBlank
    private String outboundExchangeName = INTERNAL_EVENTS_EXCHANGE_NAME;

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
}
