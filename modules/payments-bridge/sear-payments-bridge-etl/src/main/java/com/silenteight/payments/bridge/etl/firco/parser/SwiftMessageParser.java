package com.silenteight.payments.bridge.etl.firco.parser;

import com.silenteight.payments.bridge.etl.processing.model.MessageData;
import com.silenteight.payments.bridge.etl.processing.model.MessageTag;

import com.prowidesoftware.swift.io.parser.SwiftParser;
import com.prowidesoftware.swift.model.SwiftMessage;

import java.io.IOException;
import java.util.stream.Collectors;

class SwiftMessageParser {

  private final SwiftParser parser;

  SwiftMessageParser(String message) {
    parser = new SwiftParser(message);
  }

  MessageData parse() {
    var swiftMessage = parseMessage();
    var block4 = swiftMessage.getBlock4();
    var tags = block4
        .getTags()
        .stream()
        .map(tag -> new MessageTag(tag.getName(), tag.getValue()))
        .collect(Collectors.toUnmodifiableList());

    return new MessageData(tags);
  }

  private SwiftMessage parseMessage() {
    try {
      return parser.message();
    } catch (IOException e) {
      throw new MessageParsingException("Failed to parse SWIFT message", e);
    }
  }
}
