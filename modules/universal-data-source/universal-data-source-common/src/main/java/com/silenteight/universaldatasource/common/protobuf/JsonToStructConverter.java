package com.silenteight.universaldatasource.common.protobuf;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message.Builder;
import com.google.protobuf.util.JsonFormat;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonToStructConverter {

  public static void map(Builder builder, String json) {
    try {
      JsonFormat.parser().merge(json, builder);
    } catch (InvalidProtocolBufferException e) {
      throw new JsonStructConversionException("Cannot convert agent feature input to Struct", e);
    }
  }

  private static class JsonStructConversionException extends RuntimeException {

    private static final long serialVersionUID = -204330995574517285L;

    JsonStructConversionException(String message, Throwable cause) {
      super(message, cause);
    }
  }
}
