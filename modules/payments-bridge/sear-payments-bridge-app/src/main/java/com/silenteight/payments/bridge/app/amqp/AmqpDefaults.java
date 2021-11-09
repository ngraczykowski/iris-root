package com.silenteight.payments.bridge.app.amqp;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
class AmqpDefaults {

  static final String FIRCO_EXCHANGE_NAME = "pb.event";

  static final String FIRCO_COMMAND_QUEUE_NAME = "pb.firco.command";

  static final String FIRCO_RESPONSE_QUEUE_NAME = "pb.firco.response";

  static final String FIRCO_ALERT_STORED_ROUTING_KEY = "pb.firco.command.stored";

  static final String FIRCO_RESPONSE_COMPLETED_ROUTING_KEY = "pb.firco.response.completed";

  static final String BRIDGE_RECOMMENDATION_QUEUE_NAME = "pb.recommendation";

  static final String BRIDGE_MODEL_PROMOTED_PRODUCTION_QUEUE_NAME = "pb.model";

  static final String BRIDGE_WAREHOUSE_EXCHANGE_NAME = "bridge.command";

  static final String BRIDGE_WAREHOUSE_ROUTING_KEY = "command.index-request.production";

  static final String DATA_RETENTION_EXCHANGE_NAME = "bridge.retention";

  static final String DATA_RETENTION_PERSONAL_INFORMATION_EXPIRED_ROUTING_KEY =
      "retention.personal-information-expired";

  static final String DATA_RETENTION_ALERT_EXPIRED_ROUTING_KEY =
      "retention.alerts-expired";

}
