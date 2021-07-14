package com.silenteight.hsbc.bridge.model.transfer

import com.silenteight.hsbc.bridge.model.dto.ModelType

import spock.lang.Specification

class GetModelUseCaseSpec extends Specification {

  def modelRepository = Mock(ModelInformationRepository)
  def underTest = new GetModelUseCase(modelRepository)

  def 'should get IS_PEP_PROCEDURAL model from database'() {
    given:
    def modelInformationEntity = ModelInformationEntity.builder()
        .name('test_model')
        .minIoUrl('test_url')
        .type(ModelType.IS_PEP_PROCEDURAL)
        .build()

    when:
    def result = underTest.getModel(ModelType.IS_PEP_PROCEDURAL)

    then:
    1 * modelRepository.findFirstByTypeOrderByCreatedAtDesc(ModelType.IS_PEP_PROCEDURAL) >>
        Optional.of(modelInformationEntity)

    with(result) {
      name == modelInformationEntity.name
      minIoUrl == modelInformationEntity.minIoUrl
      type == modelInformationEntity.type
    }
  }

  def 'should get IS_PEP_HISTORICAL model from database'() {
    given:
    def modelInformationEntity = ModelInformationEntity.builder()
        .name('test_model')
        .minIoUrl('test_url')
        .type(ModelType.IS_PEP_HISTORICAL)
        .build()

    when:
    def result = underTest.getModel(ModelType.IS_PEP_HISTORICAL)

    then:
    1 * modelRepository.findFirstByTypeOrderByCreatedAtDesc(ModelType.IS_PEP_HISTORICAL) >>
        Optional.of(modelInformationEntity)

    with(result) {
      name == modelInformationEntity.name
      minIoUrl == modelInformationEntity.minIoUrl
      type == modelInformationEntity.type
    }
  }

  def 'should get NAME_ALIASES model from database'() {
    given:
    def modelInformationEntity = ModelInformationEntity.builder()
        .name('test_model')
        .minIoUrl('test_url')
        .type(ModelType.NAME_ALIASES)
        .build()

    when:
    def result = underTest.getModel(ModelType.NAME_ALIASES)

    then:
    1 * modelRepository.findFirstByTypeOrderByCreatedAtDesc(ModelType.NAME_ALIASES) >>
        Optional.of(modelInformationEntity)

    with(result) {
      name == modelInformationEntity.name
      minIoUrl == modelInformationEntity.minIoUrl
      type == modelInformationEntity.type
    }
  }
}
