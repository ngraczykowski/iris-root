package com.silenteight.sep.base.common.database;

import com.silenteight.sep.base.common.support.hibernate.ByteStringConverter;

import com.google.protobuf.ByteString;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ByteStringConverterTest {

  private final ByteStringConverter underTest = new ByteStringConverter();

  @Test
  void convertsBackAndForth() {
    ByteString given = ByteString.copyFromUtf8("someByteString");

    String converted = underTest.convertToDatabaseColumn(given);
    ByteString actual = underTest.convertToEntityAttribute(converted);

    assertThat(actual).isEqualTo(given);
  }

  @Test
  void nullInputTranslatesToNullOutput() {
    assertThat(underTest.convertToDatabaseColumn(null)).isNull();
    assertThat(underTest.convertToEntityAttribute(null)).isNull();
  }

  @Test
  void emptyInputResultsInEmptyOutput() {
    assertThat(underTest.convertToDatabaseColumn(ByteString.copyFromUtf8(""))).isEmpty();
    assertThat(underTest.convertToEntityAttribute("")).isEmpty();
  }
}
