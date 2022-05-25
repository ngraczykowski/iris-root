Feature: Smoke tests

  Scenario: Get all the simulations
    Given Simulation endpoint responses with status code 200

  Scenario: Get all the datasets
    Given Datasets endpoint responses with status code 200

  Scenario: Get all the policies
    Given Policies endpoint responses with status code 200

  Scenario: Create basic policy and activate
    Given Create empty policy with name "Test QA Policy"
    And Prepare feature list for step
      | name                 | condition | values |
      | features/commonNames | is        | YES    |
    And Prepare policy steps with predefined feature list
      | name      | solution                |
      | some step | POTENTIAL_TRUE_POSITIVE |
    When Add prepared steps to policy
    And Add prepared features to steps
    Then Policy is created
    And Mark created policy as ready

  Scenario: Create dataset and run simulation
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
