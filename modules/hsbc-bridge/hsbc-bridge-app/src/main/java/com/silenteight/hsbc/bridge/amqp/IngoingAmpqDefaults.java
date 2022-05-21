package com.silenteight.hsbc.bridge.amqp;

class IngoingAmpqDefaults {

  public static final String BRIDGE_MODEL_EXCHANGE = "bridge.model";

  public static final String MODEL_PROMOTED_QUEUE = "bridge.model-promoted.production";
  public static final String MODEL_PROMOTED_ROUTING_KEY = "event.model-promoted.production";

  public static final String DEAD_LETTER_QUEUE = "bridge.dlq";
  public static final String DEAD_LETTER_EXCHANGE = "bridge.dlx";
  public static final String DEAD_LETTER_ROUTING_KEY = "#";

  public static final String RECOMMENDATIONS_QUEUE = "bridge.recommendations";
  public static final String RECOMMENDATIONS_ROUTING_KEY = "ae.event.recommendations-generated";

  public static final String WORLDCHECK_MODEL_LOADED_QUEUE = "bridge.model.loaded.worldcheck";
  public static final String WORLDCHECK_MODEL_LOADED_ROUTING_KEY = "loaded.worldcheck";


  public static final String HISTORICAL_DECISIONS_MODEL_LOADED_QUEUE =
      "bridge.model.loaded.historical-decisions";
  public static final String HISTORICAL_DECISIONS_MODEL_LOADED_ROUTING_KEY =
      "loaded.historical-decisions";

  public static final String WORLDCHECK_MODEL_PERSISTED_QUEUE =
      "bridge.model.persisted.internal.worldcheck";
  public static final String WORLDCHECK_MODEL_PERSISTED_ROUTING_KEY =
      "persisted.internal.worldcheck";

  public static final String HISTORICAL_DECISIONS_MODEL_PERSISTED_QUEUE =
      "bridge.model.persisted.internal.historical-decisions";
  public static final String HISTORICAL_DECISIONS_MODEL_PERSISTED_ROUTING_KEY=
      "persisted.internal.historical-decisions";
}
