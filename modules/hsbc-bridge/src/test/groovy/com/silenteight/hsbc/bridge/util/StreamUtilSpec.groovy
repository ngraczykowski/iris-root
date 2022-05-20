package com.silenteight.hsbc.bridge.util

import spock.lang.Specification

import java.util.stream.Collectors

class StreamUtilsSpec extends Specification {

  def 'should filter collection to distinct values of given type'() {
    given:
    def collection = List.of("1", "2", "3", "3", "4")

    when:
    def result = collection.stream()
        .filter(StreamUtils.distinctBy(s -> s))
        .collect(Collectors.toList())

    then:
    result.size() == 4
    result.containsAll("1", "2", "3", "4")
  }
}
