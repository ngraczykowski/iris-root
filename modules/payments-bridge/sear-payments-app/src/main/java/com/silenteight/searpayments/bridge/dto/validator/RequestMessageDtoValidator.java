package com.silenteight.searpayments.bridge.dto.validator;

import com.silenteight.tsaas.bridge.dto.input.RequestMessageDto;

public interface RequestMessageDtoValidator {

  void validate(
      RequestMessageDto messageDto, Class<? extends ValidationGroup> validationGroup);
}
