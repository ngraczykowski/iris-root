@smoke
Feature: Smoke scenarios

  Background:
    Given Default user is admin

  Scenario: Get all the simulations
    Given Simulation endpoint responses with status code 200

  Scenario: Get all the datasets
    Given Datasets endpoint responses with status code 200

  Scenario: Get all the policies
    Given Policies endpoint responses with status code 200

  Scenario: Create basic policy and activate
    Given Create empty policy with name "Test QA Policy"
    And Add steps to recently created policy
      | name      | solution                |
      | some step | POTENTIAL_TRUE_POSITIVE |
    And Add features to recently created steps
      | name                 | condition | values |
      | features/commonNames | is        | YES    |
    And Mark created policy as ready

  Scenario: Create country group and assign it
    Given Create country group
    And Add countries to country group
      | PL |
    And Create user "A" with random name
    Then Assign user "A" to country group
    And Delete user "A"

  Scenario: Get all users
    Given Users endpoint responses with status code 200

  Scenario: Create and Delete user A
    Given Create user "A" with random name
    Then Delete user "A"

  @hotel-backend
  Scenario: Frontend Configuration via API is accessible without login
    Given Frontend Configuration API respond with 200 status code
