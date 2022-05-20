package com.silenteight.hsbc.bridge.model.transfer

import com.silenteight.hsbc.bridge.jenkins.Fixtures
import com.silenteight.hsbc.bridge.jenkins.JenkinsModelClient
import com.silenteight.hsbc.bridge.model.dto.ModelType

import spock.lang.Specification

class ExportModelJobsSpec extends Specification {

  def fixtures = new Fixtures()
  def address = fixtures.bridgeApiProperties.getAddress()
  def jenkinsModelClient = Mock(JenkinsModelClient)
  def getModelUseCase = Mock(GetModelUseCase)

  def underTest = new ExportModelJobs(address, jenkinsModelClient, getModelUseCase)

  def 'should run exportNameModel job'() {
    given:
    def modelInformation = createModelInformationEntity(
        fixtures.testModelVersion,
        fixtures.testWorldCheckNameAliasesVersionUrl,
        ModelType.NAME_ALIASES
    )
    def modelInfo = createModelInformation(modelInformation, address)

    when:
    underTest.exportNameModel()

    then:
    1 * getModelUseCase.getModel(ModelType.NAME_ALIASES) >> modelInformation
    1 * jenkinsModelClient.updateModel(modelInfo)
  }

  def 'should run exportIsPepProceduralModel job'() {
    given:
    def modelInformation = createModelInformationEntity(
        fixtures.testModelVersion,
        fixtures.testWorldCheckIsPepProceduralVersionUrl,
        ModelType.IS_PEP_PROCEDURAL
    )
    def modelInfo = createModelInformation(modelInformation, address)

    when:
    underTest.exportIsPepProceduralModel()

    then:
    1 * getModelUseCase.getModel(ModelType.IS_PEP_PROCEDURAL) >> modelInformation
    1 * jenkinsModelClient.updateModel(modelInfo)
  }

  def 'should run exportIsPepHistoricalModel job'() {
    given:
    def modelInformation = createModelInformationEntity(
        fixtures.testModelVersion,
        fixtures.testWorldCheckIsPepHistoricalVersionUrl,
        ModelType.IS_PEP_HISTORICAL
    )
    def modelInfo = createModelInformation(modelInformation, address)

    when:
    underTest.exportIsPepHistoricalModel()

    then:
    1 * getModelUseCase.getModel(ModelType.IS_PEP_HISTORICAL) >> modelInformation
    1 * jenkinsModelClient.updateModel(modelInfo)
  }

  private static createModelInformation(ModelInformationEntity modelInformation, String address) {
    ModelInfoCreator.of(modelInformation).create(address)
  }

  private static createModelInformationEntity(String name, String minIoUrl, ModelType type) {
    ModelInformationEntity.builder()
        .name(name)
        .minIoUrl(minIoUrl)
        .type(type)
        .build()
  }
}
