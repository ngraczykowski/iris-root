package com.silenteight.hsbc.bridge.grpc;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.RecommendationsGenerated;
import com.silenteight.data.api.v1.ProductionDataIndexRequest;
import com.silenteight.hsbc.bridge.amqp.MessageRegistry;

import com.google.protobuf.*;
import com.google.protobuf.Enum;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.apache.commons.lang3.StringUtils.isEmpty;

@Slf4j
class ProtoMessageRegistry implements MessageRegistry {

  private final Map<String, Parser<? extends Message>> registry = new HashMap<>();

  ProtoMessageRegistry() {
    registerKnownTypes();
  }

  @Override
  public Optional<Parser<Message>> findParser(String typeName) {
    if (isEmpty(typeName) || !registry.containsKey(typeName)) {
      return empty();
    }

    var parser = (Parser<Message>) registry.get(typeName);
    return of(parser);
  }

  private void registerMessageType(GeneratedMessageV3 message) {
    var descriptor = message.getDescriptorForType();
    var parser = message.getParserForType();

    registry.put(descriptor.getFullName(), parser);
  }

  private void registerKnownTypes() {
    List.of(
        Any.getDefaultInstance(),
        Empty.getDefaultInstance(),
        Enum.getDefaultInstance(),
        ProductionDataIndexRequest.getDefaultInstance(),
        RecommendationsGenerated.getDefaultInstance()
    ).forEach(this::registerMessageType);
  }
}
