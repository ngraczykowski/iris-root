package com.silenteight.payments.bridge.svb.etl.model;

import lombok.Builder;
import lombok.Value;

import com.silenteight.payments.bridge.svb.etl.response.AlertedPartyData;
import com.silenteight.payments.bridge.svb.etl.response.MessageFieldStructure;

import java.util.List;

@Value
@Builder
public class CreateAlertedPartyEntitiesRequest {

  List<String> allMatchingText;
  AlertedPartyData alertedPartyData;

  public MessageFieldStructure getMessageFieldStructure() {
    return alertedPartyData.getMessageFieldStructure();
  }
}
