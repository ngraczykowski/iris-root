package com.silenteight.payments.bridge.dto.validator;

import com.silenteight.payments.bridge.dto.input.AlertMessageDto;

public interface AlertMessageDtoValidator {

  void validate(
      AlertMessageDto alertMessageDto, Class<? extends ValidationGroup> validationGroup);
}
