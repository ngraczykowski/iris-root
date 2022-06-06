Feature: Hotel scenarios

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
    Then Activate solving model as other user

  Scenario: AI Reasoning report generated for batch with 5 alerts contains 5 rows
    Given Send a batch with 5 alerts on solving and wait until it's solved
    And Get result for batch and send on ingest
    And Send batch on learning
    When Initialize generation of "AI_REASONING" HSBC report via warehouse and wait until it's generated
    And Download generated HSBC report
    Then Downloaded HSBC report contains 5 rows
