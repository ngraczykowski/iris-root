package com.silenteight.hsbc.bridge.jenkins

import com.silenteight.hsbc.bridge.BridgeApiProperties
import com.silenteight.hsbc.bridge.model.dto.ModelInfo
import com.silenteight.hsbc.bridge.model.dto.ModelStatusUpdatedDto
import com.silenteight.hsbc.bridge.model.dto.ModelType
import com.silenteight.hsbc.bridge.nexus.NexusApiProperties
import com.silenteight.model.api.v1.ModelPromotedForProduction
import com.silenteight.proto.worldcheck.api.v1.ModelPersisted
import com.silenteight.proto.worldcheck.api.v1.ModelStatus
import com.silenteight.proto.worldcheck.api.v1.ModelStatusUpdated

class Fixtures {

  static TEST_UUID = "a5250934-a67d-11eb-bcbc-0242ac130002"
  static TEST_VERSION = "20210715101720"
  static SOLVING_MODEL_WITH_UID = "solvingModels/" + TEST_UUID
  static TEST_BUCKET_WITH_UID = "testbucket/" + TEST_UUID
  static JENKINS_URI = "https://jenkins.silenteight.com/crumbIssuer/api/json"
  static JENKINS_SENDING_STATUS_URI = "https://jenkins.silenteight.com/model/status"
  static UPDATE_MODEL_URI = "https://jenkins.silenteight.com/model/update"
  static DEV = "dev"
  static TEST_NEXUS_URL = "https://nexus.hsbc.com/maven2/com/silenteight/hsbc/model/"
  static SNAPSHOT = "-SNAPSHOT"
  static SNAPSHOT_JSON = "-SNAPSHOT.JSON"
  static MODEL_EXPORT = "/model/export/"
  static BRIDGE_ADDRESS = "http://localhost:24802"
  static CRUMB_TOKEN = "ba4742b9d92606f4236456568a"
  static CRUMB_REQUEST = "Jenkins-Crumb"
  static MODEL = "MODEL/"
  static IS_PEP_HISTORICAL = "IS_PEP_HISTORICAL/"
  static IS_PEP_PROCEDURAL = "IS_PEP_PROCEDURAL/"
  static NAME_ALIASES = "NAME_ALIASES/"
  static CRUMB_HTTP_RESPONSE = "{crumbRequestField:" + CRUMB_REQUEST + ",crumb:" + CRUMB_TOKEN + "}"
  static UPDATE_MODEL_HTTP_RESPONSE = "{version:" + TEST_UUID + SNAPSHOT + ":url:" + TEST_NEXUS_URL + TEST_UUID + SNAPSHOT_JSON + "}"

  def testNexusUrl = TEST_NEXUS_URL + TEST_UUID + SNAPSHOT_JSON
  def testGovernanceModelName = SOLVING_MODEL_WITH_UID
  def testModelVersion = TEST_VERSION
  def testWorldCheckModelName = TEST_BUCKET_WITH_UID
  def testGovernanceUrl = BRIDGE_ADDRESS + MODEL_EXPORT + MODEL + TEST_VERSION
  def testWorldCheckNameAliasesVersionUrl = BRIDGE_ADDRESS + MODEL_EXPORT + NAME_ALIASES + TEST_VERSION
  def testWorldCheckIsPepProceduralVersionUrl = BRIDGE_ADDRESS + MODEL_EXPORT + IS_PEP_PROCEDURAL + TEST_VERSION
  def testWorldCheckIsPepHistoricalVersionUrl = BRIDGE_ADDRESS + MODEL_EXPORT + IS_PEP_HISTORICAL + TEST_VERSION
  def testWorldCheckNameUidUrl = BRIDGE_ADDRESS + MODEL_EXPORT + NAME_ALIASES + TEST_UUID
  def testWorldCheckIsPepProceduralUidUrl = BRIDGE_ADDRESS + MODEL_EXPORT + IS_PEP_PROCEDURAL + TEST_UUID
  def testWorldCheckIsPepHistoricalUidUrl = BRIDGE_ADDRESS + MODEL_EXPORT + IS_PEP_HISTORICAL + TEST_UUID
  def testCrumb = CRUMB_TOKEN
  def testCrumbRequestField = CRUMB_REQUEST
  def jenkinsApiProperties = new JenkinsApiProperties(JENKINS_URI, UPDATE_MODEL_URI, JENKINS_SENDING_STATUS_URI, DEV, DEV)
  def bridgeApiProperties = new BridgeApiProperties(BRIDGE_ADDRESS)
  def nexusApiProperties = new NexusApiProperties(DEV, DEV)

  def modelInfo = ModelInfo.builder()
      .type("MODEL")
      .changeType("MAJOR")
      .version(testModelVersion)
      .name(SOLVING_MODEL_WITH_UID)
      .url(testGovernanceUrl)
      .build()

