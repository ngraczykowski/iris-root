package com.silenteight.searpayments.scb.mapper;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.dto.input.AlertMessageDto;
import com.silenteight.payments.bridge.dto.validator.AlertMessageDtoValidator;
import com.silenteight.searpayments.scb.etl.countrycode.CountryCodeExtractor;

@RequiredArgsConstructor
public class CreateAlertFactory {

  @NonNull private final CreateHitsFactory createHitsFactory;
  @NonNull private final CreateMessageTypeFactory createMessageTypeFactory;
  @NonNull private final CountryCodeExtractor countryCodeExtractor;
  @NonNull private final CreateBasicAlertFactory createBasicAlertFactory;
  @NonNull private final String gitCommitId;
  @NonNull private final AlertMessageDtoValidator alertMessageDtoValidator;

  public CreateAlert create(AlertMessageDto alertMessageDto, String dataCenter) {
    return new CreateAlertImpl(
        alertMessageDto, dataCenter, createHitsFactory, createMessageTypeFactory,
        createBasicAlertFactory, gitCommitId, alertMessageDtoValidator, countryCodeExtractor);
  }
}
