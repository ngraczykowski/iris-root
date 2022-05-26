package com.silenteight.scb.ingest.adapter.incomming.common.rest

import com.silenteight.scb.ingest.adapter.incomming.common.rest.GlobalHealthEndpointProperties.Service

import org.springframework.cloud.client.DefaultServiceInstance
import org.springframework.cloud.client.discovery.simple.SimpleDiscoveryClient
import org.springframework.cloud.client.discovery.simple.SimpleDiscoveryProperties
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus

class GlobalHealthEndpointSpec extends Specification {

  def restTemplate = new RestTemplate()

  def mockServer = MockRestServiceServer.bindTo(restTemplate).build()

  def 'should parse info and health endpoints'() {
    given:

    def sdp = new SimpleDiscoveryProperties()
    sdp.setInstances(
        ["lima-universal-data-source": [new DefaultServiceInstance("", "", "someHost", 42, false)]])

    def config = new GlobalHealthEndpointProperties(
        [new Service("universal-data-source", "/rest/uds")])

    def underTest = new GlobalHealthEndpoint(
        restTemplate, new SimpleDiscoveryClient(sdp), "lima", config)

    when:

    mockServer
        .expect(requestTo(new URI("http://someHost:42/rest/uds/management/info")))
        .andExpect(method(HttpMethod.GET))
        .andRespond(
            withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                    """
{
  "git": {
    "branch": "master",
    "commit": {
      "id": "76d1edc",
      "time": "2022-03-04T16:36:35Z"
    }
  },
  "build": {
    "artifact": "universal-data-source-app",
    "name": "Universal Data Source",
    "version": "1.9.0",
    "group": "com.silenteight.universaldatasource"
  }
}
"""))

    mockServer
        .expect(requestTo(new URI("http://someHost:42/rest/uds/management/health")))
        .andExpect(method(HttpMethod.GET))
        .andRespond(
            withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                    """
{
  "status": "UP"
}
"""))

    def data = underTest.health()

    then:
    mockServer.verify()
    data == ["universal-data-source": [
        [status: "UP",
         actuator_uri: "http://someHost:42/rest/uds/management",
         artefact: "com.silenteight.universaldatasource:universal-data-source-app:1.9.0",
         commit_time: "2022-03-04T16:36:35Z"]
    ]]
  }
}
