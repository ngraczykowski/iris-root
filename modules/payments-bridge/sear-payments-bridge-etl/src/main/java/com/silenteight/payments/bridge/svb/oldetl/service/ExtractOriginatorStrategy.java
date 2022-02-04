package com.silenteight.payments.bridge.svb.oldetl.service;

import com.silenteight.payments.bridge.svb.oldetl.model.UnsupportedMessageException;
import com.silenteight.payments.bridge.svb.oldetl.service.impl.*;

import javax.annotation.Nonnull;

import static com.silenteight.payments.bridge.svb.oldetl.util.CommonTerms.*;

public final class ExtractOriginatorStrategy {


  public static AlertedPartyDataFactory choose(
      final String hitTag, final boolean tagValueInFormatF) {
    switch (hitTag) {
      case TAG_ORIGINATOR:
        return determineExtractorByTagOriginator(tagValueInFormatF);
      case TAG_BENE:
        return determineExtractorByTagBene(tagValueInFormatF);
      case TAG_ORGBANK:
      case TAG_INSBANK:
        return new ExtractBeneOrgbankInsbankAlertedPartyData();
      case TAG_50F:
        return new Extract50FAlertedPartyData();
      case TAG_RECEIVBANK:
        return new ExtractReceivbankAlertedPartyData();
      case TAG_50K:
      case TAG_59:
      case TAG_50:
        return new Extract50k59AlertedPartyData();
      default:
        throw new UnsupportedMessageException("Tag not supported " + hitTag);
    }
  }

  @Nonnull
  private static AlertedPartyDataFactory determineExtractorByTagOriginator(
      final boolean tagValueInFormatF) {
    if (tagValueInFormatF) {
      return new ExtractOriginatorBeneFormatFAlertedPartyData();
    } else {
      return new ExtractOriginatorAlertedPartyData();
    }
  }

  @Nonnull
  private static AlertedPartyDataFactory determineExtractorByTagBene(
      final boolean tagValueInFormatF) {
    if (tagValueInFormatF) {
      return new ExtractOriginatorBeneFormatFAlertedPartyData();
    } else {
      return new ExtractBeneOrgbankInsbankAlertedPartyData();
    }
  }


}


