package com.silenteight.bridge.core.reports

import com.silenteight.bridge.core.reports.domain.model.*
import com.silenteight.bridge.core.reports.domain.model.AlertWithMatchesDto.MatchDto
import com.silenteight.bridge.core.reports.domain.model.Report.AlertData
import com.silenteight.bridge.core.reports.domain.model.Report.MatchData
import com.silenteight.data.api.v2.Alert
import com.silenteight.data.api.v2.Match

import com.google.protobuf.Struct
import com.google.protobuf.Value

import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.Map.Entry
import java.util.stream.Collectors

import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME

class ReportFixtures {

  static def BATCH_ID = 'batchId'
  static def ANALYSIS_NAME = 'analysisName'

  static def MATCH_ONE_ID = '21312217'
  static def MATCH_ONE_NAME = 'alerts/1/matches/1'
  static def MATCH_TWO_ID = '3983487'
  static def MATCH_TWO_NAME = 'alerts/1/matches/2'

  static def MATCH_THREE_ID = '9934988'
  static def MATCH_THREE_NAME = 'alerts/1/matches/3'
  static def MATCH_FOUR_ID = '31231232'
  static def MATCH_FOUR_NAME = 'alerts/1/matches/4'

  static def MATCH_ONE = new MatchDto(MATCH_ONE_ID, MATCH_ONE_NAME)
  static def MATCH_TWO = new MatchDto(MATCH_TWO_ID, MATCH_TWO_NAME)
  static def MATCH_THREE = new MatchDto(MATCH_THREE_ID, MATCH_THREE_NAME)
  static def MATCH_FOUR = new MatchDto(MATCH_FOUR_ID, MATCH_FOUR_NAME)

  static def ALERT_ONE_ID = 'SANC-ASM-1252185'
  static def ALERT_ONE_NAME = 'alerts/1'
  static def ALERT_ONE_STATUS = 'RECOMMENDED'
  static def ALERT_ONE_ERROR_STATUS = 'ERROR'
  static def ALERT_ONE_ERR_DESC = 'some error'
  static def ALERT_ONE_METADATA = "{\"currentVersionId\":\"678175153\", \"stopDescriptorNames\":[\"JOHN BAMBO\",\"BAMBO JOHN\"],\"datasetId\":\"1231123\",\"datasetName\":\"R_US_Address_FAKED\",\"uniqueCustId\":\"R_US_Address_FAKED_G1243141_2011-11-11-11.22.33.333333\",\"masterId\":\"21312321341324\",\"busDate\":\"123412412412331\"}"

  static def ALERT_TWO_ID = 'SANC-ASM-1252186'
  static def ALERT_TWO_NAME = 'alerts/2'
  static def ALERT_TWO_STATUS = 'RECOMMENDED'
  static def ALERT_TWO_ERR_DESC = 'potential error'
  static def ALERT_TWO_METADATA = "{\"currentVersionId\":\"678175153\",\"stopDescriptorNames\":[\"JOHN DOE\",\"DOE JOHN\"],\"datasetId\":\"1231123\",\"datasetName\":\"R_US_Address_FAKED\",\"uniqueCustId\":\"R_US_Address_FAKED_G1243141_2011-11-11-11.22.33.333333\",\"masterId\":\"21312321341324\",\"busDate\":\"123412412412331\"}"

  static def RECOMMENDATION_ONE_NAME = 'recommendation/1'
  static def RECOMMENDATION_ONE_ACTION = 'some recommendation one action'
  static def RECOMMENDATION_ONE_COMMENT = 'some recommendation one comment'
  static def RECOMMENDATION_ONE_POLICY_ID = 'policies/5AFC2F12-85C0-4FB3-992E-1552AC843CEB'
  static def RECOMMENDATION_ONE_POLICY_TITLE = 'some fancy policy title name'
  static def RECOMMENDATION_ONE_STEP_ID = 'steps/E6CEB774-AB56-4576-B653-1CDCEB2D25E7'
  static def RECOMMENDATION_ONE_STEP_TITLE = 'some fancy step title name'
  static def RECOMMENDATION_ONE_FV_SIGNATURE = 'J4VGkp1+FaNsaGDtBXgQsWpUYDo='
  static def RECOMMENDATION_ONE_MATCH_COMMENT = 'some recommendation match metadata one comment'
  static def RECOMMENDATION_ONE_RECOMMENDED_AT = OffsetDateTime
      .of(2022, 3, 28, 11, 33, 00, 00, ZoneOffset.UTC)
  static def RECOMMENDATION_ONE_METADATA_MATCH_METADATA = MatchMetadataDto.builder()
      .match('alerts/1/matches/1')
      .solution('ACTION_POTENTIAL_TRUE_POSITIVE')
      .matchComment(RECOMMENDATION_ONE_MATCH_COMMENT)
      .reason(
          Map.of(
              'feature_vector_signature', RECOMMENDATION_ONE_FV_SIGNATURE,
              'policy', RECOMMENDATION_ONE_POLICY_ID,
              'policy_title', RECOMMENDATION_ONE_POLICY_TITLE,
              'step', RECOMMENDATION_ONE_STEP_ID,
              'step_title', RECOMMENDATION_ONE_STEP_TITLE
          ))
      .features(
          Map.of(
              'features/name',
              createFeatureMetadata('EXACT_MATCH', Map.of('name', 'Some dummy reason')),
              'features/nationalId',
              createFeatureMetadata('NO_MATCH', Map.of('name', 'Some dummy reason'))
          ))
      .categories(
          Map.of('categories/customerType', 'C'))
      .build()

