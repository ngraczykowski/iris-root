package com.silenteight.sens.webapp.common.support.csv;

import lombok.AllArgsConstructor;
import lombok.Data;

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
    String csv = new CsvBuilder<>(Stream.of(
        new SimpleDto("field a 1", 1, 1L),
        new SimpleDto("field a 2", 1, 1L)))
        .cell("header a", SimpleDto::getFieldA)
        .build();

    // then
    assertThat(csv).isEqualTo("header a\nfield a 1\nfield a 2\n");
  }

  @Test
  void shouldReturnTabSeparatedCells() {
    // when
    String csv = new CsvBuilder<>(Stream.of(
        new SimpleDto("field a", 1, 1L),
        new SimpleDto("field b", 2, 2L)))
        .delimiter("\t")
        .cell("header a", SimpleDto::getFieldA)
        .cell("header b", dto -> String.valueOf(dto.fieldB))
        .build();

    // then
    assertThat(csv).isEqualTo("header a\theader b\nfield a\t1\nfield b\t2\n");
  }

  @Data
  @AllArgsConstructor
  private class SimpleDto {

    private String fieldA;
    private int fieldB;
    private Long fieldC;
  }
}
