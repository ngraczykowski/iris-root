package com.silenteight.payments.bridge.svb.learning.step.reetl

import com.silenteight.payments.bridge.datasource.DefaultFeatureInputSpecification
import com.silenteight.payments.bridge.datasource.IndicatedFeatureInputSpecification

import spock.lang.Specification

class FeatureInputSpecificationStrategySpec extends Specification {

  def "When no feature input is indicated "() {

    given:
    def determinedFeatureInputs = Collections.emptyList() as List<String>
    when:
    def result = FeatureInputSpecificationStrategy.INSTANCE.chooseSpecification(
        determinedFeatureInputs)
    then:
    noExceptionThrown()
    result instanceof DefaultFeatureInputSpecification
  }

  def "When  feature input is indicated "() {

    given:
    def determinedFeatureInputs = ["feature1", "feature2"] as List<String>
    when:
    def result = FeatureInputSpecificationStrategy.INSTANCE.chooseSpecification(
        determinedFeatureInputs
    )
    then:
    noExceptionThrown()
    result instanceof IndicatedFeatureInputSpecification
  }
}
