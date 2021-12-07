package com.silenteight.registration.api.library.v1.messages;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class MessageAlertMatchesFeatureInputFed {

  String batchId;
  String alertId;
  List<FedMatchOut> fedMatches;
}
