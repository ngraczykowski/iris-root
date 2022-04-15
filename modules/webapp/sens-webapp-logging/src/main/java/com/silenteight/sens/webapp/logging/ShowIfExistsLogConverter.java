package com.silenteight.sens.webapp.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.CompositeConverter;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public class ShowIfExistsLogConverter extends CompositeConverter<ILoggingEvent> {

  private static final String REPLACEMENT_SIGN = "$";

  @Override
  protected String transform(ILoggingEvent o, String input) {
    String pattern = getFirstOption();
    if (isNotEmpty(input) && isNotEmpty(pattern)) {
      return pattern.replace(REPLACEMENT_SIGN, input);
    }

    return "";
  }
}