  static def RECOMMENDATION_TWO_NAME = 'recommendation/2'
  static def RECOMMENDATION_TWO_ACTION = 'some recommendation two action'
  static def RECOMMENDATION_TWO_COMMENT = 'some recommendation two comment'
  static def RECOMMENDATION_TWO_MATCH_COMMENT = 'some recommendation match metadata two comment'
  static def RECOMMENDATION_TWO_RECOMMENDED_AT = OffsetDateTime
      .of(2022, 3, 28, 12, 33, 00, 00, ZoneOffset.UTC)
  static def RECOMMENDATION_TWO_METADATA_MATCH_METADATA = MatchMetadataDto.builder()
      .match('alerts/1/matches/2')
      .solution('ACTION_INVESTIGATE')
      .matchComment(RECOMMENDATION_TWO_MATCH_COMMENT)
      .reason(
          Map.of(
              'fvSignature', 'J4VGkp1+FaNsaGDtBXgQsWpUYDo=',
              'policy', 'policies/CA9C8D45-00E6-443A-B7E4-A589C49829FE',
              'policy_title', 'some fancy policy title name',
              'step', 'steps/EE05365F-11A5-44A3-A5C3-BE5EC54204C7',
              'step_title', 'some fancy step title name'
          ))
      .features(
          Map.of(
              'features/name',
              createFeatureMetadata('EXACT_MATCH', Map.of('name', 'Some dummy reason'))
          ))
      .categories(
          Map.of('categories/customerType', 'C'))
      .build()

  static def RECOMMENDATION_ONE_METADATA = new RecommendationMetadataDto(
      [
          RECOMMENDATION_ONE_METADATA_MATCH_METADATA
      ]
  )

  static def RECOMMENDATION_TWO_METADATA = new RecommendationMetadataDto(
      [
          RECOMMENDATION_TWO_METADATA_MATCH_METADATA
      ]
  )

  static def ALERT_ONE = AlertWithMatchesDto.builder()
      .id(ALERT_ONE_ID)
      .name(ALERT_ONE_NAME)
      .status(ALERT_ONE_STATUS)
      .metadata(ALERT_ONE_METADATA)
      .errorDescription(ALERT_ONE_ERR_DESC)
      .matches([MATCH_ONE, MATCH_TWO])
      .build()

  static def ALERT_ONE_WITHOUT_METADATA = AlertWithMatchesDto.builder()
      .id(ALERT_ONE_ID)
      .name(ALERT_ONE_NAME)
      .status(ALERT_ONE_STATUS)
      .metadata('')
      .errorDescription(ALERT_ONE_ERR_DESC)
      .matches([MATCH_ONE, MATCH_TWO])
      .build()

  static def ALERT_ONE_WITH_ERROR_STATUS = AlertWithMatchesDto.builder()
      .id(ALERT_ONE_ID)
      .name(ALERT_ONE_NAME)
      .status(ALERT_ONE_ERROR_STATUS)
      .metadata(ALERT_ONE_METADATA)
      .errorDescription(ALERT_ONE_ERR_DESC)
      .matches([MATCH_ONE, MATCH_TWO])
      .build()

  static def ALERT_TWO = AlertWithMatchesDto.builder()
      .id(ALERT_TWO_ID)
      .name(ALERT_TWO_NAME)
      .status(ALERT_TWO_STATUS)
      .metadata(ALERT_TWO_METADATA)
      .errorDescription(ALERT_TWO_ERR_DESC)
      .matches([MATCH_THREE, MATCH_FOUR])
      .build()

  static def FIRST_RECOMMENDATION_WITH_METADATA = RecommendationWithMetadataDto.builder()
      .name(RECOMMENDATION_ONE_NAME)
      .alertName(ALERT_ONE_NAME)
      .analysisName(ANALYSIS_NAME)
      .recommendedAction(RECOMMENDATION_ONE_ACTION)
      .recommendationComment(RECOMMENDATION_ONE_COMMENT)
      .recommendedAt(RECOMMENDATION_ONE_RECOMMENDED_AT)
      .metadata(RECOMMENDATION_ONE_METADATA)
      .timeout(false)
      .build()

