package com.silenteight.payments.bridge.app.amqp;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
class AmqpDefaults {

  static final String FIRCO_EXCHANGE_NAME = "pb.event";

  static final String FIRCO_COMMAND_QUEUE_NAME = "pb.firco.command";

  static final String FIRCO_ALERT_STORED_ROUTING_KEY = "pb.firco.command.stored";

  static final String BRIDGE_RECOMMENDATION_QUEUE_NAME = "bridge.recommendations";

  static final String BRIDGE_MODEL_PROMOTED_PRODUCTION_QUEUE_NAME =
      "bridge.model-promoted.production";

}
