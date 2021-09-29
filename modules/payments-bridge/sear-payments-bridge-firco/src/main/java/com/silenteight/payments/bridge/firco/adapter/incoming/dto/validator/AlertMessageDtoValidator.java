package com.silenteight.payments.bridge.firco.adapter.incoming.dto.validator;

import com.silenteight.payments.bridge.firco.adapter.incoming.dto.input.AlertMessageDto;

public interface AlertMessageDtoValidator {

  void validate(
      AlertMessageDto alertMessageDto, Class<? extends ValidationGroup> validationGroup);
}
