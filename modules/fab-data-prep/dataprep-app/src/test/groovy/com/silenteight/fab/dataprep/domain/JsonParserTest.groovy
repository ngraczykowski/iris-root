package com.silenteight.fab.dataprep.domain

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import spock.lang.Specification

import static java.util.stream.Collectors.toList

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class JsonParserTest extends Specification {

  @Autowired
  JsonParser underTest

  def 'hits nodes should be returned'() {
    when:
    def list = underTest.getHits('''{
 "Hits": [ {"a":"1"}, {"b":"2"} ]
}''')

    then:
    list.stream()
        .map { it.toString() }
        .collect(toList()) == ['{"a":"1"}', '{"b":"2"}']
  }
}
