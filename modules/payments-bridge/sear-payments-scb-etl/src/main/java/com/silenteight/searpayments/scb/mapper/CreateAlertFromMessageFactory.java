package com.silenteight.searpayments.scb.mapper;

import com.silenteight.searpayments.bridge.dto.input.RequestMessageDto;
import com.silenteight.searpayments.bridge.dto.validator.RequestMessageDtoValidator;
import com.silenteight.searpayments.scb.etl.countrycode.CountryCodeExtractor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class CreateAlertFromMessageFactory {

  @NonNull private final CreateHitsFactory createHitsFactory;
  @NonNull private final CreateMessageTypeFactory createMessageTypeFactory;
  @NonNull private final CountryCodeExtractor countryCodeExtractor;
  @NonNull private final CreateBasicAlertFactory createBasicAlertFactory;
  @NonNull private final String gitCommitId;
  @NonNull private final RequestMessageDtoValidator requestMessageDtoValidator;

  CreateAlertFromMessage create(RequestMessageDto messageDto, String dataCenter) {
    return new CreateAlertFromMessage(
        messageDto, dataCenter, createHitsFactory, createMessageTypeFactory,
        createBasicAlertFactory, gitCommitId, requestMessageDtoValidator, countryCodeExtractor);
  }
}
