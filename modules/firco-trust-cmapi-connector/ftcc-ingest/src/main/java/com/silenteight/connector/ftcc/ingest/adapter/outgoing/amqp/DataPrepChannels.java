package com.silenteight.connector.ftcc.ingest.adapter.outgoing.amqp;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DataPrepChannels {

  public static final String DATA_PREP_GATEWAY_CHANNEL = "dataPrepGatewayChannel";

  public static final String DATA_PREP_OUTBOUND_CHANNEL = "dataPrepOutboundChannel";
}
