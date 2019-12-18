package com.silenteight.sens.webapp.common.support.csv;

import lombok.AllArgsConstructor;
import lombok.Data;

import com.silenteight.sens.webapp.common.audit.AuditDto;

import org.hibernate.envers.RevisionType;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class CsvBuilderTest {

  @Test
  public void shouldReturnsCsvWithHeadersOnlyWhenEmptyListOfRowsPassed() {
    // when
    String csv = new CsvBuilder<SimpleDto>(Stream.empty())
        .cell("header a", SimpleDto::getFieldA)
        .cell("header b", c -> String.valueOf(c.getFieldB()))
        .cell("header c", c -> c.getFieldC().toString())
        .build();

    // then
    assertThat(csv).isEqualTo("header a,header b,header c\n");
  }

  @Test
  public void shouldReturnsFullCsvForNonEmptyListOfElements() {
    // when
    String csv = new CsvBuilder<SimpleDto>(Stream.of(
        new SimpleDto("field a 1", 1, 1L),
        new SimpleDto("field a 2", 1, 1L)))
        .cell("header a", SimpleDto::getFieldA)
        .build();

    // then
    assertThat(csv).isEqualTo("header a\nfield a 1\nfield a 2\n");
  }

  @Data
  @AllArgsConstructor
  private class SimpleDto implements AuditDto {

    private String fieldA;
    private int fieldB;
    private Long fieldC;

    @Override
    public RevisionType getRevisionType() {
      return RevisionType.ADD;
    }
  }
}
