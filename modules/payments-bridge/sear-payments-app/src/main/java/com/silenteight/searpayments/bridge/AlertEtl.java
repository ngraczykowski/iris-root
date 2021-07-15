package com.silenteight.searpayments.bridge;

import com.silenteight.searpayments.bridge.dto.input.AlertDataCenterDto;
import lombok.NonNull;

import com.silenteight.searpayments.bridge.dto.input.AlertMessageDto;

public interface AlertEtl {

  long invoke(@NonNull AlertDataCenterDto AlertDataCenterDto);
}
