package com.silenteight.rabbitcommonschema.definitions;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RabbitConstants {

  public static final String AE_COMMAND_EXCHANGE = "ae.command";
  public static final String AE_EVENT_EXCHANGE = "ae.event";
  public static final String AGENT_REQUEST_EXCHANGE = "agent.request";
  public static final String AGENT_RESPONSE_EXCHANGE = "agent.response";
  public static final String AUDIT_EVENT_EXCHANGE = "audit.event";
  public static final String BRIDGE_COMMAND_EXCHANGE = "bridge.command";
  public static final String BRIDGE_RETENTION_EXCHANGE = "bridge.retention";
  public static final String BRIDGE_LEARNING_ENGINE_EXCHANGE = "bridge.historical";
  public static final String BRIDGE_LEARNING_ENGINE_IS_PEP_EXCHANGE = "bridge.ispep";
  public static final String BRIDGE_LEARNING_ENGINE_MODEL_EXCHANGE = "bridge.model";
  public static final String CONNECTOR_COMMAND_EXCHANGE = "connector.command";
  public static final String CONNECTOR_EVENT_EXCHANGE = "connector.event";
  public static final String GOV_EVENT_EXCHANGE = "gov.event";
  public static final String GOV_EVENTS_EXCHANGE = "gov.events";
  public static final String GOV_QA_EXCHANGE = "gov.qa";
  public static final String SIM_COMMAND_EXCHANGE = "sim.command";
  public static final String WH_EVENT_EXCHANGE = "wh.event";
}