  static def SECOND_RECOMMENDATION_WITH_METADATA = RecommendationWithMetadataDto.builder()
      .name(RECOMMENDATION_TWO_NAME)
      .alertName(ALERT_TWO_NAME)
      .analysisName(ANALYSIS_NAME)
      .recommendedAction(RECOMMENDATION_TWO_ACTION)
      .recommendationComment(RECOMMENDATION_TWO_COMMENT)
      .recommendedAt(RECOMMENDATION_TWO_RECOMMENDED_AT)
      .metadata(RECOMMENDATION_TWO_METADATA)
      .timeout(false)
      .build()

  static def RECOMMENDATIONS_WITH_METADATA = [
      FIRST_RECOMMENDATION_WITH_METADATA, SECOND_RECOMMENDATION_WITH_METADATA
  ]

  static Map<String, String> ALERT_ONE_METADATA_MAP = Map.of(
      'currentVersionId', '678175153',
      'stopDescriptorNames[0]', 'JOHN BAMBO',
      'stopDescriptorNames[1]', 'BAMBO JOHN',
      'datasetId', '1231123',
      'datasetName', 'R_US_Address_FAKED',
      'uniqueCustId', 'R_US_Address_FAKED_G1243141_2011-11-11-11.22.33.333333',
      'masterId', '21312321341324',
      'busDate', '123412412412331',
  )

  static AlertData ALERT_DATA_ONE = AlertData.builder()
      .id(ALERT_ONE.id())
      .name(ALERT_ONE.name())
      .recommendation(RECOMMENDATION_ONE_ACTION)
      .comment(RECOMMENDATION_ONE_COMMENT)
      .recommendedAt(RECOMMENDATION_ONE_RECOMMENDED_AT)
      .errorDescription(ALERT_ONE_ERR_DESC)
      .status(ALERT_ONE_STATUS.toString())
      .policyId('policies/5AFC2F12-85C0-4FB3-992E-1552AC843CEB')
      .policyTitle('some fancy policy title name')
      .metadata(ALERT_ONE_METADATA_MAP)
      .build()

  static Map<String, String> MATCH_DATA_ONE_FEATURES_MAP = Map.of(
      'features/name:solution', 'EXACT_MATCH',
      'features/nationalId:solution', 'NO_MATCH'
  )

  static Map<String, String> MATCH_DATA_ONE_CATEGORIES_MAP = Map.of(
      'categories/customerType', 'C'
  )

  static MatchData MATCH_DATA_ONE = MatchData.builder()
      .id(MATCH_ONE_ID)
      .name('alerts/1/matches/1')
      .recommendation('ACTION_POTENTIAL_TRUE_POSITIVE')
      .comment(RECOMMENDATION_ONE_MATCH_COMMENT)
      .stepId('steps/E6CEB774-AB56-4576-B653-1CDCEB2D25E7')
      .stepTitle('some fancy step title name')
      .fvSignature('J4VGkp1+FaNsaGDtBXgQsWpUYDo=')
      .features(MATCH_DATA_ONE_FEATURES_MAP)
      .categories(MATCH_DATA_ONE_CATEGORIES_MAP)
      .build()

  static AlertData ALERT_DATA_TWO = AlertData.builder()
      .id(ALERT_TWO.id())
      .name(ALERT_TWO.name())
      .recommendation(RECOMMENDATION_TWO_ACTION)
      .comment(RECOMMENDATION_TWO_COMMENT)
      .recommendedAt(RECOMMENDATION_TWO_RECOMMENDED_AT)
      .errorDescription(ALERT_TWO_ERR_DESC)
      .status(ALERT_TWO_STATUS.toString())
      .metadata(Map.of())
      .build()

  static def REPORT_ONE = Report.builder()
      .batchId(BATCH_ID)
      .analysisName(ANALYSIS_NAME)
      .alertData(ALERT_DATA_ONE)
      .matches([MATCH_DATA_ONE])
      .build()

  static def REPORT_TWO = Report.builder()
      .batchId(BATCH_ID)
      .analysisName(ANALYSIS_NAME)
      .alertData(ALERT_DATA_TWO)
      .build()

  static def ERROR_REPORT_ONE = Report.builder()
      .batchId(BATCH_ID)
      .analysisName('')
      .alertData(
          ALERT_DATA_ONE.toBuilder()
              .recommendation(null)
              .comment(null)
              .recommendedAt(null)
              .policyId(null)
              .policyTitle(null)
              .build()
      )
      .matches([MATCH_DATA_ONE])
      .build()

  static def REPORTS = [
      REPORT_ONE, REPORT_TWO
  ]

