package com.silenteight.hsbc.bridge.jenkins

import com.silenteight.hsbc.bridge.jenkins.JenkinsModelClient.ModelNotReceivedException

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

  def 'should update transferred model from Governance'() {
    given:
    def modelInfo = fixtures.modelInfo
    def crumbResponse = fixtures.crumbResponse

    def crumbHttpResponse = Mock(HttpResponse) {
      body() >> Fixtures.CRUMB_HTTP_RESPONSE
    }

    def updateModelHttpResponse = Mock(HttpResponse) {
      body() >> Fixtures.UPDATE_MODEL_HTTP_RESPONSE
      statusCode() >> 201
    }

    when:
    underTest.updateModel(modelInfo)

    then:
    1 * httpClient.send({HttpRequest request -> request.method() == 'GET'}, BodyHandlers.ofString()) >> crumbHttpResponse
    1 * httpClient.send({HttpRequest request -> request.method() == 'POST'}, BodyHandlers.ofString()) >> updateModelHttpResponse
    1 * objectMapper.readValue(crumbHttpResponse.body(), _ as TypeReference) >> crumbResponse
    1 * objectMapper.writeValueAsString(modelInfo) >> modelInfo.toString()
  }

  def 'should throw ModelNotReceivedException when unable to receive model with status code other than 201'() {
    given:
    def modelInfo = fixtures.modelInfo
    def crumbResponse = fixtures.crumbResponse

    def crumbHttpResponse = Mock(HttpResponse) {
      body() >> Fixtures.CRUMB_HTTP_RESPONSE
    }

    def updateModelHttpResponse = Mock(HttpResponse) {
      body() >> Fixtures.UPDATE_MODEL_HTTP_RESPONSE
      statusCode() >> 401
    }

    when:
    underTest.updateModel(modelInfo)

    then:
    1 * httpClient.send({HttpRequest request -> request.method() == 'GET'}, BodyHandlers.ofString()) >> crumbHttpResponse
    1 * httpClient.send({HttpRequest request -> request.method() == 'POST'}, BodyHandlers.ofString()) >> updateModelHttpResponse
    1 * objectMapper.readValue(crumbHttpResponse.body(), _ as TypeReference) >> crumbResponse
    1 * objectMapper.writeValueAsString(modelInfo) >> modelInfo.toString()
    def exception = thrown(ModelNotReceivedException)
    exception.message == "Unable to get updated model with status code: 401"
  }

  def 'should send status of transferred model from Governance'() {
    given:
    def modelStatusUpdatedDto = fixtures.modelStatusUpdatedDto
    def crumbResponse = fixtures.crumbResponse

    def crumbHttpResponse = Mock(HttpResponse) {
      body() >> Fixtures.CRUMB_HTTP_RESPONSE
    }

    def modelStatusHttpResponse = Mock(HttpResponse) {
      statusCode() >> 201
    }

    when:
    underTest.sendModelStatus(modelStatusUpdatedDto)

    then:
    1 * httpClient.send({HttpRequest request -> request.method() == 'GET'}, BodyHandlers.ofString()) >> crumbHttpResponse
    1 * httpClient.send({HttpRequest request -> request.method() == 'POST'}, BodyHandlers.ofString()) >> modelStatusHttpResponse
    1 * objectMapper.readValue(crumbHttpResponse.body(), _ as TypeReference) >> crumbResponse
    1 * objectMapper.writeValueAsString(modelStatusUpdatedDto) >> modelStatusUpdatedDto.toString()
  }

  def 'should throw ModelNotReceivedException when unable to send model status with status code other than 201'() {
    def modelStatusUpdatedDto = fixtures.modelStatusUpdatedDto
    def crumbResponse = fixtures.crumbResponse

    def crumbHttpResponse = Mock(HttpResponse) {
      body() >> Fixtures.CRUMB_HTTP_RESPONSE
    }

    def modelStatusHttpResponse = Mock(HttpResponse) {
      statusCode() >> 401
    }

    when:
    underTest.sendModelStatus(modelStatusUpdatedDto)

    then:
    1 * httpClient.send({HttpRequest request -> request.method() == 'GET'}, BodyHandlers.ofString()) >> crumbHttpResponse
    1 * httpClient.send({HttpRequest request -> request.method() == 'POST'}, BodyHandlers.ofString()) >> modelStatusHttpResponse
    1 * objectMapper.readValue(crumbHttpResponse.body(), _ as TypeReference) >> crumbResponse
    1 * objectMapper.writeValueAsString(modelStatusUpdatedDto) >> modelStatusUpdatedDto.toString()
    def exception = thrown(ModelNotReceivedException)
    exception.message == "Unable to send update model status with code: 401"
  }
}
