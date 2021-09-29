package com.silenteight.payments.bridge.firco.adapter.incoming.dto.common;

import lombok.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy.UpperCamelCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.io.Serializable;
import java.util.Map;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonNaming(UpperCamelCaseStrategy.class)
public class AckDto implements Serializable {

  private static final long serialVersionUID = -8309071436620390430L;

  private transient Map<String, Object> header;

  @JsonProperty("Body")
  private AckBodyDto requestBodyDto;

  public static AckDto ok() {
    return of(FaultCode.NONE, "");
  }

  public static AckDto clientError(String message) {
    return of(FaultCode.CLIENT, message);
  }

  public static AckDto serverError(String message) {
    return of(FaultCode.SERVER, message);
  }

  public static AckDto of(FaultCode faultCode, String faultString) {
    AckMessageDto messageDto = new AckMessageDto(faultCode.getCode(), faultString, "");
    AckBodyDto bodyDto = new AckBodyDto(messageDto);
    return new AckDto(null, bodyDto);
  }

  @RequiredArgsConstructor
  public enum FaultCode {
    NONE(""),
    CLIENT("soapenv:Client"),
    SERVER("soapenv:Server");

    @Getter
    private final String code;
  }
}
