package com.silenteight.hsbc.bridge.model.rest.output

import com.silenteight.hsbc.bridge.model.ModelServiceClient
import com.silenteight.hsbc.bridge.model.SolvingModelDto
import com.silenteight.hsbc.bridge.model.rest.ModelRestController

import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup

class ModelRestControllerSpec extends Specification {

  def modelServiceClient = Mock(ModelServiceClient)
  def controller = new ModelRestController(modelServiceClient)
  MockMvc mockMvc = standaloneSetup(controller).build()
  def model = createSolvingModelDto()

  def 'should return SimpleModelResponse'() {
    when:
    def result = mockMvc.perform(get('/model'))

    then:
    1 * modelServiceClient.getSolvingModel() >> model
    verifyResults(result)
  }

  def static verifyResults(ResultActions results) {
    results.andExpect(status().isOk())
    results.andExpect(content().contentType(MediaType.APPLICATION_JSON))
    results.andExpect(content().string('{"name":"name","policyName":"policy"}'))
  }

  def static createSolvingModelDto() {
    return SolvingModelDto.builder()
        .name("name")
        .policyName("policy")
        .build();
  }
}
