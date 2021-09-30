package com.silenteight.payments.bridge.common.dto.validator;

import com.silenteight.payments.bridge.common.dto.input.RequestMessageDto;

public interface RequestMessageDtoValidator {

  void validate(
      RequestMessageDto messageDto, Class<? extends ValidationGroup> validationGroup);
}
