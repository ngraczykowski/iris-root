package com.silenteight.searpayments.scb.etl.response;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class AlertEtlResponse {

  String systemId;
  String messageId;
  String messageType;
  String applicationCode;
  String messageFormat;
  String messageData;
  String businessUnit;
  String unit;
  String senderReference;
  String ioIndicator;
  AlertStatus currentStatus;
  String countryCode;
  List<HitData> hits;

  @Value
  public static class AlertStatus {

    String id;
    String name;
    String checksum;
    String routingCode;
  }
}
