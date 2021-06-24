package com.silenteight.hsbc.bridge.aws

import spock.lang.Specification

class AwsUriUtilsSpec extends Specification {

  static def RESOURCE_URI = "http://127.0.0.1:9000/test/testFile.xml?versionId=8b3bd531-0897-4021-885b-d322bf5f2d7c"

  def "should get file name from uri"() {
    when:
    def result = AwsUriUtils.getObjectKey(URI.create(RESOURCE_URI))

    then:
    result == "testFile.xml"
  }

  def "should get file version from uri"() {
    when:
    def result = AwsUriUtils.getObjectVersion(URI.create(RESOURCE_URI))

    then:
    result == "8b3bd531-0897-4021-885b-d322bf5f2d7c"
  }
}
