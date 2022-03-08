package com.silenteight.scb.ingest.adapter.incomming.common.util

import com.silenteight.scb.ingest.adapter.incomming.common.util.MatchingTextTransformer

import spock.lang.Specification
import spock.lang.Unroll

class MatchingTextTransformerSpec extends Specification {

  @Unroll
  def "should '#matchingTexts' with '#name' and '#alternateNames' be transformed to '#expectedName' and '#expectedAlternateNames'"() {
    given:
    def transformer = new MatchingTextTransformer(matchingTexts, name, alternateNames)

    when:
    transformer.transform()

    then:
    transformer.name == expectedName
    transformer.alternateNames == expectedAlternateNames

    where:
    matchingTexts                                     | name              | alternateNames         |
        expectedName      | expectedAlternateNames
    ['Wang,']                                         | 'Wang'            | null                   |
        'Wang'            | null
    ['Wang,']                                         | 'Ying'            | null                   |
        'Ying'            | null
    ['Wang,']                                         | 'Wang'            | ['Ying']               |
        'Wang'            | null
    ['Wang,']                                         | 'Wang'            | ['Ying', 'HK']         |
        'Wang'            | null
    ['Wang,']                                         | 'Wang'            |
        ['Ying', 'HK', 'Wangee']                                                                   |
        'Wang'            | ['Wangee']
    ['Wang,']                                         | 'Ying'            | ['Wang']               |
        null              | ['Wang']
    ['Wang,']                                         | 'Ying'            | ['HK']                 |
        'Ying'            | ['HK']
    ['Wang, Ying,']                                   | 'Wang'            | ['HK']                 |
        'Wang'            | null
    ['Wang,']                                         | 'Ying'            | ['Wang', 'HK']         |
        null              | ['Wang']
    ['Wang, Ying,']                                   | 'Wangee'          |
        ['HK', 'Yin', 'Yingyung']                                                                  |
        'Wangee'          | null
    ['Wang, Ying, HK,']                               | 'Wangee'          |
        ['HK', 'Yin', 'Wang, Yingyung']                                                            |
        'Wangee'          | ['Wang, Yingyung']
    ['Wang, Ying, HK,']                               | 'Wang, Ying, HKK' |
        ['HK', 'Yin', 'Wang, Yingyung']                                                            |
        'Wang, Ying, HKK' | ['Wang, Yingyung']
    ['wang,']                                         | 'Wang'            | ['wang']               |
        'Wang'            | ['wang']
    ['Wang,']                                         | 'wang'            | ['Wang']               |
        'wang'            | ['Wang']
    ['Wang, Ying, HK,', 'Wangee,']                    | 'Wang'            | ['Wangee', 'Yangyung'] |
        'Wang'            | ['Wangee']
    ['ROBERT   MILLER, AE,', 'ROBERT FRITZMANN, AE,'] | 'ROBERT MILLER'   |
        ['ROBERT   MILLER', 'ROBERT FRITZMANN']                                                    |
        null              | ['ROBERT   MILLER', 'ROBERT FRITZMANN']
    ['Liang Chen, ']                                  | 'Chih Liang Chen' | ['DAVID CHEN']         |
        'Chih Liang Chen' | null
    ['Zhang Yu, CN, ']                                | 'Yu Ping CHEUNG'  |
        ['Yu Ping CHEUNG', 'Zhang Yu Ping']                                                        |
        null              | ['Zhang Yu Ping']
    ['PAWAN KUMAR, IN, ']                             | 'PAWAN K DANWAR'  |
        ['PAWAN K DANWAR', 'PAWAN KUMAR DANWAR']                                                   |
        null              | ['PAWAN KUMAR DANWAR']
  }
}
