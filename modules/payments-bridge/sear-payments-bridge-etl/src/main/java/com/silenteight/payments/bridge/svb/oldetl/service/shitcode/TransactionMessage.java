package com.silenteight.payments.bridge.svb.oldetl.service.shitcode;

import java.util.List;
import java.util.Optional;

public interface TransactionMessage {

  Optional<String> getAccountNumber();

  List<String> getAllMatchingTexts(String tag);

  String getHitTagValue(String tag);

  List<String> getAllMatchingTagValues(String tag, String matchingText);
}
