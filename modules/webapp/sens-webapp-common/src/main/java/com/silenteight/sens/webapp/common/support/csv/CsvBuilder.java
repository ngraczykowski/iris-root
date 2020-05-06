package com.silenteight.sens.webapp.common.support.csv;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

import static com.silenteight.commons.CSVUtils.getCSVRecordWithDefaultDelimiter;
import static java.util.stream.Collectors.toList;

public class CsvBuilder<T> implements LinesSupplier {

  private final Stream<T> elements;
  private final Collection<String> headers;
  private final Collection<Function<T, String>> cells;

  public CsvBuilder(Stream<T> elements) {
    this.elements = elements;
    this.cells = new ArrayList<>();
    this.headers = new ArrayList<>();
  }

  public CsvBuilder<T> cell(String header, Function<T, String> value) {
    this.headers.add(header);
    this.cells.add(value);
    return this;
  }

  @Override
  public Stream<String> lines() {
    final Builder<String> builder = Stream.builder();
    builder.add(buildHeader());
    this.elements.forEach(element -> builder.add(buildRow(element)));
    return builder.build();
  }

  public String build() {
    return String.format("%s%s", buildHeader(), buildRows());
  }

  private String buildRows() {
    final StringBuilder rows = new StringBuilder();
    this.elements.forEach(element -> rows.append(buildRow(element)));
    return rows.toString();
  }

  private String buildRow(T element) {
    List<String> cellsInRow = this.cells
        .stream()
        .map(columnFunction -> columnFunction.apply(element))
        .collect(toList());
    return getCSVRecordWithDefaultDelimiter(toArray(cellsInRow));
  }

  private String buildHeader() {
    return getCSVRecordWithDefaultDelimiter(toArray(headers));
  }

  private static String[] toArray(Collection<String> collection) {
    String[] array = new String[collection.size()];
    return collection.toArray(array);
  }
}
