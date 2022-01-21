package com.silenteight.agent.common.dictionary;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static lombok.AccessLevel.NONE;
import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor
@Data
class TestSource implements DictionarySource {

  private enum State {
    OPENED, CLOSED
  }

  @Setter(PRIVATE)
  @Getter(NONE)
  private State state;

  private final AtomicInteger readLinesCounter = new AtomicInteger();

  private final String identifier;
  private final Stream<String> stream;

  boolean isStreamOpened() {
    return state == State.OPENED;
  }

  boolean isStreamClosed() {
    return state == State.CLOSED;
  }

  boolean hasNotBeenUsed() {
    return !(isStreamOpened() || isStreamClosed());
  }

  @Override
  public String getIdentifier() {
    return identifier;
  }

  @Override
  public DictionaryStream stream() {
    setState(State.OPENED);
    return new DictionaryStream() {
      @Override
      public Stream<String> lines() {
        return stream.peek(l -> readLinesCounter.incrementAndGet());
      }

      @Override
      public void close() {
        setState(State.CLOSED);
        stream.close();
      }
    };
  }
}
