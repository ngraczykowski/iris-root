package com.silenteight.hsbc.bridge.report

import spock.lang.Specification

class DataIndexRequestCreatorSpec extends Specification {

  def alert = Mock(Alert)

  def "Should create DataIndexRequest object"(){
    given:
    alert.getMetadata() >> metadata
    alert.getMatches() >> []
    alert.getName() >> "alertName"
    alert.getDiscriminator() >> "someDiscriminator"

    when:
    def result = DataIndexRequestCreator.create([alert])

    then:
    with (result.alertsList.first()) {
      payload.fieldsCount == 5
      name == 'alertName'
      discriminator == 'someDiscriminator'
    }
  }

  static Map<String, String> metadata = [
      "id": "someId",
      "status": "ERROR",
      "errorMessage": "someError",
      "recommendation": "someRecommendation",
      "comment": "someComment",
      "fvSignature": null,
      "policyId": null,
      "stepId": null
  ] as Map
}
