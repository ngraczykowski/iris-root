package com.silenteight.payments.bridge.dto.validator;

import com.silenteight.payments.bridge.dto.input.RequestMessageDto;

public interface RequestMessageDtoValidator {

  void validate(
      RequestMessageDto messageDto, Class<? extends ValidationGroup> validationGroup);
}
