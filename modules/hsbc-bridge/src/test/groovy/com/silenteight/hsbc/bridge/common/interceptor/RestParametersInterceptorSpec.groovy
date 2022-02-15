package com.silenteight.hsbc.bridge.common.interceptor

import com.silenteight.hsbc.BaseSpecificationIT
import com.silenteight.hsbc.bridge.BridgeApplication
import com.silenteight.hsbc.bridge.model.rest.output.SimpleModelResponse
import com.silenteight.hsbc.datasource.category.dto.CategoryDto

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext

@DirtiesContext
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = BridgeApplication)
class RestParametersInterceptorSpec extends BaseSpecificationIT {

  @Autowired
  private TestRestTemplate restTemplate

  def 'should process request without checking parameter pollution'() {
    when:
    def result = restTemplate.getForEntity("/model", SimpleModelResponse.class)

    then:
    noExceptionThrown()
    result.getStatusCode() == HttpStatus.OK
  }

  def "should fail on http parameter pollution check - for non existing path"() {
    when:
    def result = restTemplate
        .getForEntity("/test/path?test=value1&test=value2", SimpleModelResponse.class)

    then:
    result.getStatusCode() == HttpStatus.NOT_FOUND
  }


  def "should fail on http parameter pollution check - for existing path but not allowed method"() {
    when:
    def result = restTemplate
        .postForEntity("/categories?test=value1&test=value2", null, List<CategoryDto>.class)

    then:
    result.getStatusCode() == HttpStatus.METHOD_NOT_ALLOWED
  }
}
