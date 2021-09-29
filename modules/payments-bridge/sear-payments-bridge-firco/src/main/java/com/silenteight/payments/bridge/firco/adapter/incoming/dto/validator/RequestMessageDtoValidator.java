package com.silenteight.payments.bridge.firco.adapter.incoming.dto.validator;

import com.silenteight.payments.bridge.firco.adapter.incoming.dto.input.RequestMessageDto;

public interface RequestMessageDtoValidator {

  void validate(
      RequestMessageDto messageDto, Class<? extends ValidationGroup> validationGroup);
}
