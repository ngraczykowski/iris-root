package com.silenteight.connector.ftcc.firco.dto.validator;


import com.silenteight.connector.ftcc.firco.dto.input.AlertMessageDto;

public interface AlertMessageDtoValidator {

  void validate(
      AlertMessageDto alertMessageDto, Class<? extends ValidationGroup> validationGroup);
}
