package com.silenteight.agent.common.io;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collection;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;


import static com.silenteight.agent.common.io.FileFormatConstants.DEFAULT_DICT_FORMAT_FILTERS;
import static com.silenteight.agent.common.io.FileFormatConstants.DEFAULT_DICT_FORMAT_TRANSFORMERS;
import static com.silenteight.agent.common.io.FileFormatConstants.VALUES_SEPARATOR;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toSet;

@SuppressWarnings("unused") //used in agents as helper
@UtilityClass
public class ResourcesReader {

  /**
   * <pre>
   *   Load lines from resources
   *
   *   Expected dict format: "value1;value2"
   *
   *   Returns unique unique values, all UPPER CASE, ignores # comments or empty lines
   * </pre>
   */
  public static Set<Set<String>> readMultipleValues(@NonNull InputStream is) {
    return readLinesAsStream(is, DEFAULT_DICT_FORMAT_FILTERS, DEFAULT_DICT_FORMAT_TRANSFORMERS)
        .map(line -> Set.of(line.split(VALUES_SEPARATOR)))
        .collect(toSet());
  }


  /**
   * <pre>
   *   Load lines from resources
   *   Returns all UPPER CASE, unique lines ignoring comments or empty lines
   * </pre>
   */
  public static Set<String> readLinesAsSet(@NonNull InputStream is) {
    return readLinesAsStream(is, DEFAULT_DICT_FORMAT_FILTERS, DEFAULT_DICT_FORMAT_TRANSFORMERS)
        .collect(toSet());
  }

  public static Set<String> readLinesAsSet(
      @NonNull InputStream is,
      Collection<Predicate<String>> filters,
      Collection<UnaryOperator<String>> lineTransformers) {
    return readLinesAsStream(is, filters, lineTransformers)
        .collect(toSet());
  }

  /**
   * <pre>
   *   Load lines from file located under AGENT_HOME/conf/{path}
   *   without any filtering or transformations
   * </pre>
   */
  public static Stream<String> readLinesAsStream(@NonNull InputStream is) {
    return readLinesAsStream(is, emptyList(), emptyList());
  }


  /**
   * <pre>
   *   Load lines from resources
   *
   *   applying filters and transformers for each line
   * </pre>
   */
  public static Stream<String> readLinesAsStream(
      @NonNull InputStream is,
      Collection<Predicate<String>> filters,
      Collection<UnaryOperator<String>> lineTransformers) {
    try (is) {
      final InputStreamReader inputStreamReader = new InputStreamReader(is, UTF_8);
      final BufferedReader reader = toBufferedReader(inputStreamReader);
      final Stream.Builder<String> streamBuilder = Stream.builder();
      String line;
      while ((line = reader.readLine()) != null) {
        streamBuilder.add(line);
      }

      Stream<String> stream = streamBuilder.build();

      for (Predicate<String> filter : filters) {
        stream = stream.filter(filter);
      }

      for (UnaryOperator<String> transformer : lineTransformers) {
        stream = stream.map(transformer);
      }

      return stream;
    } catch (Exception e) {
      throw new FileReadingException(e);
    }
  }

  private static BufferedReader toBufferedReader(final Reader reader) {
    return reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader);
  }
}
