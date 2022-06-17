@hotel
  #TODO(dsniezek): temporary remove after gov,soim, webapp merged to one backend application
@hotel-backend
Feature: Hotel scenarios

  Background:
    Then Create user "A" with random name
    And Assign user "A" to roles
      | MODEL_TUNER |
    And Create user "B" with random name
    And Assign user "B" to roles
      | APPROVER |
    And Default user is "A"

  Scenario: Create solving model from scratch
    Given Send batch with 15 alerts on learning
    And Create empty policy with name "QA Policy for simulation test"
    And Add steps to recently created policy
      | name      | solution                |
      | some step | POTENTIAL_TRUE_POSITIVE |
    And Add features to recently created steps
      | name                 | condition | values |
      | features/commonNames | is        | YES    |
    And Mark created policy as ready
    And Create dataset with name "QA Dataset for simulation test" for recently created learning
    And Create solving model for created policy
    When Create simulation based on created policy and dataset with name "QA Simulation"
    And Wait until simulation is done
    And Create policy state change request
    Then Activate solving model as user "B"

  Scenario: AI Reasoning report generated for batch with 5 alerts contains 5 rows
    Given Send a batch with 5 alerts on solving and wait until it's solved
    And Get result for batch and send on ingest
    And Send batch on learning
    And Create country group
    And Add countries to country group
      | PL |
      | MX |
      | SG |
    And Assign user "A" to country group
    And Prepare report date range based on the last batch
    When Initialize generation of "AI_REASONING" report via warehouse and wait until it's generated
    And Download generated report
    Then Downloaded report contains 5 rows
    And All entries have "S8 Alert Resolution" equal to "ACTION_INVESTIGATE"
