Feature: Sierra scenarios

  Scenario: Get an alert from CMAPI
    Given Send alert on solving
    When Initialize generation of "PAYMENTS_BRIDGE" PB report via warehouse and wait until it's generated
    And Download generated PB report
    Then Downloaded PB report contains 1 rows
