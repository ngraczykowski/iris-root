package com.silenteight.agent.common.io;

import com.google.common.collect.Streams;

import java.util.stream.Stream;

public interface RowsReader<T> extends Iterable<T> {

  default Stream<T> stream() {
    return Streams.stream(iterator());
  }
}
