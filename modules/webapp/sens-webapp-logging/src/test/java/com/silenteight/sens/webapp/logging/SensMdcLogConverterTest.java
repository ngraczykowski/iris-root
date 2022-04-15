package com.silenteight.sens.webapp.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static java.util.Collections.emptyMap;
import static java.util.Map.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class SensMdcLogConverterTest {

  private SensMdcLogConverter underTest = new SensMdcLogConverter();

  @Mock
  private ILoggingEvent loggingEvent;

  @Test
  void unknownEntries_returnsEmptyString() {
    given(loggingEvent.getMDCPropertyMap()).willReturn(of("some", "entry"));

    String actual = underTest.convert(loggingEvent);

    assertThat(actual).isEmpty();
  }

  @Test
  void emptyMap_returnsEmptyString() {
    given(loggingEvent.getMDCPropertyMap()).willReturn(emptyMap());

    String actual = underTest.convert(loggingEvent);

    assertThat(actual).isEmpty();
  }

  @Test
  void singleKnownEntry_returnsCorrectString() {
    given(loggingEvent.getMDCPropertyMap()).willReturn(of(
        SensWebappMdcKeys.USERNAME.getKey(), "jdoe123"));

    String actual = underTest.convert(loggingEvent);

    assertThat(actual).isEqualTo("username=jdoe123");
  }
}
