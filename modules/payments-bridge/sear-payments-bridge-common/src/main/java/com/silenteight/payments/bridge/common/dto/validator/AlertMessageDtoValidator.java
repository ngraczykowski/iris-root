package com.silenteight.payments.bridge.common.dto.validator;

import com.silenteight.payments.bridge.common.dto.input.AlertMessageDto;

public interface AlertMessageDtoValidator {

  void validate(
      AlertMessageDto alertMessageDto, Class<? extends ValidationGroup> validationGroup);
}
