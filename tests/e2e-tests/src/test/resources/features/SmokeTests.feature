Feature: Smoke tests

  Background:
    * url baseUrl

  Scenario: QA-T2 Get all the simulations
    Given path 'rest/simulator/api/v1/simulations'
    And param state = ['NEW', 'PENDING', 'RUNNING', 'DONE', 'ERROR']
    When method get
    Then status 200
    And assert response.length > 1

  Scenario: QA-T3 Get all the datasets
    Given path 'rest/simulator/api/v1/datasets'
    When method get
    Then status 200
    And assert response.length > 1

  Scenario: QA-T4 Get all the policies
    Given path 'rest/governance/api/v1/policies'
    When method get
    Then status 200
    And assert response.length > 1
