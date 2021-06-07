package com.silenteight.hsbc.bridge.unpacker

import spock.lang.Specification

class FileUnzipperSpec extends Specification {

  static def FILE_CONTENT =
      "watchlist content\n" +
      "test line 1\n" +
      "test line 2"
  def underTest = new FileUnzipper()

  def "should unzip gzip file"(){
    given:
    def archive = getClass().getResourceAsStream("/files/watchlistTest.gz")

    when:
    def result = underTest.unzip(archive)

    then:
    result.getInputStream().bytes.size() == FILE_CONTENT.size()
  }
}
