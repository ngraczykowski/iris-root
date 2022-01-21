package com.silenteight.agent.common.dictionary;

import java.util.function.Function;
import java.util.stream.Stream;

class TestDictionaryFactory implements Function<Stream<String>, TestDictionary> {

  @Override
  public TestDictionary apply(Stream<String> stringStream) {
    return new TestDictionary(stringStream);
  }
}
