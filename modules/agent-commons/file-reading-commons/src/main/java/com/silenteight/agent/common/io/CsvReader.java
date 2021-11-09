package com.silenteight.agent.common.io;

import lombok.NonNull;
import lombok.Value;
import lombok.experimental.UtilityClass;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import static com.silenteight.agent.common.io.FileFormatConstants.COMMENT_MARKER;
import static com.silenteight.agent.common.io.FileFormatConstants.QUOTE_CHAR;
import static com.silenteight.agent.common.io.FileFormatConstants.VALUES_SEPARATOR_CHAR;
import static com.silenteight.agent.configloader.ConfigsPathFinder.findFile;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.StreamSupport.stream;
import static org.apache.commons.csv.CSVParser.parse;

@SuppressWarnings("unused") //used in agents as helper
@UtilityClass
public class CsvReader {

  private static final CSVFormat CSV_FORMAT = CSVFormat
      .newFormat(VALUES_SEPARATOR_CHAR)
      .withQuote(QUOTE_CHAR)
      .withIgnoreEmptyLines()
      .withTrim()
      .withCommentMarker(COMMENT_MARKER);

  public static <T> List<T> readCsv(String path, Function<CSVRecord, T> rowParser) {
    var file = findFile(path);

    try (CSVParser csvParser = parse(file, UTF_8, CSV_FORMAT)) {

      return stream(csvParser.spliterator(), false)
          .map(rowParser)
          .collect(toList());

    } catch (Exception e) {
      throw new FileReadingException(path, e);
    }
  }

  /**
   * <pre>
   * First csv line is treated as a header.
   * Example:
   * header1;header2;header3 -> List.of(header1,header2,header3)
   *
   * Info: InputStream is closing in CSVParser.parse() method.
   * </pre>
   */

  public static CsvData readCsvWithHeaderFromStream(
      @NonNull InputStream is) {
    var csvFormat = CSV_FORMAT.withFirstRecordAsHeader();

    try (CSVParser csvParser = CSVParser.parse(is, UTF_8, csvFormat)) {

      var headers = csvParser.getHeaderNames();
      var rows = stream(csvParser.spliterator(), false)
          .map(CsvReader::mapToList)
          .collect(toSet());

      return new CsvData(headers, rows);
    } catch (Exception e) {
      throw new FileReadingException(e);
    }
  }

  public static <T> MappedCsvData<T> readCsvWithHeaderFromStream(
      @NonNull InputStream is,
      Function<List<String>, T> rowMapper) {
    var csvFormat = CSV_FORMAT.withFirstRecordAsHeader();

    try (CSVParser csvParser = CSVParser.parse(is, UTF_8, csvFormat)) {

      var headers = csvParser.getHeaderNames();
      var rows = stream(csvParser.spliterator(), false)
          .map(CsvReader::mapToList)
          .map(rowMapper)
          .collect(toSet());

      return new MappedCsvData<>(headers, rows);
    } catch (Exception e) {
      throw new FileReadingException(e);
    }
  }

  private static List<String> mapToList(CSVRecord csvRecord) {
    List<String> list = new ArrayList<>();
    csvRecord.forEach(list::add);
    return list;
  }

  @Value
  public static class CsvData {

    List<String> headers;
    Set<List<String>> rows;
  }

  @Value
  public static class MappedCsvData<T> {

    List<String> headers;
    Set<T> rows;
  }
}
