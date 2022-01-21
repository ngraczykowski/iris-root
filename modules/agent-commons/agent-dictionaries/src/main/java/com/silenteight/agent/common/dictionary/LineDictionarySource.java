package com.silenteight.agent.common.dictionary;

import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.stream.Stream;

@RequiredArgsConstructor
class LineDictionarySource implements DictionarySource {

  private final Collection<String> lines;

  @Override
  public String getIdentifier() {
    return lines.toString();
  }

  @Override
  public DictionaryStream stream() {
    return new LineDictionaryStream();
  }

  private class LineDictionaryStream implements DictionaryStream {

    private final Stream<String> stream = lines.stream();

    @Override
    public Stream<String> lines() {
      return stream;
    }

    @Override
    public void close() {
      stream.close();
    }
  }
}
