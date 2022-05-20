package com.silenteight.adjudication.engine.common.protobuf;

import com.silenteight.sep.base.common.protocol.MessageRegistryFactory;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.protobuf.Struct;
import com.google.protobuf.Value;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class ProtoMessageToObjectNodeConverterTest {

  @Test
  void shouldConvertProtoMessage() {
    var messageRegistry = new MessageRegistryFactory("com.google.protobuf").create();
    var protoMessageToObjectNodeConverter = new ProtoMessageToObjectNodeConverter(messageRegistry);

    UUID randomId = UUID.randomUUID();

    Struct protoMessage = Struct.newBuilder()
        .putFields("randomId", Value.newBuilder().setStringValue(randomId.toString()).build())
        .build();

    Optional<ObjectNode> objectNode = protoMessageToObjectNodeConverter.convert(protoMessage);

    assertThat(objectNode).hasValueSatisfying(r -> {
      assertThat(r.requiredAt("/randomId").asText()).isEqualTo(randomId.toString());
    });
  }
}
