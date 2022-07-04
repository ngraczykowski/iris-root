@sierra
Feature: Sierra scenarios

  Background:
    Then Create user "A" with random name
    And Assign user "A" to roles
      | MODEL_TUNER |
    And Create user "B" with random name
    And Assign user "B" to roles
      | APPROVER |
    And Default user is "A"

  Scenario: Create solving model from scratch without running simulation
    And Create empty policy with name "QA Policy for simulation test"
    And Add steps to recently created policy
      | stepInternalId  | name      | solution                |
      | 1               | some step | POTENTIAL_TRUE_POSITIVE |
    And Add features to recently created steps
      | stepInternalId  | name          | condition | values |
      | 1               | features/name | is        | YES    |
    And Mark created policy as ready
    And Create solving model for created policy
    And Create policy state change request
    Then Activate solving model as user "B"

  Scenario: Get an alert from CMAPI
    Given Subscribed to callbacks at "/mock/cmapi/pb"
    When Send alert on solving
    Then Wait for a message for max 50 seconds

    When The last message contains following fields
      | Body.msg_ReceiveDecision.Messages[0].Message.Comment | S8 recommended action: Manual Investigation\n\nManual Investigation hits:\nMatch Hit-ID-1(50K, #1)\nMatch AS00307517(50K, #2) |
    When Initialize generation of "AI_REASONING" report via warehouse and wait until it's generated
    And Download generated report
    Then Downloaded report contains 1 rows
    And All entries have "S8 Alert Resolution" equal to "ACTION_INVESTIGATE"
