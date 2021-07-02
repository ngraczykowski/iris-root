package com.silenteight.warehouse.common.elastic;

import org.elasticsearch.index.query.TermsQueryBuilder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class DlsQueryProcessorTest {

  private static final String KEYWORD = "country.keyword";
  private static final String VALUE = "UK";

  private static final String TERMS_SERIALIZED = "{\n"
      + "  \"terms\" : {\n"
      + "    \"" + KEYWORD + "\" : [\n"
      + "      \"" + VALUE + "\"\n"
      + "    ],\n"
      + "    \"boost\" : 1.0\n"
      + "  }\n"
      + "}";

  private final DlsQueryProcessor underTest = new DlsQueryProcessor();

  @Test
  void shouldSerialize() {
    TermsQueryBuilder builder = new TermsQueryBuilder(KEYWORD, VALUE);

    String serialized = underTest.serialize(builder);

    assertThat(serialized).isEqualTo(TERMS_SERIALIZED);
  }

  @Test
  void shouldDeserialize() {
    TermsQueryBuilder builder = underTest.deserialize(TERMS_SERIALIZED);

    assertThat(builder.fieldName()).isEqualTo(KEYWORD);
    assertThat(builder.values()).containsExactly(VALUE);
  }
}