  def modelInfoWorldCheckNameAliases = ModelInfo.builder()
      .type("NAME_ALIASES")
      .changeType("MINOR")
      .version(testModelVersion)
      .name(TEST_BUCKET_WITH_UID)
      .url(testWorldCheckNameAliasesVersionUrl)
      .build()

  def modelInfoWorldCheckIsPepProcedural = ModelInfo.builder()
      .type("IS_PEP_PROCEDURAL")
      .changeType("MINOR")
      .version(testModelVersion)
      .name(TEST_BUCKET_WITH_UID)
      .url(testWorldCheckIsPepProceduralVersionUrl)
      .build()

  def modelInfoWorldCheckIsPepHistorical = ModelInfo.builder()
      .type("IS_PEP_HISTORICAL")
      .changeType("MINOR")
      .version(testModelVersion)
      .name(TEST_BUCKET_WITH_UID)
      .url(testWorldCheckIsPepHistoricalVersionUrl)
      .build()

  def nameModelPersisted = ModelPersisted.newBuilder()
      .setModelName(testWorldCheckModelName)
      .setModelUri(testWorldCheckNameUidUrl)
      .setModelType(com.silenteight.proto.worldcheck.api.v1.ModelType.NAME_ALIASES)
      .setModelVersion(testModelVersion)
      .build()

  def proceduralModelPersisted = ModelPersisted.newBuilder()
      .setModelName(testWorldCheckModelName)
      .setModelUri(testWorldCheckIsPepProceduralUidUrl)
      .setModelType(com.silenteight.proto.worldcheck.api.v1.ModelType.IS_PEP_PROCEDURAL)
      .setModelVersion(testModelVersion)
      .build()

  def historicalModelPersisted = ModelPersisted.newBuilder()
      .setModelName(testWorldCheckModelName)
      .setModelUri(testWorldCheckIsPepHistoricalUidUrl)
      .setModelType(com.silenteight.proto.worldcheck.api.v1.ModelType.IS_PEP_HISTORICAL)
      .setModelVersion(testModelVersion)
      .build()

  def nameModelStatusUpdated = ModelStatusUpdated.newBuilder()
      .setModelName(testModelVersion)
      .setModelUri(testWorldCheckNameAliasesVersionUrl)
      .setModelType(com.silenteight.proto.worldcheck.api.v1.ModelType.NAME_ALIASES)
      .setModelStatus(ModelStatus.SUCCESS)
      .build()

  def proceduralModelStatusUpdated = ModelStatusUpdated.newBuilder()
      .setModelName(testModelVersion)
      .setModelUri(testWorldCheckIsPepProceduralVersionUrl)
      .setModelType(com.silenteight.proto.worldcheck.api.v1.ModelType.IS_PEP_PROCEDURAL)
      .setModelStatus(ModelStatus.SUCCESS)
      .build()

  def historicalModelStatusUpdated = ModelStatusUpdated.newBuilder()
      .setModelName(testModelVersion)
      .setModelUri(testWorldCheckIsPepHistoricalVersionUrl)
      .setModelType(com.silenteight.proto.worldcheck.api.v1.ModelType.IS_PEP_HISTORICAL)
      .setModelStatus(ModelStatus.SUCCESS)
      .build()

  def governanceModelStatusUpdatedDto = ModelStatusUpdatedDto.builder()
      .name(testModelVersion)
      .url(testGovernanceUrl)
      .type(ModelType.MODEL.name())
      .status(ModelStatus.SUCCESS.name())
      .build()

  def nameModelStatusUpdatedDto = ModelStatusUpdatedDto.builder()
      .name(testModelVersion)
      .url(testWorldCheckNameAliasesVersionUrl)
      .type(ModelType.NAME_ALIASES.name())
      .status(ModelStatus.SUCCESS.name())
      .build()

  def proceduralModelStatusUpdatedDto = ModelStatusUpdatedDto.builder()
      .name(testModelVersion)
      .url(testWorldCheckIsPepProceduralVersionUrl)
      .type(ModelType.IS_PEP_PROCEDURAL.name())
      .status(ModelStatus.SUCCESS.name())
      .build()

  def historicalModelStatusUpdatedDto = ModelStatusUpdatedDto.builder()
      .name(testModelVersion)
      .url(testWorldCheckIsPepHistoricalVersionUrl)
      .type(ModelType.IS_PEP_HISTORICAL.name())
      .status(ModelStatus.SUCCESS.name())
      .build()

  def modelPromotedForProduction = ModelPromotedForProduction.newBuilder()
      .setName(testGovernanceModelName)
      .setVersion(testModelVersion)
      .build()

  def crumbResponse =
      new CrumbResponse(
          crumb: testCrumb,
          crumbRequestField: testCrumbRequestField
      )
}
