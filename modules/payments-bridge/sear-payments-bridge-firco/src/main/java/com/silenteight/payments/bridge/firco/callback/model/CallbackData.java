package com.silenteight.payments.bridge.firco.callback.model;

import lombok.RequiredArgsConstructor;
import lombok.Value;

import com.silenteight.payments.bridge.common.dto.common.StatusInfoDto;
import com.silenteight.payments.bridge.common.dto.input.AlertMessageDto;

@Value
@RequiredArgsConstructor
public class CallbackData {
  AlertMessageDto alertMessageDto;
  StatusInfoDto decision;
}
