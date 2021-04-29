package com.silenteight.hsbc.bridge.jenkins

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import spock.lang.Specification

import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.net.http.HttpResponse.BodyHandlers

class JenkinsModelClientSpec extends Specification {

  def fixtures = new Fixtures()
  def objectMapper = Mock(ObjectMapper)
  def httpClient = Mock(HttpClient)
  def jenkinsApiProperties = fixtures.jenkinsApiProperties

  def underTest = new JenkinsModelClient(objectMapper, httpClient, jenkinsApiProperties)

  def 'should get transferred model from Governance'() {
    given:
    def modelInfo = fixtures.modelInfo
    def crumbResponse = fixtures.crumbResponse
    def modelUpdateResponse = fixtures.model

    def crumbHttpResponse = Mock(HttpResponse) {
      body() >> Fixtures.CRUMB_HTTP_RESPONSE
    }

    def updateModelHttpResponse = Mock(HttpResponse) {
      body() >> Fixtures.UPDATE_MODEL_HTTP_RESPONSE
    }

    when:
    def model = underTest.getModel(modelInfo)

    then:
    1 * httpClient.send({HttpRequest request -> request.method() == 'GET'}, BodyHandlers.ofString()) >> crumbHttpResponse
    1 * httpClient.send({HttpRequest request -> request.method() == 'POST'}, BodyHandlers.ofString()) >> updateModelHttpResponse
    1 * objectMapper.readValue(crumbHttpResponse.body(), _ as TypeReference) >> crumbResponse
    1 * objectMapper.readValue(updateModelHttpResponse.body(), _ as TypeReference) >> modelUpdateResponse
    1 * objectMapper.writeValueAsString(modelInfo) >> modelInfo.toString()
    model
  }
}
