package com.silenteight.hsbc.bridge.aws

import spock.lang.Specification

class AwsUriUtilsSpec extends Specification {

  static def RESOURCE_URI = "http://127.0.0.1:9000/test/testFile.xml"

  def "should get file name from uri"() {
    when:
    def result = AwsUriUtils.getObjectKey(URI.create(RESOURCE_URI))

    then:
    result == "testFile.xml"
  }
}
