package com.silenteight.searpayments.bridge;

import lombok.NonNull;

import com.silenteight.searpayments.bridge.dto.input.AlertMessageDto;

public interface AlertEtl {

  long invoke(@NonNull AlertMessageDto alertDto);
}
