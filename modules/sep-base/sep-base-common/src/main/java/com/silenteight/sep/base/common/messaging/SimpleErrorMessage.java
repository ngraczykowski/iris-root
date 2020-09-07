package com.silenteight.sep.base.common.messaging;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JsonNode;

import java.beans.ConstructorProperties;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

@Data
@RequiredArgsConstructor
public class SimpleErrorMessage {

  private final Message failedMessage;
  private final List<Exception> exceptionChain;
  private final String stackTrace;
  private final String fileName;
  private final Integer lineNumber;

  @Data
  @RequiredArgsConstructor
  public static class Exception {

    private final String type;
    private final String message;
  }

  /**
   * Contains payload in one of two formats: {@link com.google.protobuf.Message} or {@link
   * com.fasterxml.jackson.databind.JsonNode}. Each format is stored in different field of the
   * message. Only one of the payload fields holds the payload value.
   */
  @Data
  public static class Message {

    /**
     * Payload of message in bytes. Subtype of {@link com.google.protobuf.Message}. Can be
     * deserialized using method parseFrom. Null value indicates that jsonPayload holds value in
     * JSON format.
     */
    @Nullable
    private final byte[] protoPayload;
    /**
     * Payload of message as JsonNode. {@link com.fasterxml.jackson.databind.node.NullNode}
     * indicates that protoPayload contains value in Proto Message format.
     */
    private final JsonNode jsonPayload;
    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
    private final Map<String, Object> headers;

    @ConstructorProperties({ "protoPayload", "jsonPayload", "headers" })
    Message(
        @Nullable byte[] protoPayload, JsonNode jsonPayload, Map<String, Object> headers) {
      if (protoPayload == null && jsonPayload.isNull()) {
        throw new IllegalArgumentException(
            "Both arguments protoPayload and jsonPayload are null,"
                + " at least one of them must be set.");
      }
      if (protoPayload != null && !jsonPayload.isNull()) {
        throw new IllegalArgumentException(
            "Both arguments protoPayload and jsonPayload are not null,"
                + " but only one of them can be set.");
      }

      this.protoPayload = protoPayload;
      this.jsonPayload = jsonPayload;
      this.headers = headers;
    }
  }
}
