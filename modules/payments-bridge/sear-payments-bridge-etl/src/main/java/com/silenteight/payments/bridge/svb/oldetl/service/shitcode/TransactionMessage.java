package com.silenteight.payments.bridge.svb.oldetl.service.shitcode;

import java.util.List;
import java.util.Optional;

public interface TransactionMessage {

  Optional<String> getAccountNumber(String tag);

  List<String> getAllMatchingTexts(String tag, String matchingText);

  String getHitTagValue(String tag);

  List<String> getAllMatchingTagValues(String tag, String matchingText);
}
