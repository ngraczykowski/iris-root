package com.silenteight.payments.bridge.firco.callback.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.common.dto.output.ClientRequestDto;

import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
public class CallbackRequestFactoryImpl implements CallbackRequestFactory {

  private final String endpoint;
  private final RestTemplate restTemplate;

  public CallbackRequest create(@NonNull ClientRequestDto clientRequestDto) {
    return new CallbackRequestImpl(endpoint, restTemplate, clientRequestDto);
  }
}
