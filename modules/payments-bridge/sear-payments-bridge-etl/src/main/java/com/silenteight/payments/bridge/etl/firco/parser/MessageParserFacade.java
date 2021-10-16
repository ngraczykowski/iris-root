package com.silenteight.payments.bridge.etl.firco.parser;

public class MessageParserFacade {

  MessageData get(MessageFormat messageFormat, String message) {
    switch (messageFormat) {
      case ALL:
        return new FircoMessageParser(message).parse();
      case SWIFT:
        return new SwiftMessageParser(message).parse();
      default:
        throw new IllegalArgumentException("Unknown message format " + messageFormat);
    }
  }
}
