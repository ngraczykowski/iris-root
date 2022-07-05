@smoke
Feature: Frontend smoke scenarios

  Scenario: It is possible to login as existing user
    When User login as admin
    Then User is on main page
