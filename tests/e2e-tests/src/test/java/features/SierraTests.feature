#@sierra - disabled intentionally
Feature: Sierra scenarios

  Background:
    Then Create user "A" with random name
    And Assign user "A" to roles
      | MODEL_TUNER |
    And Create user "B" with random name
    And Assign user "B" to roles
      | APPROVER |
    And Default user is "A"

  Scenario: Get an alert from CMAPI
    Given Send alert on solving
    And Prepare report date range based on the last batch
    When Initialize generation of "PAYMENTS_BRIDGE" report via warehouse and wait until it's generated
    And Download generated report
    Then Downloaded report contains 1 rows
