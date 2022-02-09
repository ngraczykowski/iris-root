package com.silenteight.payments.bridge.common.agents

import spock.lang.Specification
import spock.lang.Unroll

class AlertedPartyIdContextAdapterTest extends Specification {

  @Unroll
  def 'generates `#expectedResult` based on `#matchingField` and `#matchText` using generateAlertedPartyId'() {
    given:
    def request = ContextualAlertedPartyIdModel.builder()
        .matchingField(matchingField)
        .matchText(matchText)
        .numberOfTokensLeft(numberOfTokensLeft)
        .numberOfTokensRight(numberOfTokensRight)
        .minTokens(minTokens)
        .lineBreaks(lineBreaks)
        .build()

    when:
    def result = AlertedPartyIdContextAdapter.generateAlertedPartyId(request)

    then:
    result == expectedResult

    where:
    matchingField                                    | matchText  | numberOfTokensLeft | numberOfTokensRight | minTokens | lineBreaks || expectedResult
    'GARCIA LUIS 314\nLA NEGRA ANTOFAGASTA\nCHILE'   | "LA NEGRA" | 1                  | 1                   | 2         | false      || '314 LA NEGRA ANTOFAGASTA'
    'GARCIA LUIS 314\nLA NEGRA ANTOFAGASTA\nCHILE'   | "LA NEGRA" | 2                  | 2                   | 2         | false      || 'LUIS 314 LA NEGRA ANTOFAGASTA CHILE'
    'GARCIA LUIS 314\nLA NEGRA ANTOFAGASTA\nCHILE'   | "LA NEGRA" | 3                  | 2                   | 2         | false      || 'GARCIA LUIS 314 LA NEGRA ANTOFAGASTA CHILE'
    'GARCIA LUIS 314\nLA NEGRA ANTOFAGASTA\nCHILE'   | "LA NEGRA" | 1                  | 1                   | 2         | true       || "\n LA NEGRA ANTOFAGASTA"
    'GARCIA LUIS 314\nLA NEGRA ANTOFAGASTA\nCHILE'   | "LA NEGRA" | 2                  | 2                   | 2         | true       || "314 \n LA NEGRA ANTOFAGASTA \n"
    'GARCIA LUIS 314\nLA NEGRA ANTOFAGASTA\nCHILE'   | "LA NEGRA" | 2                  | 3                   | 2         | true       || "314 \n LA NEGRA ANTOFAGASTA \n CHILE"
    'GARCIA LUIS 314\nLA NEGRA ANTOFAGASTA\nCHILE'   | "LA NEGRA" | 50                 | 50                  | 2         | true       || "GARCIA LUIS 314 \n LA NEGRA ANTOFAGASTA \n CHILE"
    'GARCIA LUIS 314\nLA NEGRA ANTOFAGASTA\nCHILE'   | "LA NEGRA" | 0                  | 0                   | 0         | true       || "LA NEGRA"
    ''                                               | "LA NEGRA" | 0                  | 0                   | 0         | true       || ""
    ''                                               | ""         | 1                  | 1                   | 1         | true       || ""
    'random string'                                  | "LA NEGRA" | 1                  | 1                   | 2         | true       || ""
    'LA NEGRA'                                       | "LA NEGRA" | 1                  | 1                   | 0         | true       || "LA NEGRA"
    '314 LA NEGRA'                                   | "LA NEGRA" | 1                  | 1                   | 0         | true       || "314 LA NEGRA"
    'LA NEGRA ANTOFAGASTA'                           | "LA NEGRA" | 1                  | 1                   | 0         | true       || "LA NEGRA ANTOFAGASTA"
    'GARCIA LUIS 314\r\nLA NEGRA ANTOFAGASTA\nCHILE' | "LA NEGRA" | 1                  | 2                   | 2         | true       || "\r\n LA NEGRA ANTOFAGASTA \n"
    'GARCIA LUIS 314\r\nLA NEGRA ANTOFAGASTA\nCHILE' | "LA NEGRA" | 1                  | 1                   | 2         | false      || "314 LA NEGRA ANTOFAGASTA"
    'GARCIA LUIS 314\rLA NEGRA ANTOFAGASTA\nCHILE'   | "LA NEGRA" | 1                  | 2                   | 2         | true       || "\r LA NEGRA ANTOFAGASTA \n"
    'GARCIA LUIS 314\rLA NEGRA ANTOFAGASTA\nCHILE'   | "LA NEGRA" | 1                  | 1                   | 2         | false      || "314 LA NEGRA ANTOFAGASTA"
  }
}

