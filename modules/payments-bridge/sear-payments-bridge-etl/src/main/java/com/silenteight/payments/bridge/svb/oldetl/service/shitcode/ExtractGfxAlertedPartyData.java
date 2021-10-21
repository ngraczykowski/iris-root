package com.silenteight.payments.bridge.svb.oldetl.service.shitcode;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.common.dto.input.HitDto;
import com.silenteight.payments.bridge.etl.processing.model.MessageData;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;

@RequiredArgsConstructor
public class ExtractGfxAlertedPartyData {

  private final MessageData messageData;
  private final HitDto hit;

  public AlertedPartyData extract() {
    return null;
  }
}
