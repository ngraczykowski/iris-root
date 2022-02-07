package com.silenteight.payments.bridge.svb.oldetl.service.impl;

import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;
import com.silenteight.payments.bridge.svb.oldetl.service.AlertedPartyDataFactory;
import com.silenteight.payments.bridge.svb.oldetl.service.ExtractDisposition;

import static com.silenteight.payments.bridge.common.dto.common.CommonTerms.LINE_2;
import static com.silenteight.payments.bridge.common.dto.common.CommonTerms.LINE_3;
import static com.silenteight.payments.bridge.svb.oldetl.response.MessageFieldStructure.NAMEADDRESS_FORMAT_F;


public class ExtractOriginatorBeneFormatFAlertedPartyData implements AlertedPartyDataFactory {

  @Override
  public AlertedPartyData extract(final ExtractDisposition extractDisposition) {

    var hitTag = extractDisposition.getHitTag();
    var messageData = extractDisposition.getMessageData();
    var tagValueLines = messageData.getLines(hitTag);

    return new ExtractFormatFData(NAMEADDRESS_FORMAT_F).extract(
        tagValueLines, LINE_2, LINE_3);
  }
}
