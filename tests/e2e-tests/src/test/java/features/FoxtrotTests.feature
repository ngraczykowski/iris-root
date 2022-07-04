@foxtrot
Feature: Foxtrot scenarios

  Background:
    Then Create user "A" with random name
    And Assign user "A" to roles
      | MODEL_TUNER |
    And Create user "B" with random name
    And Assign user "B" to roles
      | APPROVER |
    And Default user is "A"

  Scenario: Send alert to FTCC
    Given Send 5 alerts on "learning" to FTCC.
    And Create empty policy with name "QA Policy for simulation test"
    And Add steps to recently created policy
      | stepInternalId  | name      | solution                |
      | 1               | some step | POTENTIAL_TRUE_POSITIVE |
    And Add features to recently created steps
      | stepInternalId  |  name             | condition | values |
      | 1               | features/gender   | is        | YES    |
    And Mark created policy as ready
    And Create dataset with name "QA Dataset for simulation test" for recently created learning
    And Create solving model for created policy
    When Create simulation based on created policy and dataset with name "QA Simulation"
    And Wait until simulation is done
    And Create policy state change request
    Then Activate solving model as user "B"

  Scenario:
    Given Send 5 alerts on "solving" to FTCC.
    And Give it time - 5 seconds
    And Create country group
    And Add countries to country group
      | PL |
      | MX |
      | SG |
    And Assign user "A" to country group
    And Prepare report date range for today
    When Initialize generation of "AI_REASONING" report via warehouse and wait until it's generated
    And Download generated report
    Then Downloaded report contains 5 rows
