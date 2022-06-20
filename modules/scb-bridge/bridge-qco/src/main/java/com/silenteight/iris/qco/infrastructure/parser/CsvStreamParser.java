/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.qco.infrastructure.parser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
class CsvStreamParser<T> {

  private final CsvMapper mapper;
  private final char columnSeparator;

  List<T> parse(InputStream inputStream, Class<T> clazz) throws IOException {
    var csvSchema = mapper.schemaFor(clazz).withColumnSeparator(columnSeparator);
    return (List<T>) mapper.readerFor(clazz)
        .with(csvSchema)
        .readValues(inputStream)
        .readAll();
  }
}
