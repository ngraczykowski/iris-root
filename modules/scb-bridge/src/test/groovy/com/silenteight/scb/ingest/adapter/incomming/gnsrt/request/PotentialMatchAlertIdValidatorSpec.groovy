package com.silenteight.scb.ingest.adapter.incomming.gnsrt.request

import com.silenteight.scb.ingest.adapter.incomming.gnsrt.model.GnsRtAlertStatus
import com.silenteight.scb.ingest.adapter.incomming.gnsrt.model.request.GnsRtAlert
import com.silenteight.scb.ingest.adapter.incomming.gnsrt.model.request.PotentialMatchAlertIdValidator

import spock.lang.Specification
import spock.lang.Unroll

import javax.validation.ConstraintValidatorContext

class PotentialMatchAlertIdValidatorSpec extends Specification {

  def someContext = Mock(ConstraintValidatorContext)
  def objectUnderTest = new PotentialMatchAlertIdValidator()

  def 'should not potential match be valid'() {
    given:
    def someStatusThatIsNotPotentialMatch = GnsRtAlertStatus.MATCH
    def someWrongAlertId = ''
    def alert = createAlert(someWrongAlertId, someStatusThatIsNotPotentialMatch)

    when:
    def result = objectUnderTest.isValid(alert, someContext)

    then:
    result
  }

  @Unroll
  def 'return isValid=#expectedResult for list containing any potential match with #input'() {
    given:
    def alert = createAlert(input, GnsRtAlertStatus.POTENTIAL_MATCH)

    expect:
    objectUnderTest.isValid(alert, someContext) == expectedResult

    where:
    input                                               | expectedResult
    null                                                | false
    ''                                                  | false
    'a'                                                 | false
    '!'                                                 | false
    '!a'                                                | false
    'US_PERD_DUDL!'                                     | false
    'a!b'                                               | false
    'US_PERD_DUDL!8A1DFAFE-EA1041B0-9F1426E6-DBD75121|' | false
    'VN_0981927382_HCVN!Sanctions'                      | false
    'US_PERD_DUDL!8A1DFAWE-EA1041B0-9F1426E6-DBD75121'  | false
    'VN_SCICPEPL!FFE16928-74B047CE-ACED1346-63AA9A19'   | false

    'US_PERD_DUDL!8A1DFAFE-EA1041B0-9F1426E6-DBD75121'  | true
    'VN_SCIC_PEPL!FFE16928-74B047CE-ACED1346-63AA9A19'  | true

    'XX_SCIC_PEPL!FFE16928-74B047CE-ACED1346-63AA9A19'  | false
  }

  def createAlert(String alertId, GnsRtAlertStatus status) {
    new GnsRtAlert(alertId: alertId, alertStatus: status)
  }
}
