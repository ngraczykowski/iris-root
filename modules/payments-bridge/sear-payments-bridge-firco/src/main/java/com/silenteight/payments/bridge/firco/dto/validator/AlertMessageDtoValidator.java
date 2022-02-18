package com.silenteight.payments.bridge.firco.dto.validator;

import com.silenteight.payments.bridge.firco.dto.input.AlertMessageDto;

public interface AlertMessageDtoValidator {

  void validate(
      AlertMessageDto alertMessageDto, Class<? extends ValidationGroup> validationGroup);
}
