package com.silenteight.adjudication.engine.analysis.service.integration;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
class AmqpDefaults {

  private static final String PREFIX = "ae.";
  private static final String COMMAND_PREFIX = PREFIX + "command.";
  private static final String EVENT_PREFIX = PREFIX + "event.";

  static final String EVENT_EXCHANGE_NAME = PREFIX + "event";
  static final String RECOMMENDATIONS_GENERATED_ROUTING_KEY =
      EVENT_PREFIX + "recommendations-generated";

  static final String EVENT_INTERNAL_EXCHANGE_NAME = PREFIX + "event.internal";
  static final String ANALYSIS_ALERTS_ADDED_ROUTING_KEY =
      EVENT_PREFIX + "analysis-alerts-added";
  static final String DELETE_AGENT_EXCHANGE_ROUTING_KEY =
      EVENT_PREFIX + "delete-agent-exchange";
  static final String PENDING_RECOMMENDATIONS_ROUTING_KEY =
      EVENT_PREFIX + "pending-recommendations";
  static final String MATCH_CATEGORIES_UPDATED_ROUTING_KEY =
      EVENT_PREFIX + "match-categories-updated";
  static final String COMMENT_INPUTS_UPDATED_ROUTING_KEY =
      EVENT_PREFIX + "comment-inputs-updated";
  static final String MATCH_FEATURES_UPDATED_ROUTING_KEY =
      EVENT_PREFIX + "match-features-updated";
  static final String MATCHES_SOLVED_ROUTING_KEY =
      EVENT_PREFIX + "matches-solved-updated";

  static final String PENDING_RECOMMENDATION_QUEUE_NAME = PREFIX + "pending-recommendation";
  static final String AGENT_EXCHANGE_QUEUE_NAME = PREFIX + "agent-exchange";
  static final String DELETE_AGENT_EXCHANGE_QUEUE_NAME = PREFIX + "delete-agent-exchange";
  static final String CATEGORY_QUEUE_NAME = PREFIX + "category";
  static final String COMMENT_INPUT_QUEUE_NAME = PREFIX + "comment-input";
  static final String MATCH_FEATURE_QUEUE_NAME = PREFIX + "match-feature";

  static final String AGENT_REQUEST_EXCHANGE_NAME = "agent.request";
  static final String TMP_AGENT_REQUEST_QUEUE_NAME = PREFIX + "tmp-agent-request";

  static final String AGENT_RESPONSE_EXCHANGE_NAME = "agent.response";
  static final String AGENT_RESPONSE_QUEUE_NAME = PREFIX + "agent-response";
}
