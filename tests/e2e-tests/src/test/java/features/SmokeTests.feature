Feature: Smoke tests

  Scenario: QA-T2 Get all the simulations
    Given Simulation endpoint responses with status code 200

  Scenario: QA-T3 Get all the datasets
    Given Datasets endpoint responses with status code 200

  Scenario: QA-T4 Get all the policies
    Given Policies endpoint responses with status code 200
