package com.silenteight.hsbc.bridge.aws

import com.amazonaws.services.s3.AmazonS3URI
import spock.lang.Specification

class UriDataExtractorSpec extends Specification {
  static def URI = "http://127.0.0.1:9000/test/testFile.xml?versionId=8b3bd531-0897-4021-885b-d322bf5f2d7c"
  static def S3_URI = "s3://127.0.0.1:9000/test/testFile.xml?versionId=8b3bd531-0897-4021-885b-d322bf5f2d7c"
  static def AWS_URI = "s3://127.0.0.1:9000/test/testFile.xml%3FversionId%3D8b3bd531-0897-4021-885b-d322bf5f2d7c"

  def "should change http uri to aws"(){
    when:
    def result = UriDataExtractor.toAwsUri(URI)

    then:
    result.getURI().toString() == AWS_URI
  }

  def "should get file name from uri"(){
    when:
    def result = UriDataExtractor.getObjectKey(getAmazonS3Uri())

    then:
    result == "testFile.xml"
  }

  def "should get file version from uri"(){
    when:
    def result = UriDataExtractor.getObjectVersion(getAmazonS3Uri())

    then:
    result == "8b3bd531-0897-4021-885b-d322bf5f2d7c"
  }

  static def getAmazonS3Uri(){
    return new AmazonS3URI(S3_URI)
  }
}
