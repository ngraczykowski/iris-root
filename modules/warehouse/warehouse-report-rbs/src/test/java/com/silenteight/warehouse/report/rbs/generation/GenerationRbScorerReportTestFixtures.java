package com.silenteight.warehouse.report.rbs.generation;

import lombok.NoArgsConstructor;

import com.silenteight.warehouse.report.reporting.*;
import com.silenteight.warehouse.report.reporting.ColumnProperties;
import com.silenteight.warehouse.report.reporting.FilterProperties;

import java.util.List;

import static com.silenteight.warehouse.report.rbs.RbsReportTestFixtures.*;
import static java.util.List.of;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class GenerationRbScorerReportTestFixtures {

  public static final String FV_SIGNATURE_FIELD_NAME = "fvsignature";
  public static final String FV_SIGNATURE_FIELD_LABEL = "FV Signature";
  public static final String POLICY_NAME_FIELD = "policy_name";
  public static final String STEP_NAME_FIELD = "step_name";
  public static final String RECOMMENDED_ACTION = "recommended_action";
  public static final String CATEGORIES_AP_TYPE_FIELD = "categories/apType";
  public static final String CATEGORIES_RISK_TYPE_FIELD = "categories/riskType";
  public static final String FEATURES_NAME_FIELD = "features/name";
  public static final String FEATURES_DOB_FIELD = "features/dob";
  public static final String QA_DECISION_FIELD = "qa_decision";
  public static final String ANALYSIS_DECISION_FIELD = "analysis_decision";
  public static final String ALERT_STATUS_FIELD = "alert_status";
  public static final String COMPLETED = "COMPLETED";
  public static final String FP_VALUE = "FP";
  public static final String PTP_VALUE = "PTP";
  public static final String PASS_VALUE = "PASS";
  public static final String FAILED_VALUE = "FAILED";
  public static final String QA_DECISION_FALSE_POSITIVE = "QA_decision_false_positive";
  public static final String QA_DECISION_TRUE_POSITIVE = "QA_decision_true_positive";
  public static final String ANALYST_DECISION_FP = "analyst_decision_FP";
  public static final String ANALYST_DECISION_PTP = "analyst_decision_PTP";

  public static final RbsReportDefinition PROPERTIES = new RbsReportDefinition(
      DATE_FIELD_NAME,
      of(
          getColumn(POLICY_ID_FIELD_NAME, POLICY_ID_FIELD_LABEL),
          getColumn(RECOMMENDATION_FIELD_NAME, RECOMMENDATION_FIELD_LABEL),
          getColumn(POLICY_NAME_FIELD),
          getColumn(STEP_NAME_FIELD),
          getColumn(RECOMMENDED_ACTION),
          getColumn(CATEGORIES_AP_TYPE_FIELD),
          getColumn(CATEGORIES_RISK_TYPE_FIELD),
          getColumn(FEATURES_NAME_FIELD),
          getColumn(FEATURES_DOB_FIELD)),
      getGroupingColumn(),
      getFilters(ALERT_STATUS_FIELD, of(COMPLETED)),
      TEST_INDEX);

  private static ColumnProperties getColumn(String name, String label) {
    return new ColumnProperties(name, label);
  }

  private static ColumnProperties getColumn(String name) {
    return getColumn(name, name);
  }

  private static List<GroupingColumnProperties> getGroupingColumn() {
    GroupingColumnProperties groupingColumnForQa = getGroupingColumn(
        QA_DECISION_FIELD,
        of(
            getGroupingValue(PASS_VALUE, QA_DECISION_FALSE_POSITIVE),
            getGroupingValue(FAILED_VALUE, QA_DECISION_TRUE_POSITIVE)));

    GroupingColumnProperties groupingColumnForAnalyst = getGroupingColumn(
        ANALYSIS_DECISION_FIELD,
        of(
            getGroupingValue(FP_VALUE, ANALYST_DECISION_FP),
            getGroupingValue(PTP_VALUE, ANALYST_DECISION_PTP)));

    return of(groupingColumnForQa, groupingColumnForAnalyst);
  }

  private static GroupingColumnProperties getGroupingColumn(
      String name,
      List<GroupingValues> groupingValues) {

    return new GroupingColumnProperties(name, groupingValues);
  }

  private static GroupingValues getGroupingValue(String value, String label) {
    return new GroupingValues(value, label);
  }

  private static List<FilterProperties> getFilters(String name, List<String> values) {
    return of(new FilterProperties(name, values));
  }
}
