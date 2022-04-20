package com.silenteight.qco.domain

import com.silenteight.qco.domain.model.ChangeCondition
import com.silenteight.qco.domain.model.ResolverAction
import com.silenteight.qco.domain.model.ResolverCommand
import com.silenteight.qco.infrastructure.CommentsPrefixProperties

import spock.lang.Specification
import spock.lang.Subject

import static com.silenteight.qco.domain.Fixtures.QCO_RECOMMENDATION_MATCH
import static com.silenteight.qco.domain.model.ResolverAction.NOT_CHANGE
import static com.silenteight.qco.domain.model.ResolverAction.ONLY_MARK
import static com.silenteight.qco.domain.model.ResolverAction.OVERRIDE

class MatchSolutionFactorySpec extends Specification {

  def static prefix = 'QCO prefix'
  def configurationHolder = Mock(QcoConfigurationHolder)

  @Subject
  def underTest = new MatchSolutionFactory(new CommentsPrefixProperties(prefix), configurationHolder)

  def "MatchSolutionFactory should create match with new solution and comment (OVERRIDE)"() {
    given:
    def match = QCO_RECOMMENDATION_MATCH
    def condition = Fixtures.CHANGE_CONDITION
    def command = new ResolverCommand(match, condition, OVERRIDE)
    def qcoConfigurations = [(Fixtures.CHANGE_CONDITION): Fixtures.QCO_PARAM_1]

    when:
    def result = underTest.createMatchSolution(command)

    then:
    1 * configurationHolder.getConfiguration() >> qcoConfigurations
    result.qcoMarked()
    result.comment().startsWith(prefix)
    result.solution() == Fixtures.QCO_SOLUTION
  }

  def "MatchSolutionFactory should not change match's solution and comment (NOT_CHANGE)"() {
    given:
    def match = QCO_RECOMMENDATION_MATCH
    def condition = new ChangeCondition("policyId", "stepId", "solution_old")
    def command = new ResolverCommand(match, condition, NOT_CHANGE)

    when:
    def result = underTest.createMatchSolution(command)

    then:
    0 * configurationHolder.getConfiguration()
    !result.qcoMarked()
    result.solution() == Fixtures.SOLUTION
    !result.comment().startsWith(prefix)
  }

  def "MatchSolutionFactory should not change match's solution and comment (ONLY_MARK)"() {
    given:
    def match = QCO_RECOMMENDATION_MATCH
    def condition = Fixtures.CHANGE_CONDITION
    def command = new ResolverCommand(match, condition, ONLY_MARK)

    when:
    def result = underTest.createMatchSolution(command)

    then:
    0 * configurationHolder.getConfiguration()
    result.qcoMarked()
    result.solution() == Fixtures.SOLUTION
    !result.comment().startsWith(prefix)
  }
}