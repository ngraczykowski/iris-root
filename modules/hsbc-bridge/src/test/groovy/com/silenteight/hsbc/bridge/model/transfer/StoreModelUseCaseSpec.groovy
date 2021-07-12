package com.silenteight.hsbc.bridge.model.transfer

import spock.lang.Specification

class StoreModelUseCaseSpec extends Specification {

  def modelRepository = Mock(ModelInformationRepository)
  def underTest = new StoreModelUseCase(modelRepository)

  def 'should save model'() {
    given:
    def modelStatusUpdated = new ModelStatusUpdatedDto(
        'test_model', 'test_url', 'IS_PEP_PROCEDURAL', 'SUCCESS')

    when:
    underTest.storeModel(modelStatusUpdated)

    then:
    1 * modelRepository.save(_ as ModelInformationEntity)
  }
}
