package com.silenteight.agent.common.io;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.util.List;
import java.util.function.Function;

import static com.silenteight.agent.common.io.FileFormatConstants.COMMENT_MARKER;
import static com.silenteight.agent.common.io.FileFormatConstants.QUOTE_CHAR;
import static com.silenteight.agent.common.io.FileFormatConstants.VALUES_SEPARATOR_CHAR;
import static com.silenteight.agent.configloader.ConfigsPathFinder.findFile;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;
import static org.apache.commons.csv.CSVParser.parse;

public class CsvReader {

  private static final CSVFormat CSV_FORMAT = CSVFormat
      .newFormat(VALUES_SEPARATOR_CHAR)
      .withQuote(QUOTE_CHAR)
      .withIgnoreEmptyLines()
      .withTrim()
      .withCommentMarker(COMMENT_MARKER);

  public <T> List<T> readCsv(String path, Function<CSVRecord, T> rowParser) {
    var file = findFile(path);

    try (CSVParser csvParser = parse(file, UTF_8, CSV_FORMAT)) {

      return stream(csvParser.spliterator(), false)
          .map(rowParser)
          .collect(toList());

    } catch (Exception e) {
      throw new FileReadingException(path, e);
    }
  }
}
