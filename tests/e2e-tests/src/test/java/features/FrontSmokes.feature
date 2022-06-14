Feature: Front smoke tests

  Scenario: It is possible to login as existing user
    When Login as admin
    Then User is on main page
