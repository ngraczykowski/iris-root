package com.silenteight.hsbc.bridge.jenkins

import com.silenteight.hsbc.bridge.model.transfer.ModelInfo
import com.silenteight.hsbc.bridge.model.transfer.ModelStatus
import com.silenteight.hsbc.bridge.model.transfer.ModelStatusUpdatedDto
import com.silenteight.hsbc.bridge.model.transfer.ModelType
import com.silenteight.hsbc.bridge.nexus.NexusApiProperties
import com.silenteight.model.api.v1.ModelPromotedForProduction
import com.silenteight.worldcheck.api.v1.ModelPersisted
import com.silenteight.worldcheck.api.v1.ModelStatusUpdated

import static com.silenteight.worldcheck.api.v1.ModelStatus.SUCCESS
import static com.silenteight.worldcheck.api.v1.ModelType.IS_PEP

class Fixtures {

  static TEST_UUID = "a5250934-a67d-11eb-bcbc-0242ac130002"
  static JENKINS_URI = "https://jenkins.silenteight.com/crumbIssuer/api/json"
  static JENKINS_SENDING_STATUS_URI = "https://jenkins.silenteight.com/model/status"
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

  def testNexusUrl = TEST_NEXUS_URL + TEST_UUID + SNAPSHOT_JSON
  def testModelName = MODEL + TEST_UUID
  def testUrl = MODEL_HTTP + TEST_UUID + EXPORT
  def testCrumb = CRUMB_TOKEN
  def testCrumbRequestField = CRUMB_REQUEST
  def jenkinsApiProperties = new JenkinsApiProperties(
      JENKINS_URI, UPDATE_MODEL_URI, JENKINS_SENDING_STATUS_URI, DEV, DEV)
  def nexusApiProperties = new NexusApiProperties(DEV, DEV)

  def modelInfo = ModelInfo.builder()
      .type("MODEL")
      .changeType("MAJOR")
      .name(testModelName)
      .url(testUrl)
      .build()

  def modelInfoWorldCheck = ModelInfo.builder()
      .type("IS_PEP")
      .changeType("MINOR")
      .name(testModelName)
      .url(testUrl)
      .build()

  def modelPersisted = ModelPersisted.newBuilder()
      .setModelName(testModelName)
      .setModelUri(testUrl)
      .setModelType(IS_PEP)
      .build()

  def modelStatusUpdated = ModelStatusUpdated.newBuilder()
      .setModelName(testModelName)
      .setModelUri(testUrl)
      .setModelType(IS_PEP)
      .setModelStatus(SUCCESS)
      .build()

  def modelStatusUpdatedDto = ModelStatusUpdatedDto.builder()
      .name(testModelName)
      .url(testUrl)
      .type(ModelType.IS_PEP.name())
      .status(ModelStatus.SUCCESS.name())
      .build()

  def modelPromotedForProduction = ModelPromotedForProduction.newBuilder()
      .setName(testModelName)
      .setExportUrl(testUrl)
      .build()

  def crumbResponse =
      new CrumbResponse(
          crumb: testCrumb,
          crumbRequestField: testCrumbRequestField
      )
}
