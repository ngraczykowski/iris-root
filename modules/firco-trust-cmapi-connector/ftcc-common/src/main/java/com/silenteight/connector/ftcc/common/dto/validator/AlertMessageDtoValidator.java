package com.silenteight.connector.ftcc.common.dto.validator;


import com.silenteight.connector.ftcc.common.dto.input.AlertMessageDto;

public interface AlertMessageDtoValidator {

  void validate(
      AlertMessageDto alertMessageDto, Class<? extends ValidationGroup> validationGroup);
}
