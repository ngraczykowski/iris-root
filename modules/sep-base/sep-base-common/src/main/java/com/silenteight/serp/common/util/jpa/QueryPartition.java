package com.silenteight.serp.common.util.jpa;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.function.Consumer;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class QueryPartition {

  private static final int PARTITION_SIZE = 30_000;

  public static <T> void doInPartitions(List<T> fullList, Consumer<List<T>> action) {
    for (List<T> partition : Lists.partition(fullList, PARTITION_SIZE))
      action.accept(partition);
  }
}
