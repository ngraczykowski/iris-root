package com.silenteight.sep.base.common.messaging;

import com.silenteight.sep.base.common.messaging.SimpleErrorMessage.Exception;
import com.silenteight.sep.base.common.messaging.SimpleErrorMessage.Message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;
import com.google.common.base.Throwables;
import org.springframework.integration.transformer.GenericTransformer;
import org.springframework.messaging.support.ErrorMessage;
import org.springframework.messaging.support.MessageBuilder;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class ErrorMessageTransformer implements
    GenericTransformer<ErrorMessage, org.springframework.messaging.Message<SimpleErrorMessage>> {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public org.springframework.messaging.Message<SimpleErrorMessage> transform(ErrorMessage source) {
    SimpleErrorMessage message = createSimpleErrorMessage(source);

    List<Exception> exceptionChain = message.getExceptionChain();
    Exception mostSpecificException = exceptionChain.get(exceptionChain.size() - 1);

    return MessageBuilder.withPayload(message)
        .setHeader("exceptionType", mostSpecificException.getType())
        .setHeader("exceptionMessage", mostSpecificException.getMessage())
        .build();
  }

  private SimpleErrorMessage createSimpleErrorMessage(ErrorMessage source) {
    List<Exception> exceptions = extractExceptions(source.getPayload());
    String stackTrace = Throwables.getStackTraceAsString(source.getPayload());
    return new SimpleErrorMessage(
        createMessage(source.getOriginalMessage()), exceptions, stackTrace);
  }

  private List<Exception> extractExceptions(Throwable throwable) {
    return Throwables.getCausalChain(throwable).stream()
        .map(t -> new Exception(t.getClass().getName(), t.getMessage()))
        .collect(Collectors.toList());
  }

  private Message createMessage(org.springframework.messaging.Message<?> source) {
    if (source.getPayload() instanceof com.google.protobuf.Message)
      return new Message(((com.google.protobuf.Message) source.getPayload()).toByteArray(),
          NullNode.getInstance(), Map.copyOf(source.getHeaders()));

    return new Message(
        null, objectMapper.valueToTree(source.getPayload()), Map.copyOf(source.getHeaders()));
  }
}
