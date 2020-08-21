package com.silenteight.sep.base.common.messaging;

import lombok.Data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;
import com.google.protobuf.StringValue;
import org.apache.groovy.util.Maps;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.ErrorMessage;
import org.springframework.messaging.support.MessageBuilder;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class ErrorMessageTransformerTest {

  private final ObjectMapper objectMapper = new ObjectMapper();
  private final ErrorMessageTransformer transformer = new ErrorMessageTransformer();

  @Test
  public void transformProtoMessage() {
    StringValue protoMessagePayload = StringValue.of("string");
    ErrorMessage errorMessage = buildErrorMessage(protoMessagePayload);

    Message<SimpleErrorMessage> result = transformer.transform(errorMessage);

    assertThat(result.getPayload().getExceptionChain().size()).isOne();
    assertThat(result.getPayload().getExceptionChain().get(0).getMessage())
        .isEqualTo("Test message");
    assertThat(result.getPayload().getExceptionChain().get(0).getType())
        .isEqualTo(IllegalStateException.class.getName());
    assertThat(result.getPayload().getStackTrace()).isNotBlank();
    assertThat(result.getPayload().getFailedMessage().getProtoPayload())
        .isEqualTo(protoMessagePayload.toByteArray());
    assertThat(result.getPayload().getFailedMessage().getJsonPayload())
        .isEqualTo(NullNode.getInstance());
    assertThat(result.getPayload().getFailedMessage().getHeaders())
        .containsAllEntriesOf(Maps.of("originalMessageKey1", "originalMessageValue1"));
  }

  @Test
  public void transformNonProtoMessage() {
    TestObject testObjectPayload = new TestObject("text", 1);
    ErrorMessage errorMessage = buildErrorMessage(testObjectPayload);

    Message<SimpleErrorMessage> result = transformer.transform(errorMessage);

    assertThat(result.getPayload().getExceptionChain().size()).isOne();
    assertThat(result.getPayload().getExceptionChain().get(0).getMessage())
        .isEqualTo("Test message");
    assertThat(result.getPayload().getExceptionChain().get(0).getType())
        .isEqualTo(IllegalStateException.class.getName());
    assertThat(result.getPayload().getStackTrace()).isNotBlank();
    assertThat(result.getPayload().getFailedMessage().getProtoPayload()).isNull();
    assertThat(result.getPayload().getFailedMessage().getJsonPayload())
        .isEqualTo(new ObjectMapper().valueToTree(testObjectPayload));
    assertThat(result.getPayload().getFailedMessage().getHeaders())
        .containsAllEntriesOf(Maps.of("originalMessageKey1", "originalMessageValue1"));
  }

  @Test
  public void transformAndConvertNonProtoMessageToJson() throws JsonProcessingException {
    TestObject testObjectPayload = new TestObject("text", 1);
    ErrorMessage errorMessage = buildErrorMessage(testObjectPayload);

    Message<SimpleErrorMessage> result = transformer.transform(errorMessage);
    String resultJson = objectMapper.writeValueAsString(result.getPayload());
    SimpleErrorMessage deserialized = objectMapper.readValue(resultJson, SimpleErrorMessage.class);

    assertThat(result.getPayload()).isEqualTo(deserialized);
  }

  @Test
  public void transformAndConvertProtoMessageToJson() throws JsonProcessingException {
    StringValue protoMessagePayload = StringValue.of("string");
    ErrorMessage errorMessage = buildErrorMessage(protoMessagePayload);

    Message<SimpleErrorMessage> result = transformer.transform(errorMessage);
    String resultJson = objectMapper.writeValueAsString(result.getPayload());
    SimpleErrorMessage deserialized = objectMapper.readValue(resultJson, SimpleErrorMessage.class);

    assertThat(result.getPayload()).isEqualTo(deserialized);
  }

  @NotNull
  private <T> ErrorMessage buildErrorMessage(T payload) {
    Throwable throwable =
        catchThrowable(() -> {
          throw new IllegalStateException("Test message");
        });
    return new ErrorMessage(
        throwable,
        Map.of("errorMessageKey1", "errorMessageValue1"),
        MessageBuilder
            .withPayload(payload)
            .setHeader("originalMessageKey1", "originalMessageValue1")
            .build());
  }

  @Data
  private static class TestObject {

    private final String str;
    private final int num;
  }
}
