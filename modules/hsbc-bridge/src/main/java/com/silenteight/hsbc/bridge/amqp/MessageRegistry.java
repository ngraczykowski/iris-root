package com.silenteight.hsbc.bridge.amqp;

import com.google.protobuf.Message;
import com.google.protobuf.Parser;

import java.util.Optional;

public interface MessageRegistry {

  Optional<Parser<Message>> findParser(String typeName);
}
