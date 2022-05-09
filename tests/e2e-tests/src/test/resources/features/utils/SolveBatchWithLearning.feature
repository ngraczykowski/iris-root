@ignore
Feature: Utils

  Scenario: Solve batch and send learning
    * url baseUrl
    Given path 'rest/hsbc-bridge/async/batch/v1/'+batch.getId()+'/recommend'
    And request batch.getPayload()
    And method post
    And status 200
    * karateHelper.waitUtils.waitForSolvingToComplete(batch.getId())
    And path 'rest/hsbc-bridge/async/batch/v1/'+batch.getId()+'/status'
    And method get
    And status 200
    And match response.batchStatus == 'COMPLETED'
    And path 'rest/hsbc-bridge/async/batch/v1/'+batch.getId()+'/result'
    And method get
    And status 200
    And path 'rest/hsbc-bridge/async/batch/v1/ingestRecommendations'
    And request response
    And method post
    And status 200
    And path 'rest/hsbc-bridge/async/batch/v1/'+batch.getId()+'-learning/learning'
    And request batch.getPayload()
    And method post
    And status 200
