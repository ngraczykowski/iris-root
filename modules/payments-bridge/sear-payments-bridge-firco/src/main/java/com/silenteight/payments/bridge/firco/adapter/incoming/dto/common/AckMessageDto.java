package com.silenteight.payments.bridge.firco.adapter.incoming.dto.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@RequiredArgsConstructor
@Getter
@ToString
class AckMessageDto implements Serializable {

  private static final long serialVersionUID = -9036091619375056784L;

  @JsonProperty("faultcode")
  private final String faultCode;

  @JsonProperty("faultstring")
  private final String faultString;

  @JsonProperty("faultactor")
  private final String faultActor;
}
