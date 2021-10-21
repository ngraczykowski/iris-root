package com.silenteight.payments.bridge.svb.oldetl.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.etl.processing.model.MessageData;
import com.silenteight.payments.bridge.svb.oldetl.port.ExtractAlertedPartyDataUseCase;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;
import com.silenteight.payments.bridge.svb.oldetl.service.shitcode.ExtractGfxAlertedPartyData;
import com.silenteight.payments.bridge.svb.oldetl.service.shitcode.ExtractGtexAlertedPartyData;
import com.silenteight.payments.bridge.svb.oldetl.service.shitcode.ExtractPepAlertedPartyData;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
class ExtractAlertedPartyDataService implements ExtractAlertedPartyDataUseCase {

  @Override
  public AlertedPartyData extractAlertedPartyData(
      String applicationCode, MessageData messageData, String hitTag) {

    switch (applicationCode) {
      case "GFX":
        return new ExtractGfxAlertedPartyData(messageData, hitTag).extract();
      case "PEP":
        return new ExtractPepAlertedPartyData(messageData).extract();
      case "GTEX":
        return new ExtractGtexAlertedPartyData(messageData).extract();
      default:
        throw new IllegalArgumentException("Application not supported " + applicationCode);
    }
  }
}
