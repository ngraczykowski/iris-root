package com.silenteight.payments.bridge.svb.oldetl.service.shitcode;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.etl.processing.model.MessageData;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;

@RequiredArgsConstructor
public class ExtractPepAlertedPartyData {

  private final MessageData messageData;

  // Wiktor
  public AlertedPartyData extract() {
    return null;
  }
}
