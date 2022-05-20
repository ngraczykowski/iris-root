package com.silenteight.hsbc.bridge.model.transfer

import com.silenteight.hsbc.bridge.model.dto.ModelStatus
import com.silenteight.hsbc.bridge.model.dto.ModelType

import spock.lang.Specification

class GetModelUseCaseSpec extends Specification {

  def modelRepository = Mock(ModelInformationRepository)
  def underTest = new GetModelUseCase(modelRepository)

  def 'should get IS_PEP_PROCEDURAL model from database'() {
    given:
    def modelInformationEntity = Optional.of(
        ModelInformationEntity.builder()
            .name('test_model')
            .minIoUrl('test_url')
            .type(ModelType.IS_PEP_PROCEDURAL)
            .status(ModelStatus.SUCCESS)
            .build())
    def entity = modelInformationEntity.get()

    when:
    def result = underTest.getModel(ModelType.IS_PEP_PROCEDURAL)

    then:
    1 * modelRepository
        .findFirstByTypeAndStatusOrderByCreatedAtDesc(ModelType.IS_PEP_PROCEDURAL, ModelStatus.SUCCESS) >>
        modelInformationEntity

    with(result) {
      name == entity.name
      minIoUrl == entity.minIoUrl
      type == entity.type
      status == entity.status
    }
  }

  def 'should get IS_PEP_HISTORICAL model from database'() {
    given:
    def modelInformationEntity = Optional.of(
        ModelInformationEntity.builder()
            .name('test_model')
            .minIoUrl('test_url')
            .type(ModelType.IS_PEP_HISTORICAL)
            .status(ModelStatus.SUCCESS)
            .build())
    def entity = modelInformationEntity.get()

    when:
    def result = underTest.getModel(ModelType.IS_PEP_HISTORICAL)

    then:
    1 * modelRepository
        .findFirstByTypeAndStatusOrderByCreatedAtDesc(ModelType.IS_PEP_HISTORICAL, ModelStatus.SUCCESS) >>
        modelInformationEntity

    with(result) {
      name == entity.name
      minIoUrl == entity.minIoUrl
      type == entity.type
      status == entity.status
    }
  }

  def 'should get NAME_ALIASES model from database'() {
    given:
    def modelInformationEntity = Optional.of(
        ModelInformationEntity.builder()
            .name('test_model')
            .minIoUrl('test_url')
            .type(ModelType.NAME_ALIASES)
            .status(ModelStatus.SUCCESS)
            .build())
    def entity = modelInformationEntity.get()

    when:
    def result = underTest.getModel(ModelType.NAME_ALIASES)

    then:
    1 * modelRepository.findFirstByTypeAndStatusOrderByCreatedAtDesc(ModelType.NAME_ALIASES, ModelStatus.SUCCESS) >>
        modelInformationEntity

    with(result) {
      name == entity.name
      minIoUrl == entity.minIoUrl
      type == entity.type
      status == entity.status
    }
  }
}
