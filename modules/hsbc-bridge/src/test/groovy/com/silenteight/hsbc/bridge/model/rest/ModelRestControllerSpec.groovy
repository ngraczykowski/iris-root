package com.silenteight.hsbc.bridge.model.rest

import com.silenteight.hsbc.bridge.model.ModelRestController
import com.silenteight.hsbc.bridge.model.ModelServiceClient
import com.silenteight.hsbc.bridge.model.SolvingModelDto
import com.silenteight.hsbc.bridge.model.rest.input.ModelInfoRequest
import com.silenteight.hsbc.bridge.model.rest.input.ModelInfoStatusRequest
import com.silenteight.hsbc.bridge.model.transfer.GovernanceModelManager
import com.silenteight.hsbc.bridge.model.transfer.WorldCheckModelManager

import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import spock.lang.Specification

import static com.silenteight.hsbc.bridge.model.transfer.ModelType.IS_PEP
import static com.silenteight.hsbc.bridge.model.transfer.ModelType.MODEL
import static groovy.json.JsonOutput.toJson
import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup

class ModelRestControllerSpec extends Specification {

  def modelServiceClient = Mock(ModelServiceClient)
  def governanceModelManager = Mock(GovernanceModelManager)
  def worldCheckModelManager = Mock(WorldCheckModelManager)
  def modelManagers = [governanceModelManager, worldCheckModelManager]
  def controller = new ModelRestController(modelManagers, modelServiceClient)
  MockMvc mockMvc = standaloneSetup(controller).build()

  def 'should return SimpleModelResponse'() {
    given:
    def model = createSolvingModelDto()

    when:
    def result = mockMvc.perform(get('/model'))

    then:
    1 * modelServiceClient.getSolvingModel() >> model
    verifyResults(result)
  }

  def 'should update Governance model'() {
    given:
    governanceModelManager.supportsModelType(MODEL) >> true
    def modelInfoRequest = createModelInfoRequest('MODEL', 'MINOR')

    when:
    def result = mockMvc.perform(
        post('/model').contentType(APPLICATION_JSON).content(toJson(modelInfoRequest)))

    then:
    1 * modelManagers.first().transferModelFromJenkins(_ as ModelInfoRequest)
    result.andExpect(status().isOk())
  }

  def 'should send BAD_REQUEST status when model is not supported in Governance during updating model'() {
    given:
    governanceModelManager.supportsModelType(MODEL) >> false
    def modelInfoRequest = createModelInfoRequest('MODEL', 'MINOR')

    when:
    def result = mockMvc.perform(
        post('/model').contentType(APPLICATION_JSON).content(toJson(modelInfoRequest)))

    then:
    result.andExpect(status().isBadRequest())
  }

  def 'should send model status to Governance'() {
    given:
    governanceModelManager.supportsModelType(MODEL) >> true
    def modelInfoStatusRequest = createModelInfoStatusRequest('MODEL', 'SUCCESS')

    when:
    def result = mockMvc.perform(
        put('/model').contentType(APPLICATION_JSON).content(toJson(modelInfoStatusRequest)))

    then:
    1 * modelManagers.first().transferModelStatus(_ as ModelInfoStatusRequest)
    result.andExpect(status().isOk())
  }

  def 'should send BAD_REQUEST status when model is not supported in Governance during sending model status'() {
    given:
    governanceModelManager.supportsModelType(MODEL) >> false
    def modelInfoStatusRequest = createModelInfoStatusRequest('MODEL', 'SUCCESS')

    when:
    def result = mockMvc.perform(
        put('/model').contentType(APPLICATION_JSON).content(toJson(modelInfoStatusRequest)))

    then:
    result.andExpect(status().isBadRequest())
  }

  def 'should update WorldCheck model'() {
    given:
    worldCheckModelManager.supportsModelType(IS_PEP) >> true
    def modelInfoRequest = createModelInfoRequest('IS_PEP', 'MAJOR')

    when:
    def result = mockMvc.perform(
        post('/model').contentType(APPLICATION_JSON).content(toJson(modelInfoRequest)))

    then:
    1 * modelManagers.get(1).transferModelFromJenkins(_ as ModelInfoRequest)
    result.andExpect(status().isOk())
  }

  def 'should send BAD_REQUEST status when model is not supported in WorldCheck during updating model'() {
    given:
    worldCheckModelManager.supportsModelType(IS_PEP) >> false
    def modelInfoRequest = createModelInfoRequest('IS_PEP', 'MAJOR')

    when:
    def result = mockMvc.perform(
        post('/model').contentType(APPLICATION_JSON).content(toJson(modelInfoRequest)))

    then:
    result.andExpect(status().isBadRequest())
  }

  def 'should send model status to WorldCheck'() {
    given:
    worldCheckModelManager.supportsModelType(IS_PEP) >> true
    def modelInfoStatusRequest = createModelInfoStatusRequest('IS_PEP', 'SUCCESS')

    when:
    def result = mockMvc.perform(
        put('/model').contentType(APPLICATION_JSON).content(toJson(modelInfoStatusRequest)))

    then:
    1 * modelManagers.get(1).transferModelStatus(_ as ModelInfoStatusRequest)
    result.andExpect(status().isOk())
  }

  def 'should send BAD_REQUEST status when model is not supported in WorldCheck during sending model status'() {
    given:
    worldCheckModelManager.supportsModelType(IS_PEP) >> false
    def modelInfoStatusRequest = createModelInfoStatusRequest('IS_PEP', 'SUCCESS')

    when:
    def result = mockMvc.perform(
        put('/model').contentType(APPLICATION_JSON).content(toJson(modelInfoStatusRequest)))

    then:
    result.andExpect(status().isBadRequest())
  }

  def static verifyResults(ResultActions results) {
    results.andExpect(status().isOk())
    results.andExpect(content().contentType(APPLICATION_JSON))
    results.andExpect(content().string('{"name":"name","policyName":"policy"}'))
  }

  def static createSolvingModelDto() {
    return SolvingModelDto.builder()
        .name('name')
        .policyName('policy')
        .build()
  }

  static ModelInfoRequest createModelInfoRequest(String modelType, String changeType) {
    new ModelInfoRequest(
        "name": 'model_name',
        "url": 'https://jenkins.silenteight.com/model/update',
        "type": modelType,
        "changeType": changeType
    )
  }

  static ModelInfoStatusRequest createModelInfoStatusRequest(String modelType, String status) {
    new ModelInfoStatusRequest(
        "name": 'model_name',
        "url": 'https://jenkins.silenteight.com/model/status',
        "type": modelType,
        "status": status
    )
  }
}
