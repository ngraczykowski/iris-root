package com.silenteight.connector.ftcc.ingest.dto.validator;


import com.silenteight.connector.ftcc.ingest.dto.input.AlertMessageDto;

public interface AlertMessageDtoValidator {

  void validate(
      AlertMessageDto alertMessageDto, Class<? extends ValidationGroup> validationGroup);
}
