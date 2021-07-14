package com.silenteight.simulator.processing.alert.index.feed;

import lombok.NonNull;

import com.silenteight.adjudication.api.v1.Alert;

public interface AlertService {

  Alert getAlert(@NonNull String alertName);
}
