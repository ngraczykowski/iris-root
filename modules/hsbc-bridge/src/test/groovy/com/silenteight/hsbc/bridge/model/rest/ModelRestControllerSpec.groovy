package com.silenteight.hsbc.bridge.model.rest

import com.silenteight.hsbc.bridge.model.ModelRestController
import com.silenteight.hsbc.bridge.model.ModelServiceClient
import com.silenteight.hsbc.bridge.model.dto.ModelDetails
import com.silenteight.hsbc.bridge.model.dto.ModelStatusUpdatedDto
import com.silenteight.hsbc.bridge.model.dto.ModelType
import com.silenteight.hsbc.bridge.model.dto.SolvingModelDto
import com.silenteight.hsbc.bridge.model.rest.input.ModelInfoRequest
import com.silenteight.hsbc.bridge.model.rest.input.ModelInfoStatusRequest
import com.silenteight.hsbc.bridge.model.transfer.GovernanceModelManager
import com.silenteight.hsbc.bridge.model.transfer.WorldCheckModelManager

import groovy.json.JsonOutput
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import java.nio.charset.StandardCharsets

class ModelRestControllerSpec extends Specification {

  def modelServiceClient = Mock(ModelServiceClient)
  def governanceModelManager = Mock(GovernanceModelManager)
  def worldCheckModelManager = Mock(WorldCheckModelManager)
  def modelManagers = [governanceModelManager, worldCheckModelManager]
  def controller = new ModelRestController(modelManagers, modelServiceClient)
  MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build()

  def 'should return SimpleModelResponse'() {
    given:
    def model = createSolvingModelDto()

    when:
    def result = mockMvc.perform(MockMvcRequestBuilders.get('/model'))

    then:
    1 * modelServiceClient.getSolvingModel() >> model
    verifyStringResults(result, '{"name":"name","policyName":"policy"}')
  }

  def 'should export governance model'() {
    given:
    governanceModelManager.supportsModelType(ModelType.MODEL) >> true
    def version = "20210801082734"
    def type = "MODEL"
    def modelDetails = createModelDetails(type, version)
    def exportModelResponse = createExportModelResponse()

    when:
    def result = mockMvc.perform(MockMvcRequestBuilders.get('/model/export/' + type + '/' + version))

    then:
    1 * modelManagers.first().exportModel(modelDetails) >> exportModelResponse
    verifyResults(result, '{"modelJson":"20210801082734"}')
  }

  def 'should send BAD_REQUEST status when model is not supported in Governance during updating model'() {
    given:
    governanceModelManager.supportsModelType(ModelType.MODEL) >> false
    def modelInfoRequest = createModelInfoRequest('MODEL', 'MINOR')

    when:
    def result = mockMvc.perform(
        MockMvcRequestBuilders.post('/model').contentType(MediaType.APPLICATION_JSON)
            .content(JsonOutput.toJson(modelInfoRequest)))

    then:
    result.andExpect(MockMvcResultMatchers.status().isBadRequest())
  }

  def 'should send model status to Governance'() {
    given:
    governanceModelManager.supportsModelType(ModelType.MODEL) >> true
    def modelInfoRequest = createModelInfoRequest('MODEL', 'MAJOR')
    governanceModelManager.transferModelFromJenkins(modelInfoRequest) >> createModelStatusUpdatedDto()

    when:
    def result = mockMvc.perform(
        MockMvcRequestBuilders.post('/model').contentType(MediaType.APPLICATION_JSON)
            .content(JsonOutput.toJson(modelInfoRequest)))

    then:
    1 * modelManagers.first().transferModelStatus(_ as ModelInfoRequest)
    result.andExpect(MockMvcResultMatchers.status().isOk())
  }

  def 'should send BAD_REQUEST status when model is not supported in Governance during sending model status'() {
    given:
    governanceModelManager.supportsModelType(ModelType.MODEL) >> false
    def modelInfoRequest = createModelInfoRequest('MODEL', 'MAJOR')

    when:
    def result = mockMvc.perform(
        MockMvcRequestBuilders.post('/model').contentType(MediaType.APPLICATION_JSON)
            .content(JsonOutput.toJson(modelInfoRequest)))

    then:
    result.andExpect(MockMvcResultMatchers.status().isBadRequest())
  }

