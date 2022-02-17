package com.silenteight.payments.bridge.firco.datasource.model;

import lombok.Builder;
import lombok.Value;

import com.silenteight.payments.bridge.common.dto.common.StatusInfoDto;
import com.silenteight.payments.bridge.svb.oldetl.response.HitData;

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
  StatusInfoDto currentStatus;
  String countryCode;
  List<HitData> hits;
}
