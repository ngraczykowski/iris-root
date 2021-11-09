package com.silenteight.agent.common.io;

import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.*;

class CsvReaderTest {

  @Test
  void shouldReadCsvFile() {
    //given
    var url = CsvReaderTest.class.getResource("/csvfiles/file.csv");
    var path = url.getPath();
    Function<CSVRecord, List<String>> mapper = CsvReaderTest::mapper;

    //when
    var csv = CsvReader.readCsv(path, mapper);

    //then
    assertThat(csv).containsExactly(
        List.of(
            "ala",
            "ma",
            "kota"
        ));
  }

  private static List<String> mapper(CSVRecord record) {
    List<String> values = new ArrayList<>();
    record.forEach(values::add);
    return values;
  }

  @Test
  void shouldReadCsvWithHeader() {
    //given
    var path = "/csvfiles/file_with_header.csv";
    var is = CsvReaderTest.class.getResourceAsStream(path);

    //when
    var csv = CsvReader.readCsvWithHeaderFromStream(is);
    var headers = csv.getHeaders();
    var rows = csv.getRows();

    //then
    assertThat(headers).containsExactly(
        "header1",
        "header2"
    );

    assertThat(rows).containsExactly(
        List.of(
            "ala",
            "ma",
            "kota"
        )
    );
  }

  @Test
  void shouldReadCsvWithHeaderAndCustomParser() {
    //given
    var path = "/csvfiles/file_with_header.csv";
    var is = CsvReaderTest.class.getResourceAsStream(path);
    Function<List<String>, List<String>> parser = CsvReaderTest::rowMapper;

    //when
    var csv = CsvReader.readCsvWithHeaderFromStream(is, parser);
    var headers = csv.getHeaders();
    var rows = csv.getRows();

    //then
    assertThat(headers).containsExactly(
        "header1",
        "header2"
    );

    assertThat(rows).containsExactly(
        List.of(
            "ALA",
            "MA",
            "KOTA"
        )
    );
  }

  private static List<String> rowMapper(List<String> row) {
    return row.stream()
        .map(String::toUpperCase)
        .collect(toList());
  }
}