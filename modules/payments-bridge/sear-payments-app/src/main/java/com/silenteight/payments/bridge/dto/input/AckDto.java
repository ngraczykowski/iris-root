package com.silenteight.payments.bridge.dto.input;

import lombok.*;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AckDto implements Serializable {

  private static final long serialVersionUID = -8309071436620390430L;

  @JsonProperty("Header")
  private String header;

  @JsonProperty("Body")
  private AckBodyDto requestBodyDto;

  public static AckDto ok() {
    return of(200, "OK");
  }

  public static AckDto error() {
    return of(500, "ERROR");
  }

  public static AckDto of(int faultCode, String faultString) {
    AckMessageDto messageDto = new AckMessageDto(String.valueOf(faultCode), faultString, "");
    AckBodyDto bodyDto = new AckBodyDto(messageDto);
    return new AckDto(null, bodyDto);
  }

  @RequiredArgsConstructor
  @Getter
  @ToString
  static class AckBodyDto implements Serializable {

    private static final long serialVersionUID = 8305008907553975177L;
    @JsonProperty("msg_Acknowledgement")
    private final AckMessageDto messageDto;
  }

  @RequiredArgsConstructor
  @Getter
  @ToString
  static class AckMessageDto implements Serializable {

    private static final long serialVersionUID = -9036091619375056784L;
    @JsonProperty("faultcode")
    private final String faultCode;

    @JsonProperty("faultstring")
    private final String faultString;

    @JsonProperty("faultactor")
    private final String faultActor;
  }
}
