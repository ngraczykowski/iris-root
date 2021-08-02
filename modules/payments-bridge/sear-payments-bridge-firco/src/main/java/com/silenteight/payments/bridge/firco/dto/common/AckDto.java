package com.silenteight.payments.bridge.firco.dto.common;

import lombok.*;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Map;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AckDto implements Serializable {

  private static final long serialVersionUID = -8309071436620390430L;

  @JsonProperty("Header")
  private Map<String, Object> header;

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
