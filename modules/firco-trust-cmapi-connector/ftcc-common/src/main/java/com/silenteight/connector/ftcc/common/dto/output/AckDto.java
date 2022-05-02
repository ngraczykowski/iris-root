package com.silenteight.connector.ftcc.common.dto.output;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.experimental.NonFinal;
import lombok.extern.jackson.Jacksonized;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.UpperCamelCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.Map;

@Value
@Builder
@Jacksonized
@JsonNaming(UpperCamelCaseStrategy.class)
@NonFinal
public class AckDto {

  transient Map<String, Object> header;

  @JsonProperty("Body")
  AckBodyDto requestBodyDto;

  public static AckDto ok() {
    return AckDto.builder()
        .requestBodyDto(AckBodyDto.builder()
            .messageDto(AckMessageDto.builder()
                .faultCode(FaultCode.NONE.getCode())
                .faultString("OK")
                .build())
            .build())
        .build();
  }

  @RequiredArgsConstructor
  public enum FaultCode {
    NONE("0");

    @Getter
    private final String code;
  }
}
