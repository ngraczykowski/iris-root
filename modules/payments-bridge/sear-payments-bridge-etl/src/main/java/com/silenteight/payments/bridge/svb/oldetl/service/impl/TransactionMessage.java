package com.silenteight.payments.bridge.svb.oldetl.service.impl;

import java.util.List;

public interface TransactionMessage {

  List<String> getAllMatchingTexts(String tag, String matchingText);

  String getHitTagValue(String tag);

  List<String> getAllMatchingTagValues(String tag, String matchingText);
}
