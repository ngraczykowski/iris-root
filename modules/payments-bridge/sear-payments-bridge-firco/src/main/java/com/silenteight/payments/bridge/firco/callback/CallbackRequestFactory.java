package com.silenteight.payments.bridge.firco.callback;

import lombok.NonNull;

import com.silenteight.payments.bridge.common.dto.output.ClientRequestDto;

interface CallbackRequestFactory {

  CallbackRequest create(@NonNull ClientRequestDto clientRequestDto);
}
