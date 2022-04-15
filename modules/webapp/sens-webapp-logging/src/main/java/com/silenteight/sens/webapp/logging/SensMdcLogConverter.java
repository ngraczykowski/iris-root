package com.silenteight.sens.webapp.logging;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import one.util.streamex.EntryStream;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class SensMdcLogConverter extends ClassicConverter {

  private static final List<String> KNOWN_KEYS = SensWebappMdcKeys.getAllKeys();
  private static final String DELIMITER = ",";
  private static final String JOINER = "=";

  @Override
  public String convert(ILoggingEvent loggingEvent) {
    Map<String, String> mdc = loggingEvent.getMDCPropertyMap();

    StringJoiner values = new StringJoiner(DELIMITER);
    EntryStream.of(mdc)
        .filterKeys(KNOWN_KEYS::contains)
        .forKeyValue((key, value) -> values.add(key + JOINER + value));

    return values.toString();
  }
}
