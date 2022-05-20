package com.silenteight.payments.bridge.svb.oldetl.model;

import lombok.Builder;
import lombok.Value;

import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;
import com.silenteight.payments.bridge.svb.oldetl.response.MessageFieldStructure;

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
