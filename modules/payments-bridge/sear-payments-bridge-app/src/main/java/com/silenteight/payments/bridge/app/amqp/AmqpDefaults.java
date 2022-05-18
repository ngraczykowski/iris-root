package com.silenteight.payments.bridge.app.amqp;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class AmqpDefaults {

  public static final String FIRCO_EXCHANGE_NAME = "pb.event";

  public static final String FIRCO_COMMAND_QUEUE_NAME = "pb.firco.command";

  public static final String FIRCO_RESPONSE_QUEUE_NAME = "pb.firco.response";

  public static final String FIRCO_ALERT_STORED_ROUTING_KEY = "pb.firco.command.stored";

  public static final String FIRCO_RESPONSE_COMPLETED_ROUTING_KEY = "pb.firco.response.completed";

  public static final String BRIDGE_RECOMMENDATION_QUEUE_NAME = "pb.recommendation";

  public static final String BRIDGE_MODEL_PROMOTED_PRODUCTION_QUEUE_NAME = "pb.model";

  public static final String BRIDGE_WAREHOUSE_EXCHANGE_NAME = "bridge.command";

  public static final String BRIDGE_WAREHOUSE_ROUTING_KEY = "command.index-request.production";

  public static final String DATA_RETENTION_EXCHANGE_NAME = "bridge.retention";

  public static final String DATA_RETENTION_QUEUE_NAME = "bridge.retention.alert";

  public static final String DATA_RETENTION_PERSONAL_INFORMATION_EXPIRED_ROUTING_KEY =
      "retention.personal-information-expired";

  public static final String DATA_RETENTION_ALERT_EXPIRED_ROUTING_KEY =
      "retention.alerts-expired";

  public static final String LEARNING_ENGINE_EXCHANGE_NAME = "bridge.historical";

  public static final String LEARNING_ENGINE_HISTORICAL_DECISION_ROUTING_KEY =
      "historical.historical-decisions-request.production";
}
