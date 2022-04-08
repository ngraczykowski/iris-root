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

  @Subject
  def underTest = new QueueMatchResolver(matchRegister)

  def "AlertResolver should override match's solution"() {
    given:
    def match = QCO_RECOMMENDATION_MATCH
    def condition = new ChangeCondition("policyId", "stepId", "solution_old")
    def command = new ResolverCommand(match, condition, OVERRIDE)

    when:
    def result = underTest.overrideSolutionMatch(command)

    then:
    result.changed() == true
    1 * matchRegister.register(_)
  }

  def "AlertResolver should not override match's solution (COUNTER_INCREASED)"() {
    given:
    def match = QCO_RECOMMENDATION_MATCH
    def condition = new ChangeCondition("policyId", "stepId", "solution_old")
    def command = new ResolverCommand(match, condition, NOT_CHANGE)

    when:
    def result = underTest.overrideSolutionMatch(command)

    then:
    result.changed() == false
    0 * matchRegister.register(_)
  }
}