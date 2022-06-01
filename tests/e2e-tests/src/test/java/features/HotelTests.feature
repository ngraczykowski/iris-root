Feature: Hotel scenarios

  Scenario: Create solving model from scratch
    Given Send batch with 15 alerts on learning
    And Create empty policy with name "QA Policy for simulation test"
    And Prepare feature list for step
      | name                 | condition | values |
      | features/commonNames | is        | YES    |
    And Prepare policy steps with predefined feature list
      | name      | solution                |
      | some step | POTENTIAL_TRUE_POSITIVE |
    And Add prepared steps to policy
    And Add prepared features to steps
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
