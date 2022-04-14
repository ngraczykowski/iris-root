package com.silenteight.qco.domain

import com.silenteight.qco.domain.model.ChangeCondition
import com.silenteight.qco.domain.model.ResolverCommand

import spock.lang.Specification
import spock.lang.Subject

import static com.silenteight.qco.domain.Fixtures.QCO_RECOMMENDATION_MATCH
import static com.silenteight.qco.domain.model.ResolverAction.NOT_CHANGE
import static com.silenteight.qco.domain.model.ResolverAction.OVERRIDE

class MatchResolverSpec extends Specification {

  def matchRegister = Mock(ProcessedMatchesRegister)
  def configurationHolder = Mock(QcoConfigurationHolder)

  @Subject
  def underTest = new MatchOverridingResolver(matchRegister, configurationHolder)

  def "AlertResolver should override match's solution"() {
    given:
    def match = QCO_RECOMMENDATION_MATCH
    def condition = Fixtures.CHANGE_CONDITION
    def command = new ResolverCommand(match, condition, OVERRIDE)
    def qcoConfigurations = [(Fixtures.CHANGE_CONDITION): Fixtures.QCO_PARAM_1]

    when:
    def result = underTest.overrideSolutionInMatch(command)

    then:
    1 * configurationHolder.getConfiguration() >> qcoConfigurations
    result.changed() == true
    1 * matchRegister.register(_, _)
  }

  def "AlertResolver should not override match's solution (COUNTER_INCREASED)"() {
    given:
    def match = QCO_RECOMMENDATION_MATCH
    def condition = new ChangeCondition("policyId", "stepId", "solution_old")
    def command = new ResolverCommand(match, condition, NOT_CHANGE)

    when:
    def result = underTest.overrideSolutionInMatch(command)

    then:
    result.changed() == false
    0 * matchRegister.register(_)
  }
}