package com.silenteight.payments.bridge.etl.parser.port;

import com.silenteight.payments.bridge.etl.parser.domain.MessageFormat;
import com.silenteight.payments.bridge.etl.processing.model.MessageData;

public interface MessageParserUseCase {

  MessageData parse(MessageFormat messageFormat, String message);
}
