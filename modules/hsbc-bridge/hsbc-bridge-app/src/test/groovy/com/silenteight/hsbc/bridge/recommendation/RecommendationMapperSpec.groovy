package com.silenteight.hsbc.bridge.recommendation

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class RecommendationMapperSpec extends Specification {

  @Shared
  def falsePositive = 'ACTION_FALSE_POSITIVE'
  @Shared
  def manualInvestigation = 'ACTION_INVESTIGATE'
  @Shared
  def ptp = 'ACTION_POTENTIAL_TRUE_POSITIVE'
  @Shared
  def hintedFP = 'ACTION_INVESTIGATE_HINTED_FALSE_POSITIVE'

  var s8ValuesMap = [
      (S8Recommendation.FALSE_POSITIVE)         : falsePositive,
      (S8Recommendation.MANUAL_INVESTIGATION)   : manualInvestigation,
      (S8Recommendation.POTENTIAL_TRUE_POSITIVE): ptp
  ] as Map
  var userValuesMap = [
      (HsbcRecommendation.AAA_FALSE_POSITIVE): 'AAA False Positive',
      (HsbcRecommendation.LEVEL_1_REVIEW)    : 'Level 1',
      (HsbcRecommendation.LEVEL_2_REVIEW)    : 'Level 2'
  ] as Map
  def underTest = new RecommendationMapper(s8ValuesMap, userValuesMap)

  @Unroll
  def 'should return `#expectedResult` when recommendedAction=`#action` and `#extendedAttribute`'() {
    when:
    def result = underTest.getRecommendationValue(action, extendedAttribute)

    then:
    result == expectedResult

    where:
    action              | extendedAttribute | expectedResult
    null                | null              | 'Level 1'
    null                | 'Dummy'           | 'Level 1'
    null                | 'SAN'             | 'Level 2'
    null                | 'SSC'             | 'Level 2'
    ''                  | null              | 'LEVEL_3_REVIEW'
    'Dummy'             | null              | 'LEVEL_3_REVIEW'
    'Dummy'             | 'Dummy'           | 'LEVEL_3_REVIEW'
    falsePositive       | 'Dummy'           | 'AAA False Positive'
    manualInvestigation | 'Dummy'           | 'Level 1'
    hintedFP            | 'Dummy'           | 'Level 1'
    ptp                 | 'Dummy'           | 'Level 2'
    falsePositive       | 'SAN'             | 'AAA False Positive'
    manualInvestigation | 'SAN'             | 'Level 2'
    hintedFP            | 'SAN'             | 'Level 2'
    ptp                 | 'SAN'             | 'LEVEL_3_REVIEW'
    ptp                 | 'SAN'             | 'LEVEL_3_REVIEW'
    falsePositive       | 'SSC'             | 'AAA False Positive'
    manualInvestigation | 'SSC'             | 'Level 2'
    hintedFP            | 'SSC'             | 'Level 2'
    ptp                 | 'SSC'             | 'LEVEL_3_REVIEW'
    ptp                 | 'SSC'             | 'LEVEL_3_REVIEW'

  }
}
