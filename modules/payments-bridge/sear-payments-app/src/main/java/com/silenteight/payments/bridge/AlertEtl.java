package com.silenteight.payments.bridge;

import lombok.NonNull;

import com.silenteight.payments.bridge.dto.input.AlertDataCenterDto;

public interface AlertEtl {

  long invoke(@NonNull AlertDataCenterDto alertDataCenterDto);
}