  static def ERRONEOUS_REPORTS = [ERROR_REPORT_ONE]

  static def WAREHOUSE_ALERT_PAYLOAD_WITHOUT_METADATA = Struct.newBuilder()
      .putFields('batchId', getValue(BATCH_ID))
      .putFields('clientId', getValue(ALERT_ONE_ID))
      .putFields('name', getValue(ALERT_ONE_NAME))
      .putFields('s8Recommendation', getValue(RECOMMENDATION_ONE_ACTION))
      .putFields('recommendationDate', getValue(RECOMMENDATION_ONE_RECOMMENDED_AT))
      .putFields('comment', getValue(RECOMMENDATION_ONE_COMMENT))
      .putFields('policyId', getValue(RECOMMENDATION_ONE_POLICY_ID))
      .putFields('policyTitle', getValue(RECOMMENDATION_ONE_POLICY_TITLE))
      .putFields('status', getValue(ALERT_ONE_STATUS))
      .putFields('errorDescription', getValue(ALERT_ONE_ERR_DESC))
      .build()

  static def WAREHOUSE_ALERT_PAYLOAD_WITH_METADATA = Struct.newBuilder()
      .putFields('batchId', getValue(BATCH_ID))
      .putFields('clientId', getValue(ALERT_ONE_ID))
      .putFields('name', getValue(ALERT_ONE_NAME))
      .putFields('s8Recommendation', getValue(RECOMMENDATION_ONE_ACTION))
      .putFields('recommendationDate', getValue(RECOMMENDATION_ONE_RECOMMENDED_AT))
      .putFields('comment', getValue(RECOMMENDATION_ONE_COMMENT))
      .putFields('policyId', getValue(RECOMMENDATION_ONE_POLICY_ID))
      .putFields('policyTitle', getValue(RECOMMENDATION_ONE_POLICY_TITLE))
      .putFields('status', getValue(ALERT_ONE_STATUS))
      .putFields('errorDescription', getValue(ALERT_ONE_ERR_DESC))
      .putAllFields(toStructMap(ALERT_ONE_METADATA_MAP))
      .build()

  static def WAREHOUSE_ALERT_MATCHES =
      [
          Match.newBuilder()
              .setDiscriminator(MATCH_ONE_ID)
              .setName(MATCH_ONE_NAME)
              .setPayload(
                  Struct.newBuilder()
                      .putFields('clientMatchId', getValue(MATCH_DATA_ONE.id()))
                      .putFields('s8Recommendation', getValue(MATCH_DATA_ONE.recommendation()))
                      .putFields('s8Reason', getValue(MATCH_DATA_ONE.comment()))
                      .putFields('stepId', getValue(MATCH_DATA_ONE.stepId()))
                      .putFields('stepTitle', getValue(MATCH_DATA_ONE.stepTitle()))
                      .putFields('fvSignature', getValue(MATCH_DATA_ONE.fvSignature()))
                      .putAllFields(toStructMap(MATCH_DATA_ONE_FEATURES_MAP))
                      .putAllFields(toStructMap(MATCH_DATA_ONE_CATEGORIES_MAP))
                      .build())
              .build()
      ]

  static def WAREHOUSE_ALERT = Alert.newBuilder()
      .setName(ALERT_DATA_ONE.name())
      .setDiscriminator(ALERT_DATA_ONE.id())
      .setAccessPermissionTag('')
      .setPayload(WAREHOUSE_ALERT_PAYLOAD_WITH_METADATA)
      .addAllMatches(WAREHOUSE_ALERT_MATCHES)
      .build()

  static def WAREHOUSE_ALERT_WITHOUT_METADATA = Alert.newBuilder()
      .setName(ALERT_DATA_ONE.name())
      .setDiscriminator(ALERT_DATA_ONE.id())
      .setAccessPermissionTag('')
      .setPayload(WAREHOUSE_ALERT_PAYLOAD_WITHOUT_METADATA)
      .addAllMatches(WAREHOUSE_ALERT_MATCHES)
      .build()

  private static Value getValue(OffsetDateTime value) {
    Value.newBuilder().setStringValue(ISO_OFFSET_DATE_TIME.format(value)).build()
  }

  private static Value getValue(String value) {
    Value.newBuilder().setStringValue(value).build()
  }

  private static Map<String, Value> toStructMap(Map<String, String> source) {
    source.entrySet().stream()
        .collect(Collectors.toMap(Entry::getKey, e -> getValue(e.getValue().toString())))
  }

  private static FeatureMetadataDto createFeatureMetadata(
      String solution, Map<String, String> reason) {
    return FeatureMetadataDto.builder()
        .agentConfig('agents/name/versions/1.0.0/configs/1')
        .solution(solution)
        .reason(reason)
        .build()
  }
}
