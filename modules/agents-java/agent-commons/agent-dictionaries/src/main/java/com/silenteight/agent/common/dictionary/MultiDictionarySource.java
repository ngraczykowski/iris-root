package com.silenteight.agent.common.dictionary;

import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.joining;

@RequiredArgsConstructor
class MultiDictionarySource implements DictionarySource {

  private final Collection<? extends DictionarySource> sources;

  @Override
  public String getIdentifier() {
    return sources
        .stream()
        .map(DictionarySource::getIdentifier)
        .collect(joining(","));
  }

  @Override
  public DictionaryStream stream() {
    return new MultiDictionaryStream(sources);
  }

  private static class MultiDictionaryStream implements DictionaryStream {

    private final MultiDictionaryStreamIterator iterator;

    MultiDictionaryStream(Collection<? extends DictionarySource> sources) {
      iterator = new MultiDictionaryStreamIterator(sources.iterator());
    }

    @Override
    public Stream<String> lines() {
      Iterable<String> iterable = () -> iterator;
      return StreamSupport.stream(iterable.spliterator(), false);
    }

    @Override
    public void close() {
      iterator.close();
    }
  }

  @RequiredArgsConstructor
  private static class MultiDictionaryStreamIterator implements Iterator<String>, AutoCloseable {

    private final Iterator<? extends DictionarySource> sourceIterator;

    private DictionaryStream stream;
    private Iterator<String> linesIterator;

    @Override
    public boolean hasNext() {
      do {
        if (hasNextLine()) {
          return true;
        }
      } while (hasNextSource());

      return false;
    }

    private boolean hasNextLine() {
      return linesIterator != null && linesIterator.hasNext();
    }

    private boolean hasNextSource() {
      close();
      if (sourceIterator.hasNext()) {
        stream = sourceIterator.next().stream();
        linesIterator = stream.lines().iterator();
        return true;
      }
      return false;
    }

    @Override
    public String next() {
      return linesIterator.next();
    }

    @Override
    public void close() {
      if (stream != null) {
        stream.close();
      }
    }
  }
}
