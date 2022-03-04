package com.silenteight.customerbridge.gnsrt.request

import com.silenteight.customerbridge.gnsrt.model.GnsRtAlertStatus
import com.silenteight.customerbridge.gnsrt.model.request.GnsRtAlert
import com.silenteight.customerbridge.gnsrt.model.request.PotentialMatchAlertIdValidator

import spock.lang.Specification
import spock.lang.Unroll

import javax.validation.ConstraintValidatorContext

import static com.silenteight.customerbridge.gnsrt.model.GnsRtAlertStatus.POTENTIAL_MATCH

class PotentialMatchAlertIdValidatorSpec extends Specification {

  def someContext = Mock(ConstraintValidatorContext)
  def objectUnderTest = new PotentialMatchAlertIdValidator()

  def 'should empty list be valid'() {
    when:
    def result = objectUnderTest.isValid([], someContext)

    then:
    result
  }

  def 'should list without potential matches be valid'() {
    given:
    def someStatusThatIsNotPotentialMatch = GnsRtAlertStatus.MATCH
    def someWrongAlertId = ''
    def list = [createAlert(someWrongAlertId, someStatusThatIsNotPotentialMatch)]

    when:
    def result = objectUnderTest.isValid(list, someContext)

    then:
    result
  }

  @Unroll
  def 'return isValid=#expectedResult for list containing any potential match with #input'() {
    given:
    def list = [createAlert(input, POTENTIAL_MATCH)]

    when:
    def result = objectUnderTest.isValid(list, someContext)

    then:
    result == expectedResult

    where:
    input                                               | expectedResult
    null                                                | false
    ''                                                  | false
    'a'                                                 | false
    '!'                                                 | false
    '!a'                                                | false
    'US_PERD_DUDL!'                                     | false
    'a!b'                                               | true
    'US_PERD_DUDL!8A1DFAFE-EA1041B0-9F1426E6-DBD75121|' | true
  }

  def createAlert(String alertId, GnsRtAlertStatus status) {
    new GnsRtAlert(alertId: alertId, alertStatus: status)
  }
}
