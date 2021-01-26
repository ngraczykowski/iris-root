package com.silenteight.agent.common.io;

import lombok.RequiredArgsConstructor;

import com.google.common.collect.Streams;
import com.univocity.parsers.csv.CsvParserSettings;
import com.univocity.parsers.csv.CsvRoutines;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.util.Iterator;
import java.util.function.Supplier;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class UnivocityCsvReader<T> implements RowsReader<T> {

  private final Supplier<InputStream> data;
  private final CsvParserSettings parserSettings;
  private final Class<T> rowClass;

  public Stream<T> stream() {
    return Streams.stream(iterator());
  }

  @Override
  @NotNull
  public Iterator<T> iterator() {
    return new CsvRoutines(parserSettings)
        .iterate(rowClass, data.get())
        .iterator();
  }
}
