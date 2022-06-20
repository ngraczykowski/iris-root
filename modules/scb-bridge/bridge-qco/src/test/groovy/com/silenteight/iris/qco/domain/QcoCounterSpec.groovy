/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.qco.domain

import spock.lang.Specification
import spock.lang.Subject

class QcoCounterSpec extends Specification {

  def configurationHolder = Mock(QcoConfigurationHolder)

  @Subject
  def underTest = new QcoCounter(configurationHolder)

  def "increase and overflow"() {
    given:
    def qcoConfigurations = [(Fixtures.CHANGE_CONDITION): Fixtures.QCO_PARAM_1]

    when:
    underTest.init();
    def result = underTest.increaseAndCheckOverflow(Fixtures.CHANGE_CONDITION)

    then:
    1 * configurationHolder.getConfiguration() >> qcoConfigurations
    result == true
  }

  def "twice increase and overflow"() {
    given:
    def qcoConfigurations = [(Fixtures.CHANGE_CONDITION): Fixtures.QCO_PARAM_2]

    when:
    underTest.init();
    underTest.increaseAndCheckOverflow(Fixtures.CHANGE_CONDITION)
    def result = underTest.increaseAndCheckOverflow(Fixtures.CHANGE_CONDITION)

    then:
    1 * configurationHolder.getConfiguration() >> qcoConfigurations
    result == true
  }

  def "it should reset counter after overflow"() {
    given:
    def qcoConfigurations = [(Fixtures.CHANGE_CONDITION): Fixtures.QCO_PARAM_2]

    when:
    underTest.init();
    underTest.increaseAndCheckOverflow(Fixtures.CHANGE_CONDITION)
    underTest.increaseAndCheckOverflow(Fixtures.CHANGE_CONDITION)
    def result = underTest.increaseAndCheckOverflow(Fixtures.CHANGE_CONDITION)

    then:
    1 * configurationHolder.getConfiguration() >> qcoConfigurations
    result == false
  }

  def "increase without overflow"() {
    given:
    def qcoConfigurations = [(Fixtures.CHANGE_CONDITION): Fixtures.QCO_PARAM_2]

    when:
    underTest.init();
    def result = underTest.increaseAndCheckOverflow(Fixtures.CHANGE_CONDITION)

    then:
    1 * configurationHolder.getConfiguration() >> qcoConfigurations
    result == false
  }

  def "increase without overflow while It was encountered unknown policy"() {
    given:
    def qcoConfigurations = [(Fixtures.CHANGE_CONDITION): Fixtures.QCO_PARAM_2]

    when:
    underTest.init();
    def result = underTest.increaseAndCheckOverflow(Fixtures.UNEXPECTED_CHANGE_CONDITION)

    then:
    1 * configurationHolder.getConfiguration() >> qcoConfigurations
    result == false
  }
}
