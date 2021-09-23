package protobuf;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.silenteight.sep.base.common.protocol.MessageRegistry;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class ProtoMessageToObjectNodeConverter {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  private final MessageRegistry messageRegistry;

  public Optional<ObjectNode> convert(Message message) {
    try {
      String json = messageRegistry.toJson(message);
      return convertToObjectNode(json);
    } catch (InvalidProtocolBufferException e) {
      log.warn("Could not generate Json", e);
      return Optional.empty();
    }
  }

  private static Optional<ObjectNode> convertToObjectNode(String json) {
    try {
      return Optional.of(OBJECT_MAPPER.readTree(json).deepCopy());
    } catch (JsonProcessingException e) {
      log.warn("Could not create ObjectNode", e);
      return Optional.empty();
    }
  }
}
