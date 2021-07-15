package com.silenteight.searpayments.bridge.dto.validator;

import com.silenteight.searpayments.bridge.dto.input.AlertMessageDto;

public interface AlertMessageDtoValidator {

  void validate(
      AlertMessageDto alertMessageDto, Class<? extends ValidationGroup> validationGroup);
}
