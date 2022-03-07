package com.silenteight.connector.ftcc.ingest.dto.validator;


import com.silenteight.connector.ftcc.ingest.dto.input.RequestMessageDto;

public interface RequestMessageDtoValidator {

  void validate(
      RequestMessageDto messageDto, Class<? extends ValidationGroup> validationGroup);
}
