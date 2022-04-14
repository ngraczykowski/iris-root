package com.silenteight.qco.domain

import com.silenteight.qco.domain.model.ResolverCommand

import spock.lang.Specification
import spock.lang.Subject

import static com.silenteight.qco.domain.Fixtures.CHANGE_CONDITION
import static com.silenteight.qco.domain.Fixtures.QCO_RECOMMENDATION_MATCH
import static com.silenteight.qco.domain.model.ChangeCondition.NO_CONDITION_FULFILLED
import static com.silenteight.qco.domain.model.ResolverAction.NOT_CHANGE
import static com.silenteight.qco.domain.model.ResolverAction.OVERRIDE

class MatchProcessorSpec extends Specification {

  def changeConditionFactory = Mock(SimpleMatchChangeConditionFactory)
  def counter = Mock(QcoCounter)
  def resolver = Mock(MatchOverridingResolver)

  @Subject
  def underTest = new MatchProcessor(changeConditionFactory, counter, resolver)

  def "Process match correctly with COUNTER_INCREASED result"() {
    given:
    def recommendationMatch = QCO_RECOMMENDATION_MATCH

    when:
    underTest.processMatch(recommendationMatch)

    then:
    1 * changeConditionFactory.createChangeCondition(_, _, _) >> CHANGE_CONDITION
    1 * counter.increaseAndCheckOverflow(_) >> false
    1 * resolver.overrideSolutionInMatch(_) >> {arguments ->
      {
        def command = arguments[0]
        assert command instanceof ResolverCommand
        assert command.resolverAction() == NOT_CHANGE
      }
    }
  }

  def "Process match correctly with COUNTER_OVERFLOW result"() {
    given:
    def recommendationMatch = QCO_RECOMMENDATION_MATCH

    when:
    underTest.processMatch(recommendationMatch)

    then:
    1 * changeConditionFactory.createChangeCondition(_, _, _) >> CHANGE_CONDITION
    1 * counter.increaseAndCheckOverflow(_) >> true
    1 * resolver.overrideSolutionInMatch(_) >> {arguments ->
      {
        def command = arguments[0]
        assert command instanceof ResolverCommand
        assert command.resolverAction() == OVERRIDE
      }
    }
  }

  def "Process match correctly with NO_CONDITION_FULFILLED result"() {
    given:
    def recommendationMatch = QCO_RECOMMENDATION_MATCH

    when:
    underTest.processMatch(recommendationMatch)

    then:
    1 * changeConditionFactory.createChangeCondition(_, _, _) >> NO_CONDITION_FULFILLED
    0 * counter.increaseAndCheckOverflow(_)
    1 * resolver.overrideSolutionInMatch(_) >> {arguments ->
      {
        def command = arguments[0]
        assert command instanceof ResolverCommand
        assert command.resolverAction() == NOT_CHANGE
      }
    }
  }
}