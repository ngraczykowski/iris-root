package com.silenteight.agent.common.io;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import static com.silenteight.agent.common.io.FileFormatConstants.*;
import static com.silenteight.agent.configloader.ConfigsPathFinder.findFile;
import static java.nio.file.Files.readAllLines;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

@SuppressWarnings("unused") //used in agents as helper
public class FileReader {

  /**
   * <pre>
   *   Load lines from file located under AGENT_HOME/conf/{path}
   *
   *   Expected dict format: "value1;value2"
   *
   *   Returns unique unique values, all UPPER CASE, ignores # comments or empty lines
   * </pre>
   */
  public static Set<Set<String>> readMultipleValues(String path) {
    return readLinesAsStream(path, DEFAULT_DICT_FORMAT_FILTERS, DEFAULT_DICT_FORMAT_TRANSFORMERS)
        .map(line -> Set.of(line.split(VALUES_SEPARATOR)))
        .collect(toSet());
  }

  /**
   * <pre>
   *   Load lines from file located under AGENT_HOME/conf/{path}
   *
   *   Expected dict format: "key=value1;value2"
   *
   *   Returns unique keys and unique values, all UPPER CASE, ignores # comments or empty lines
   * </pre>
   */
  public static Map<String, Set<String>> readSingleKeyMultipleValues(String path) {
    return readLinesAsStream(path, DEFAULT_DICT_FORMAT_FILTERS, DEFAULT_DICT_FORMAT_TRANSFORMERS)
        .map(line -> line.split(KEY_VALUE_SEPARATOR))
        .collect(toMap(extractSingleKey(), extractMultipleValues()));
  }

  /**
   * <pre>
   *   Load lines from file located under AGENT_HOME/conf/{path}
   *
   *   Expected dict format: "key1;key2=value1;value2"
   *
   *   Returns unique keys and unique values, all UPPER CASE, ignores # comments or empty lines
   * </pre>
   */
  public static Map<Set<String>, Set<String>> readMultipleKeyMultipleValues(String path) {
    return readLinesAsStream(path, DEFAULT_DICT_FORMAT_FILTERS, DEFAULT_DICT_FORMAT_TRANSFORMERS)
        .map(line -> line.split(KEY_VALUE_SEPARATOR))
        .collect(toMap(extractMultipleKeys(), extractMultipleValues()));
  }

  /**
   * <pre>
   *   Load lines from file located under AGENT_HOME/conf/{path}
   *   Returns unique lines ignoring comments or empty lines
   *   Order of lines is preserved
   * </pre>
   */
  public static List<String> readRegexpPatternsAsList(String path) {
    return readLinesAsStream(path, DEFAULT_DICT_FORMAT_FILTERS, DEFAULT_REGEXP_FORMAT_TRANSFORMERS)
        .distinct()
        .collect(toList());
  }

  /**
   * <pre>
   *  Load lines from file located under AGENT_HOME/conf/{path}
   *  Returns all UPPER CASE, unique lines ignoring comments or empty lines
   * </pre>
   */
  public static Set<String> readLinesAsSet(String path) {
    return readLinesAsStream(path, DEFAULT_DICT_FORMAT_FILTERS, DEFAULT_DICT_FORMAT_TRANSFORMERS)
        .collect(toSet());
  }

  public static Set<String> readLinesAsSet(
      String path,
      Collection<Predicate<String>> filters,
      Collection<UnaryOperator<String>> lineTransformers) {
    return readLinesAsStream(path, filters, lineTransformers)
        .collect(toSet());
  }

  /**
   * Load lines from file located under AGENT_HOME/conf/{path} without any filtering or
   * transformations
   */
  public static Stream<String> readLinesAsStream(String path) {
    var file = findFile(path);

    try {
      return readAllLines(file).stream();
    } catch (IOException e) {
      throw new FileReadingException(path, e);
    }
  }

  /**
   * Load lines from file located under AGENT_HOME/conf/{path} applying filters and transformers for
   * each line
   */
  public static Stream<String> readLinesAsStream(
      String path,
      Collection<Predicate<String>> filters,
      Collection<UnaryOperator<String>> lineTransformers) {
    var file = findFile(path);

    try {
      Stream<String> stream = readAllLines(file).stream();
      for (Predicate<String> filter : filters) {
        stream = stream.filter(filter);
      }

      for (UnaryOperator<String> transformer : lineTransformers) {
        stream = stream.map(transformer);
      }

      return stream;
    } catch (Exception e) {
      throw new FileReadingException(path, e);
    }
  }

  private static Function<String[], String> extractSingleKey() {
    return split -> split[0];
  }

  private static Function<String[], Set<String>> extractMultipleKeys() {
    return split -> Set.of(split[0].split(VALUES_SEPARATOR));
  }

  private static Function<String[], Set<String>> extractMultipleValues() {
    return split -> Set.of(split[1].split(VALUES_SEPARATOR));
  }
}
