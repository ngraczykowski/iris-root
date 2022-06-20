/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.qco.domain

import spock.lang.Specification
import spock.lang.Subject

class QcoConfigurationHolderSpec extends Specification {

  def configurationProvider = Mock(SolutionConfigurationProvider)

  @Subject
  def underTest = new QcoConfigurationHolder(configurationProvider)

  def "map delivered configuration to QcoMapConfiguration with duplicate values of making key"() {
    given:
    def qcoConfigurations = []
    qcoConfigurations << Fixtures.QCO_CONFIGURATION_1
    qcoConfigurations << Fixtures.QCO_CONFIGURATION_2
    qcoConfigurations << Fixtures.QCO_CONFIGURATION_DUPLICATED_VALUES

    when:
    underTest.init();
    def result = underTest.getConfiguration()

    then:
    1 * configurationProvider.getSolutionConfigurations() >> qcoConfigurations
    result.size() == 2
  }
}
