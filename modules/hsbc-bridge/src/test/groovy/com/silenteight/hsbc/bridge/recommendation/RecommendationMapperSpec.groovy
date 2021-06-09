package com.silenteight.hsbc.bridge.recommendation

import spock.lang.Specification
import spock.lang.Unroll

class RecommendationMapperSpec extends Specification {

  var s8ValuesMap = [
      (S8Recommendation.FALSE_POSITIVE)         : 'FP',
      (S8Recommendation.MANUAL_INVESTIGATION)   : 'MI',
      (S8Recommendation.POTENTIAL_TRUE_POSITIVE): 'PTP'
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
    action  | extendedAttribute | expectedResult
    null    | null              | 'LEVEL_3_REVIEW'
    ''      | null              | 'LEVEL_3_REVIEW'
    'Dummy' | null              | 'LEVEL_3_REVIEW'
    'Dummy' | 'Dummy'           | 'LEVEL_3_REVIEW'
    'FP'    | 'Dummy'           | 'AAA False Positive'
    'MI'    | 'Dummy'           | 'Level 1'
    'PTP'   | 'Dummy'           | 'Level 2'
    'FP'    | 'SAN'             | 'AAA False Positive'
    'MI'    | 'SAN'             | 'Level 2'
    'PTP'   | 'SAN'             | 'LEVEL_3_REVIEW'
    'PTP'   | 'SAN'             | 'LEVEL_3_REVIEW'
    'FP'    | 'SSC'             | 'AAA False Positive'
    'MI'    | 'SSC'             | 'Level 2'
    'PTP'   | 'SSC'             | 'LEVEL_3_REVIEW'
    'PTP'   | 'SSC'             | 'LEVEL_3_REVIEW'

  }
}
