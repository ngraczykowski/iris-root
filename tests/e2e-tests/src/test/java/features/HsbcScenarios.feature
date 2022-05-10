Feature: HSBC scenarios

  Scenario: QA-T1 AI Reasoning report generated for batch with 5 alerts contains 5 rows
    Given Send a batch with 5 alerts on solving and wait until it's solved
    And Get result for batch and send on ingest
    And Send batch on learning
    When Initialize generation of "AI_REASONING" report via warehouse and wait until it's generated
    And Download generated report
    Then Downloaded report contains 5 rows

