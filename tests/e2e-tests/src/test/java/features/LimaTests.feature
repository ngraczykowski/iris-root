@lima
Feature: Lima scenarios

  Background:
    Then Create user "A" with random name
    And Assign user "A" to roles
      | MODEL_TUNER |
    And Create user "B" with random name
    And Assign user "B" to roles
      | APPROVER |
    And Default user is "A"
    Given Create empty policy with name "QA Policy for scb-bridge GNS-RT request test"
    And Add steps to recently created policy
      | stepInternalId  | name                                                               | solution                |
      | 1               | INDIVIDUAL - dateAgent, documentAgent,  nameAgent                  | POTENTIAL_TRUE_POSITIVE |
      | 2               | COMPANY - documentAgent, nameAgent                                 | POTENTIAL_TRUE_POSITIVE |
      | 3               | COMPANY - nameAgent                                                | POTENTIAL_TRUE_POSITIVE |
      | 4               | INDIVIDUAL - DENY - dateAgent, nameAgent, documentAgent            | FALSE_POSITIVE          |
      | 5               | INDIVIDUAL - DENY - nameAgent, dateAgent, documentAgent            | FALSE_POSITIVE          |
      | 6               | INDIVIDUAL - DENY - nationalIdAgent, documentAgent, dateAgent      | FALSE_POSITIVE          |
      | 7               | INDIVIDUAL - DENY - dateAgent, documentAgent, nameAgent            | FALSE_POSITIVE          |
      | 8               | INDIVIDUAL - NOT-DENY - dateAgent, documentAgent                   | FALSE_POSITIVE          |
      | 9               | INDIVIDUAL - NOT-DENY - nameAgent, dateAgent, documentAgent        | FALSE_POSITIVE          |
      | 10              | INDIVIDUAL - NOT-DENY - nationalIdAgent, documentAgent, dateAgent  | FALSE_POSITIVE          |
      | 11              | INDIVIDUAL - dateAgent, documentAgent, nameAgent                   | FALSE_POSITIVE          |
    And Add features to recently created steps
      | stepInternalId  | name               | condition | values                                        |
      | 1               | categories/apType  | is        | I                                             |
      | 1               | features/date      | is        | EXACT                                         |
      | 1               | features/document  | is        | PERFECT_MATCH, DIGIT_MATCH                    |
      | 1               | features/name      | is        | EXACT_MATCH, STRONG_MATCH, MATCH, WEAK_MATCH  |

      | 2               | categories/apType  | is        | C                                             |
      | 2               | features/document  | is        | PERFECT_MATCH, DIGIT_MATCH                    |
      | 2               | features/name      | is        | STRONG_MATCH, MATCH, WEAK_MATCH               |

      | 3               | categories/apType  | is        | C                                             |
      | 3               | features/name      | is        | EXACT_MATCH, STRONG_MATCH                     |

      | 4               | categories/apType  | is        | I                                             |
      | 4               | categories/isDeny  | is        | YES                                           |
      | 4               | features/date      | is        | OUT_OF_RANGE                                  |
      | 4               | features/name      | is        | NO_MATCH, HQ_NO_MATCH                         |
      | 4               | features/document  | is_not    | PERFECT_MATCH, DIGIT_MATCH                    |

      | 5               | categories/apType  | is        | I                                             |
      | 5               | categories/isDeny  | is        | YES                                           |
      | 5               | features/name      | is        | HQ_NO_MATCH                                   |
      | 5               | features/date      | is        | EXACT, INCONCLUSIVE                           |
      | 5               | features/document  | is        | NO_MATCH, NO_DATA                             |

      | 6               | categories/apType  | is        | I                                             |
      | 6               | categories/isDeny  | is        | YES                                           |
      | 6               | features/nationalId| is        | NO_MATCH                                      |
      | 6               | features/document  | is        | NO_MATCH                                      |
      | 6               | features/date      | is        | NEAR, OUT_OF_RANGE                            |

      | 7               | categories/apType  | is        | I                                             |
      | 7               | categories/isDeny  | is        | YES                                           |
      | 7               | features/date      | is        | OUT_OF_RANGE                                  |
      | 7               | features/document  | is        | NO_MATCH                                      |
      | 7               | features/name      | is_not    | EXACT_MATCH, NO_DATA, INCONCLUSIVE            |

      | 8               | categories/apType  | is        | I                                             |
      | 8               | categories/isDeny  | is        | NO                                            |
      | 8               | features/date      | is        | OUT_OF_RANGE                                  |
      | 8               | features/document  | is_not    | PERFECT_MATCH, DIGIT_MATCH, INCONCLUSIVE      |

      | 9               | categories/apType  | is        | I                                             |
      | 9               | categories/isDeny  | is        | NO                                            |
      | 9               | features/name      | is        | HQ_NO_MATCH                                   |
      | 9               | features/date      | is_not    | EXACT                                         |
      | 9               | features/document  | is_not    | PERFECT_MATCH, DIGIT_MATCH, INCONCLUSIVE      |

      | 10              | categories/apType  | is        | I                                             |
      | 10              | categories/isDeny  | is        | NO                                            |
      | 10              | features/nationalId| is        | NO_MATCH                                      |
      | 10              | features/document  | is_not    | PERFECT_MATCH, DIGIT_MATCH                    |
      | 10              | features/date      | is_not    | EXACT                                         |

      | 11              | categories/apType  | is        | I                                             |
      | 11              | features/date      | is        | OUT_OF_RANGE                                  |
      | 11              | features/document  | is_not    | NO_DATA                                       |
      | 11              | features/name      | is        | EXACT_MATCH, NO_DATA, INCONCLUSIVE            |
    And Mark created policy as ready
    And Create solving model for created policy
    And Create policy state change request
    Then Activate solving model as user "B"

  Scenario: Execute GNR-RT request and verify investigation recommendation
    Given Send request to get recommendation with recommendation type "INVESTIGATE"

  Scenario: Execute GNR-RT request and verify fp recommendation
    Given Send request to get recommendation with recommendation type "FALSE_POSITIVE"

  Scenario: Execute GNR-RT request and verify ptp recommendation
    Given Send request to get recommendation with recommendation type "POTENTIAL_TRUE_POSITIVE"