  def 'should update WorldCheck model'() {
    given:
    worldCheckModelManager.supportsModelType(ModelType.IS_PEP_PROCEDURAL) >> true
    def modelInfoRequest = createModelInfoRequest('IS_PEP_PROCEDURAL', 'MAJOR')
    worldCheckModelManager.transferModelFromJenkins(modelInfoRequest) >> createModelStatusUpdatedDto()

    when:
    def result = mockMvc.perform(
        MockMvcRequestBuilders.post('/model').contentType(MediaType.APPLICATION_JSON)
            .content(JsonOutput.toJson(modelInfoRequest)))

    then:
    1 * modelManagers.get(1).transferModelStatus(_ as ModelInfoRequest)
    result.andExpect(MockMvcResultMatchers.status().isOk())
  }

  def 'should export WorldCheck model'() {
    given:
    worldCheckModelManager.supportsModelType(ModelType.IS_PEP_PROCEDURAL) >> true
    def version = "20210801082734"
    def type = "IS_PEP_PROCEDURAL"
    def modelDetails = createModelDetails(type, version)
    def exportModelResponse = createExportModelResponse()

    when:
    def result = mockMvc.perform(MockMvcRequestBuilders.get('/model/export/' + type + '/' + version))

    then:
    1 * modelManagers.get(1).exportModel(modelDetails) >> exportModelResponse
    verifyResults(result, '{"modelJson":"20210801082734"}')
  }

  def 'should send model status to WorldCheck'() {
    given:
    worldCheckModelManager.supportsModelType(ModelType.IS_PEP_PROCEDURAL) >> true
    def modelInfoRequest = createModelInfoRequest('IS_PEP_PROCEDURAL', 'MINOR')
    worldCheckModelManager.transferModelFromJenkins(modelInfoRequest) >> createModelStatusUpdatedDto()

    when:
    def result = mockMvc.perform(
        MockMvcRequestBuilders.post('/model').contentType(MediaType.APPLICATION_JSON)
            .content(JsonOutput.toJson(modelInfoRequest)))

    then:
    1 * modelManagers.get(1).transferModelStatus(_ as ModelInfoRequest)
    result.andExpect(MockMvcResultMatchers.status().isOk())
  }

  def 'should send BAD_REQUEST status when model is not supported in WorldCheck during updating and sending model status'() {
    given:
    worldCheckModelManager.supportsModelType(ModelType.IS_PEP_PROCEDURAL) >> false
    def modelInfoRequest = createModelInfoRequest('IS_PEP_PROCEDURAL', 'MINOR')

    when:
    def result = mockMvc.perform(
        MockMvcRequestBuilders.post('/model').contentType(MediaType.APPLICATION_JSON)
            .content(JsonOutput.toJson(modelInfoRequest)))

    then:
    result.andExpect(MockMvcResultMatchers.status().isBadRequest())
  }

  def static verifyStringResults(ResultActions results, String result) {
    results.andExpect(MockMvcResultMatchers.status().isOk())
    results.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
    results.andExpect(MockMvcResultMatchers.content().string(result))
  }

  def static verifyResults(ResultActions results, String result) {
    results.andExpect(MockMvcResultMatchers.status().isOk())
    results.andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
    results.andExpect(MockMvcResultMatchers.content().string(result))
  }

  def static createSolvingModelDto() {
    return SolvingModelDto.builder()
        .name('name')
        .policyName('policy')
        .build()
  }

  def static createModelDetails(String type, String version) {
    return ModelDetails.builder()
        .type(type)
        .version(version)
        .build()
  }

  def static createExportModelResponse() {
    return '{"modelJson":"20210801082734"}'.getBytes(StandardCharsets.UTF_8)
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

  static ModelStatusUpdatedDto createModelStatusUpdatedDto() {
    ModelStatusUpdatedDto.builder()
            .name("name")
            .url("http://localhost:9000/MODEL.json")
            .type("MODEL")
            .status("SUCCESS")
            .build()
  }
}
