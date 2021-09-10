package com.silenteight.payments.bridge.app.amqp;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
class AmqpDefaults {

  private static final String PREFIX = "pb.";
  private static final String COMMAND_PREFIX = PREFIX + "command.";
  private static final String EVENT_PREFIX = PREFIX + "event.";

  static final String COMMAND_EXCHANGE_NAME = PREFIX + "command";


  static final String FIRCO_COMMAND_PREFIX = COMMAND_PREFIX + "firco.";
  static final String FIRCO_ACCEPT_ALERT_ROUTING_KEY = FIRCO_COMMAND_PREFIX + "accept-alert";
  static final String FIRCO_RECOMMEND_ALERT_ROUTING_KEY = FIRCO_COMMAND_PREFIX + "recommend-alert";
  static final String FIRCO_REJECT_ALERT_ROUTING_KEY = FIRCO_COMMAND_PREFIX + "reject-alert";

  static final String EVENT_EXCHANGE_NAME = PREFIX + "event";

  static final String EVENT_INTERNAL_EXCHANGE_NAME = PREFIX + "event.internal";

  static final String FIRCO_COMMAND_QUEUE_NAME = PREFIX + "firco.command";
}
