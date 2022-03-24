package com.silenteight.scb.outputrecommendation.domain

import com.silenteight.scb.outputrecommendation.domain.model.BatchStatistics
import com.silenteight.scb.outputrecommendation.domain.model.BatchStatistics.RecommendationsStatistics
import com.silenteight.scb.outputrecommendation.domain.model.Recommendations
import com.silenteight.scb.outputrecommendation.domain.model.Recommendations.Alert
import com.silenteight.scb.outputrecommendation.domain.model.Recommendations.AlertStatus
import com.silenteight.scb.outputrecommendation.domain.model.Recommendations.Match
import com.silenteight.scb.outputrecommendation.domain.model.Recommendations.Recommendation

import java.time.OffsetDateTime

class Fixtures {

  static String BATCH_ID = 'batchId'
  static String ANALYSIS_NAME = 'analysisName'
  static List<String> ALERT_IDS = ['alertId1', 'alertId2']

  static String BATCH_SERIALIZED_METADATA =
      '''{
      "datasetId":"batchMetadataDatasetId",
      "datasetName":"batchMetadataDatasetName",
      "batchSendHistoryId":"batchMetadataBatchSendHistoryId",
      "businessDate":"batchMetadataBusinessDate",
      "transmissionDateTime":"batchMetadataTransmissionDateTime",
      "totalNotified":1,
      "totalReceivedFromDb":1
      }'''

  static Integer TRUE_POSITIVE_COUNT = 1
  static Integer FALSE_POSITIVE_COUNT = 2
  static Integer MANUAL_INVESTIGATION_COUNT = 3
  static Integer ERROR_COUNT = 4
  static Integer TOTAL_PROCESSED_COUNT = 6
  static Integer TOTAL_UNABLE_TO_PROCESS_COUNT = 4
  static Integer RECOMMENDED_ALERTS_COUNT = 6

  static RecommendationsStatistics RECOMMENDATIONS_STATISTICS = RecommendationsStatistics.builder()
      .truePositiveCount(TRUE_POSITIVE_COUNT)
      .falsePositiveCount(FALSE_POSITIVE_COUNT)
      .manualInvestigationCount(MANUAL_INVESTIGATION_COUNT)
      .errorCount(ERROR_COUNT)
      .build()

  static String ERROR_DESCRIPTION = 'errorDescription'
  static BatchStatistics BATCH_STATISTICS = BatchStatistics.builder()
      .totalProcessedCount(TOTAL_PROCESSED_COUNT)
      .totalUnableToProcessCount(TOTAL_UNABLE_TO_PROCESS_COUNT)
      .recommendedAlertsCount(RECOMMENDED_ALERTS_COUNT)
      .recommendationsStatistics(RECOMMENDATIONS_STATISTICS)
      .build()

  static PrepareRecommendationResponseCommand PREPARE_RECOMMENDATION_RESPONSE_COMMAND =
      PrepareRecommendationResponseCommand.builder()
          .batchId(BATCH_ID)
          .analysisName(ANALYSIS_NAME)
          .alertNames(ALERT_IDS)
          .batchMetadata(BATCH_SERIALIZED_METADATA)
          .build()

  static PrepareErrorRecommendationResponseCommand PREPARE_ERROR_RECOMMENDATION_RESPONSE_COMMAND =
      PrepareErrorRecommendationResponseCommand.builder()
          .batchId(BATCH_ID)
          .errorDescription(ERROR_DESCRIPTION)
          .batchMetadata(BATCH_SERIALIZED_METADATA)
          .build()

  static String RECOMMENDATION_NAME = 'recommendationName'
  static String RECOMMENDED_ACTION = 'FALSE_POSITIVE'
  static String RECOMMENDED_COMMENT = 'recommendedComment'
  static String POLICY_ID = 'policyId'
  static OffsetDateTime RECOMMENDED_AT = OffsetDateTime.now()

  static String ALERT_ID = 'alertId'
  static AlertStatus ALERT_STATUS = AlertStatus.SUCCESS
  static String ALERT_ERROR_MESSAGE = 'errorMessage'

  static String MATCH_ID = 'matchId'
  static String MATCH_RECOMMENDED_ACTION = 'recommendedAction'
  static String MATCH_RECOMMENDED_COMMENT = 'recommendedComment'
  static String MATCH_STEP_ID = 'stepId'
  static String MATCH_FV_SIGNATURE = 'fvSignature'

  static String ALERT_SERIALIZED_METADATA =
      '''{
      "currentVersionId":"alertMetadataCurrentVersionId",
      "stopDescriptorNames":[
      "alertMetadataStopDescriptorName1",
      "alertMetadataStopDescriptorName2"
      ],
      "datasetId":"alertMetadataDatasetId",
      "datasetName":"alertMetadataDatasetName",
      "uniqueCustId":"alertMetadataUniqueCustId",
      "masterId":"alertMetadataMasterId",
      "busDate":"alertMetadataBusDate"
      }'''

  static String NAME_FEATURE_VALUE = 'nameFeatureValue'
  static String GENDER_FEATURE_VALUE = 'genderFeatureValue'
  static String DATE_OF_BIRTH_FEATURE_VALUE = 'dateOfBirthFeatureValue'
  static String OTHER_COUNTRY_FEATURE_VALUE = 'otherCountryFeatureValue'
  static String RESIDENCY_COUNTRY_FEATURE_VALUE = 'residencyCountryFeatureValue'
  static String NATIONAL_ID_FEATURE_VALUE = 'nationalIdFeatureValue'

  static Map<String, String> FEATURES =
      [
          'features/name'              : NAME_FEATURE_VALUE,
          'features/gender'            : GENDER_FEATURE_VALUE,
          'features/dateOfBirth'       : DATE_OF_BIRTH_FEATURE_VALUE,
          'features/otherCountry'      : OTHER_COUNTRY_FEATURE_VALUE,
          'features/residencyCountry'  : RESIDENCY_COUNTRY_FEATURE_VALUE,
          'features/nationalIdDocument': NATIONAL_ID_FEATURE_VALUE,
      ] as Map

  static List<Match> MATCHES =
      [
          Match.builder()
              .id(MATCH_ID)
              .recommendedAction(MATCH_RECOMMENDED_ACTION)
              .recommendedComment(MATCH_RECOMMENDED_COMMENT)
              .stepId(MATCH_STEP_ID)
              .fvSignature(MATCH_FV_SIGNATURE)
              .features(FEATURES)
              .build()
      ]

  static Alert ALERT = Alert.builder()
      .id(ALERT_ID)
      .status(ALERT_STATUS)
      .metadata(ALERT_SERIALIZED_METADATA)
      .errorMessage(ALERT_ERROR_MESSAGE)
      .build()

  static List<Recommendation> RECOMMENDATION_LIST =
      [
          Recommendation.builder()
              .alert(ALERT)
              .matches(MATCHES)
              .batchId(BATCH_ID)
              .name(RECOMMENDATION_NAME)
              .recommendedAction(RECOMMENDED_ACTION)
              .recommendedComment(RECOMMENDED_COMMENT)
              .policyId(POLICY_ID)
              .recommendedAt(RECOMMENDED_AT)
              .build()
      ]

  static def RECOMMENDATIONS = Recommendations.builder()
      .recommendations(RECOMMENDATION_LIST)
      .statistics(BATCH_STATISTICS)
      .build()
}
