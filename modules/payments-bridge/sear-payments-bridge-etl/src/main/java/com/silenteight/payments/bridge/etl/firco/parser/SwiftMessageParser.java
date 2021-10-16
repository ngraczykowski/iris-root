package com.silenteight.payments.bridge.etl.firco.parser;

import com.prowidesoftware.swift.io.parser.SwiftParser;
import com.prowidesoftware.swift.model.SwiftMessage;

import java.io.IOException;

class SwiftMessageParser {

  private final SwiftParser parser;

  SwiftMessageParser(String message) {
    parser = new SwiftParser(message);
  }

  MessageData parse() {
    var swiftMessage = parseMessage();
    var block4 = swiftMessage.getBlock4();
    return new MessageData(block4.getTagMap());
  }

  private SwiftMessage parseMessage() {
    try {
      return parser.message();
    } catch (IOException e) {
      throw new MessageParsingException("Failed to parse SWIFT message", e);
    }
  }
}
