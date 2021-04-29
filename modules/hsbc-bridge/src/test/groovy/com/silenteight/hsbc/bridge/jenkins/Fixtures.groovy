package com.silenteight.hsbc.bridge.jenkins

import com.silenteight.hsbc.bridge.jenkins.CrumbResponse
import com.silenteight.hsbc.bridge.jenkins.JenkinsApiProperties
import com.silenteight.hsbc.bridge.transfer.ModelClient.Model
import com.silenteight.hsbc.bridge.transfer.ModelInfo

class Fixtures {

  static TEST_UUID = "a5250934-a67d-11eb-bcbc-0242ac130002"
  static JENKINS_URI = "https://jenkins.silenteight.com/crumbIssuer/api/json"
  static UPDATE_MODEL_URI = "https://jenkins.silenteight.com/model/update"
  static DEV = "dev"
  static TEST_NEXUS_URL = "https://nexus.hsbc.com/maven2/com/silenteight/hsbc/model/"
  static SNAPSHOT = "-SNAPSHOT"
  static SNAPSHOT_JSON = "-SNAPSHOT.JSON"
  static MODEL = "model/"
  static MODEL_HTTP = "http://10.2.3.4/model/"
  static EXPORT = "/export"
  static CRUMB_TOKEN = "ba4742b9d92606f4236456568a"
  static CRUMB_REQUEST = "Jenkins-Crumb"
  static CRUMB_HTTP_RESPONSE = "{crumbRequestField:" + CRUMB_REQUEST + ",crumb:" + CRUMB_TOKEN + "}"
  static UPDATE_MODEL_HTTP_RESPONSE = "{version:" + TEST_UUID + SNAPSHOT + ":url:" +
      TEST_NEXUS_URL + TEST_UUID + SNAPSHOT_JSON + "}"

  def testVersion = TEST_UUID + SNAPSHOT
  def testNexusUrl = TEST_NEXUS_URL + TEST_UUID + SNAPSHOT_JSON
  def testId = UUID.fromString(TEST_UUID)
  def testModelName = MODEL + TEST_UUID
  def testUrl = MODEL_HTTP + TEST_UUID + EXPORT
  def testCrumb = CRUMB_TOKEN
  def testCrumbRequestField = CRUMB_REQUEST
  def jenkinsApiProperties = new JenkinsApiProperties(JENKINS_URI, UPDATE_MODEL_URI, DEV, DEV)

  def model = new Model() {

    @Override
    String getVersion() {
      return testVersion
    }

    @Override
    String getUrl() {
      return testNexusUrl
    }
  }

  def modelInfo =
      new ModelInfo(
          id: testId,
          modelName: testModelName,
          url: testUrl
      )

  def crumbResponse =
      new CrumbResponse(
          crumb: testCrumb,
          crumbRequestField: testCrumbRequestField
      )
}
