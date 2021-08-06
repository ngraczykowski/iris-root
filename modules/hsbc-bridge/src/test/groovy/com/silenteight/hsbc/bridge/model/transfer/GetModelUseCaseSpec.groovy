package com.silenteight.hsbc.bridge.model.transfer

import spock.lang.Specification

import static com.silenteight.hsbc.bridge.model.dto.ModelStatus.SUCCESS
import static com.silenteight.hsbc.bridge.model.dto.ModelType.*
import static java.util.Optional.of

class GetModelUseCaseSpec extends Specification {

  def modelRepository = Mock(ModelInformationRepository)
  def underTest = new GetModelUseCase(modelRepository)

  def 'should get IS_PEP_PROCEDURAL model from database'() {
    given:
    def modelInformationEntity = of(
        ModelInformationEntity.builder()
            .name('test_model')
            .minIoUrl('test_url')
            .type(IS_PEP_PROCEDURAL)
            .status(SUCCESS)
            .build())
    def entity = modelInformationEntity.get()

    when:
    def result = underTest.getModel(IS_PEP_PROCEDURAL)

    then:
    1 * modelRepository.findFirstByTypeAndStatusOrderByCreatedAtDesc(IS_PEP_PROCEDURAL, SUCCESS) >>
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
    def modelInformationEntity = of(
        ModelInformationEntity.builder()
            .name('test_model')
            .minIoUrl('test_url')
            .type(IS_PEP_HISTORICAL)
            .status(SUCCESS)
            .build())
    def entity = modelInformationEntity.get()

    when:
    def result = underTest.getModel(IS_PEP_HISTORICAL)

    then:
    1 * modelRepository.findFirstByTypeAndStatusOrderByCreatedAtDesc(IS_PEP_HISTORICAL, SUCCESS) >>
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
    def modelInformationEntity = of(
        ModelInformationEntity.builder()
            .name('test_model')
            .minIoUrl('test_url')
            .type(NAME_ALIASES)
            .status(SUCCESS)
            .build())
    def entity = modelInformationEntity.get()

    when:
    def result = underTest.getModel(NAME_ALIASES)

    then:
    1 * modelRepository.findFirstByTypeAndStatusOrderByCreatedAtDesc(NAME_ALIASES, SUCCESS) >>
        modelInformationEntity

    with(result) {
      name == entity.name
      minIoUrl == entity.minIoUrl
      type == entity.type
      status == entity.status
    }
  }
}
