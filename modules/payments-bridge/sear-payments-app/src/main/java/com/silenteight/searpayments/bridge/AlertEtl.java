package com.silenteight.searpayments.bridge;

import lombok.NonNull;

import com.silenteight.searpayments.bridge.dto.input.AlertDataCenterDto;

public interface AlertEtl {

  long invoke(@NonNull AlertDataCenterDto alertDataCenterDto);
}
