package com.silenteight.searpayments.scb.request;

import com.silenteight.tsaas.bridge.domain.Alert;
import lombok.NonNull;
import lombok.Value;

import javax.annotation.Nullable;

interface PrevalidateAlertStrategy {

  @NonNull PrevalidateResult validate(@NonNull Alert alert);

  @Value
  class PrevalidateResult {

    boolean valid;
    @Nullable ReasonForInvalid reasonIfInvalid;

    static PrevalidateResult ok() {
      return new PrevalidateResult(true, null);
    }

    static PrevalidateResult invalid(String code, String description) {
      return new PrevalidateResult(false, new ReasonForInvalid(code, description));
    }
  }

  @Value
  class ReasonForInvalid {
    String code;
    String description;
  }

}
