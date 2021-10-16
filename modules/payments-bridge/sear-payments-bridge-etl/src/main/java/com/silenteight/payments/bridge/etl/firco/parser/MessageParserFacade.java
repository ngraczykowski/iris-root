package com.silenteight.payments.bridge.etl.firco.parser;

import com.silenteight.payments.bridge.etl.processing.model.MessageData;

import org.springframework.stereotype.Component;

@Component
public class MessageParserFacade {

  MessageData parse(MessageFormat messageFormat, String message) {
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
