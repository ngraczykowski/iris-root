package com.silenteight.hsbc.bridge.unpacker

import spock.lang.Specification

class FileManagerSpec extends Specification {

  def name = "watchlistFile"
  def property = System.getProperty("java.io.tmpdir")
  def underTest = new FileManager(property)

  def "should unzip gzip file"() {
    given:
    def archive = getClass().getResourceAsStream("/files/watchlistTest.gz")

    when:
    def result = underTest.unpackGzip(archive)

    then:
    result.name == name
    result.path == property + File.separator + name
  }

  def "should delete unzipped file"() {
    given:
    def archive = getClass().getResourceAsStream("/files/watchlistTest.gz")
    def path = underTest.unpackGzip(archive).getPath()

    when:
    def result = underTest.delete(path)

    then:
    result
  }

  def "should return false when path is wrong"() {
    given:
    def path = '/wrong/path'

    when:
    def result = underTest.delete(path)

    then:
    !result
  }
}
