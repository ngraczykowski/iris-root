Feature: HSBC scenarios

  Background:
    * url baseUrl
    * def KarateHelper = Java.type('utils.KarateHelper')
    * def karateHelper = new KarateHelper();


  Scenario: QA-T1 AI Reasoning report generated for batch with 5 alerts contains 5 rows
    * def batch = karateHelper.dataGenerationService.generateBatchWithSize(5)
    * call read('/utils/SolveBatchWithLearning.feature')
    When path 'rest/warehouse/api/v2/analysis/production/reports/AI_REASONING'
    And param from = batch.getGenerationStartTime()
    And param to = karateHelper.dataGenerationService.getDateTimeNow()
    And method post
    And status 200
    * karateHelper.waitUtils.waitForReportToGenerate(response.reportName)
    And path 'rest/warehouse/api/v2/'+response.reportName+'/status'
    And method get
    And status 200
    And match response.status == 'OK'
    And path 'rest/warehouse/api/v2/'+response.reportName
    And method get
    Then status 200
    * csv report = response
    And assert report.length == 5
