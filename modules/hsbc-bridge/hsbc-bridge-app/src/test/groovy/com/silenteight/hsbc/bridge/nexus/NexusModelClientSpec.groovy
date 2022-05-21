package com.silenteight.hsbc.bridge.nexus

import com.silenteight.hsbc.bridge.jenkins.Fixtures
import com.silenteight.hsbc.bridge.nexus.NexusModelClient.NexusResponseNotReceivedException

import org.apache.commons.io.IOUtils
import spock.lang.Specification

import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.net.http.HttpResponse.BodyHandlers

class NexusModelClientSpec extends Specification {

  def fixtures = new Fixtures()
  def httpClient = Mock(HttpClient)
  def nexusApiProperties = fixtures.nexusApiProperties

  def underTest = new NexusModelClient(httpClient, nexusApiProperties)

  def 'should get transferred model from WorldCheck'() {
    given:
    def url = fixtures.testNexusUrl

    def nexusResponse = Mock(HttpResponse) {
      body() >> IOUtils.toInputStream("test_data", "UTF-8")
      statusCode() >> 200
    }

    when:
    def modelJson = underTest.updateModel(url)

    then:
    1 * httpClient.send({HttpRequest request -> request.method() == 'GET'}, BodyHandlers.ofInputStream()) >> nexusResponse
    modelJson
  }

  def 'should throw NexusResponseNotReceivedException when unable to receive model with status code other than 200'() {
    given:
    def url = fixtures.testNexusUrl

    def nexusResponse = Mock(HttpResponse) {
      body() >> IOUtils.toInputStream("test_data", "UTF-8")
      statusCode() >> 401
    }

    when:
    underTest.updateModel(url)

    then:
    1 * httpClient.send({HttpRequest request -> request.method() == 'GET'}, BodyHandlers.ofInputStream()) >> nexusResponse
    def exception = thrown(NexusResponseNotReceivedException)
    exception.message == "Unable to receive model, status code from Nexus is: 401"
  }
}
