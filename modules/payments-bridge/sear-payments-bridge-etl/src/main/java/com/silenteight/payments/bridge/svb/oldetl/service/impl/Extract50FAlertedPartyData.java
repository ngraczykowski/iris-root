package com.silenteight.payments.bridge.svb.oldetl.service.impl;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;
import com.silenteight.payments.bridge.svb.oldetl.service.AlertedPartyDataFactory;
import com.silenteight.payments.bridge.svb.oldetl.service.ExtractDisposition;

import static com.silenteight.payments.bridge.common.dto.common.CommonTerms.LINE_1;
import static com.silenteight.payments.bridge.common.dto.common.CommonTerms.LINE_2;
import static com.silenteight.payments.bridge.svb.oldetl.response.MessageFieldStructure.NAMEADDRESS_FORMAT_F;

@RequiredArgsConstructor
public class Extract50FAlertedPartyData implements AlertedPartyDataFactory {


  @Override
  public AlertedPartyData extract(final ExtractDisposition extractDisposition) {
    var hitTag = extractDisposition.getHitTag();
    var tagValueLines = extractDisposition.getMessageData().getLines(hitTag);

    return new ExtractFormatFData(NAMEADDRESS_FORMAT_F).extract(tagValueLines, LINE_1, LINE_2);
  }
}
