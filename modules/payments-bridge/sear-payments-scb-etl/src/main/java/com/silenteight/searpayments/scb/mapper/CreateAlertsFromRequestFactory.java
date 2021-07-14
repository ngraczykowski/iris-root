package com.silenteight.searpayments.scb.mapper;


import com.silenteight.searpayments.bridge.dto.input.RequestDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateAlertsFromRequestFactory {

  @NonNull private final CreateAlertFromMessageFactory createAlertFromMessageFactory;

  public CreateAlertsFromRequest create(RequestDto requestDto) {
    return new CreateAlertsFromRequestImpl(requestDto, createAlertFromMessageFactory);
  }
}
