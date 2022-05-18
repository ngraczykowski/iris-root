package com.silenteight.payments.bridge.etl.parser.service;

import com.silenteight.payments.bridge.etl.parser.domain.MessageFormat;
import com.silenteight.payments.bridge.etl.parser.port.MessageParserUseCase;
import com.silenteight.payments.bridge.etl.processing.model.MessageData;

import org.springframework.stereotype.Component;

@Component
class MessageParserFacade implements MessageParserUseCase {

  @Override
  public MessageData parse(MessageFormat messageFormat, String message) {
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
