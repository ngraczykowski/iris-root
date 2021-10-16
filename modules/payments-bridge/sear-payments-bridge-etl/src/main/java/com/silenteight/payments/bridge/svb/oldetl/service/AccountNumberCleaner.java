package com.silenteight.payments.bridge.svb.oldetl.service;

import lombok.NonNull;

class AccountNumberCleaner {

  private AccountNumberCleaner() {

  }

  @NonNull
  static String clean(@NonNull String accNo, @NonNull String tag) {
    String accountNumber = accNo.trim();
    String trimmedTag = tag.trim();
    if ("CHP_502".equals(trimmedTag)) {
      accountNumber = accountNumber.replaceAll("^(D|F)", "");
    } else if ("MTS_BPI".equals(trimmedTag)) {
      accountNumber = accountNumber.replaceAll("\\(.*?\\)", "");
    }
    return accountNumber.replaceAll("(\\W\r|\n|[-\\s/(/)/.,])", "")
        .trim().toUpperCase();

  }
}
