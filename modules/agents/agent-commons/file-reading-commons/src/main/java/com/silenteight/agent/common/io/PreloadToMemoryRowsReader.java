package com.silenteight.agent.common.io;

import com.google.common.collect.ImmutableList;

import java.util.Iterator;
import java.util.List;

public class PreloadToMemoryRowsReader<T> implements RowsReader<T> {

  private final List<T> preloadedData;

  public PreloadToMemoryRowsReader(RowsReader<T> inner) {
    // By empirical tests, this seems to be faster than Linked List
    preloadedData = ImmutableList.copyOf(inner.iterator());
  }

  @Override
  public Iterator<T> iterator() {
    return preloadedData.iterator();
  }
}
