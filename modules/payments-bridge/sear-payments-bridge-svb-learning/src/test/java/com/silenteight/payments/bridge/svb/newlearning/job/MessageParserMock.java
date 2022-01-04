package com.silenteight.payments.bridge.svb.newlearning.job;

import com.silenteight.payments.bridge.etl.parser.domain.MessageFormat;
import com.silenteight.payments.bridge.etl.parser.port.MessageParserUseCase;
import com.silenteight.payments.bridge.etl.processing.model.MessageData;
import com.silenteight.payments.bridge.etl.processing.model.MessageTag;

import java.util.List;

class MessageParserMock implements MessageParserUseCase {

  @Override
  public MessageData parse(MessageFormat messageFormat, String message) {
    return new MessageData(List.of(new MessageTag("tag", "value")));
  }
}
