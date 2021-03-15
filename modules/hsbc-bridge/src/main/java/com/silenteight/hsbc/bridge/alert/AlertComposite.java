package com.silenteight.hsbc.bridge.alert;

import lombok.Builder;
import lombok.Value;

import com.silenteight.hsbc.bridge.rest.model.input.Alert;

@Builder
@Value
public class AlertComposite {

  long id;
  Alert alert;
}
