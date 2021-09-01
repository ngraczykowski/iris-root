package com.silenteight.hsbc.bridge.util

import spock.lang.Specification

import static com.silenteight.hsbc.bridge.util.StreamUtils.distinctBy
import static java.util.stream.Collectors.toList

class StreamUtilsSpec extends Specification {

  def 'should filter collection to distinct values of given type'() {
    given:
    def collection = List.of("1", "2", "3", "3", "4")

    when:
    def result = collection.stream()
        .filter(distinctBy(s -> s))
        .collect(toList())

    then:
    result.size() == 4
    result.containsAll("1", "2", "3", "4")
  }
}
