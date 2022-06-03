Feature: Smoke scenarios

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

  Scenario: Get all users
    Given Users endpoint responses with status code 200

  Scenario: Create user
    Given Create user with random name
