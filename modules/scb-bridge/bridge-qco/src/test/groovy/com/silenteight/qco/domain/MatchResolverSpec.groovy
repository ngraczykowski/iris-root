package com.silenteight.qco.domain

import com.silenteight.qco.domain.model.ChangeCondition
import com.silenteight.qco.domain.model.MatchSolution
import com.silenteight.qco.domain.model.ResolverCommand

import org.apache.commons.lang3.StringUtils
import spock.lang.Specification
import spock.lang.Subject

import static com.silenteight.qco.domain.Fixtures.*
import static com.silenteight.qco.domain.model.MatchSolution.QCO_MARKED
import static com.silenteight.qco.domain.model.MatchSolution.QCO_UNMARKED
import static com.silenteight.qco.domain.model.ResolverAction.*

class MatchResolverSpec extends Specification {

  def static prefix = 'QCO prefix'
  def matchRegister = Mock(ProcessedMatchesRegister)
  def matchSolutionFactory = Mock(MatchSolutionFactory)

  @Subject
  def underTest = new MatchOverridingResolver(matchRegister, matchSolutionFactory)

  def "AlertResolver should override matches and should sent to either db and warehouse"() {
    given:
    def match = QCO_RECOMMENDATION_MATCH
    def condition = CHANGE_CONDITION
    def command = new ResolverCommand(match, condition, OVERRIDE)
    def qcoComment = String.join(StringUtils.SPACE, prefix, COMMENT)
    def matchSolution = new MatchSolution(QCO_SOLUTION, qcoComment, QCO_MARKED)
    when:
    def result = underTest.overrideSolutionInMatch(command)

    then:
    1 * matchSolutionFactory.createMatchSolution(_) >> matchSolution
    1 * matchRegister.register(_, _)
    0 * matchRegister.registerToDbOnly(_, _)
    result == matchSolution
  }

  def "AlertResolver should not override matches and should not send to neither db and warehouse (NOT_CHANGE)"() {
    given:
    def match = QCO_RECOMMENDATION_MATCH
    def condition = new ChangeCondition("policyId", "stepId", "solution_old")
    def command = new ResolverCommand(match, condition, NOT_CHANGE)
    def matchSolution = new MatchSolution(command.match().solution(), command.match().comment(), QCO_UNMARKED)

    when:
    def result = underTest.overrideSolutionInMatch(command)

    then:
    1 * matchSolutionFactory.createMatchSolution(_) >> matchSolution
    0 * matchRegister.register(_, _)
    0 * matchRegister.registerToDbOnly(_, _)
    result == matchSolution
  }

  def "AlertResolver should store into db only (ONLY_MARK)"() {
    given:
    def match = QCO_RECOMMENDATION_MATCH
    def condition = CHANGE_CONDITION
    def command = new ResolverCommand(match, condition, ONLY_MARK)
    def matchSolution = new MatchSolution(command.match().solution(), command.match().comment(), QCO_MARKED)

    when:
    def result = underTest.overrideSolutionInMatch(command)

    then:
    1 * matchSolutionFactory.createMatchSolution(_) >> matchSolution
    0 * matchRegister.register(_, _)
    1 * matchRegister.registerToDbOnly(_, _)
    result == matchSolution
  }
}