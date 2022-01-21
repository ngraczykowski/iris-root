package com.silenteight.agent.common.dictionary;

import java.util.stream.Stream;

/**
 * Wraps {@link Stream } of lines and allows holding resources until they can be released via {@link
 * #close()}
 */
public interface DictionaryStream extends AutoCloseable {

  Stream<String> lines();

  @Override
  void close();
}
