package com.silenteight.adjudication.engine.common.jdbc;

import java.util.List;

public interface ChunkHandler<T> {

  void handle(List<? extends T> chunk);

  default void finished() {
  }
}
