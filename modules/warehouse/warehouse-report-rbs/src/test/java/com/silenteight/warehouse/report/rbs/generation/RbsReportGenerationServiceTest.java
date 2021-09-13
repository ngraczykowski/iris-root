package com.silenteight.warehouse.report.rbs.generation;

import com.silenteight.warehouse.indexer.query.grouping.FetchGroupedDataResponse;
import com.silenteight.warehouse.indexer.query.grouping.FetchGroupedDataResponse.Row;
import com.silenteight.warehouse.indexer.query.grouping.GroupingQueryService;
import com.silenteight.warehouse.report.rbs.generation.dto.CsvReportContentDto;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static com.silenteight.warehouse.report.rbs.generation.RbScorerFixtures.ANALYSIS_DECISION_FIELD;
import static java.time.OffsetDateTime.now;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RbsReportGenerationServiceTest {

  @Mock
  private GroupingQueryService groupingQueryService;

  private RbsReportGenerationService underTest;

  @BeforeEach
  void setUp() {
    underTest = new RbsReportGenerationConfiguration().rbsReportGenerationService(
        groupingQueryService);
  }

  @Test
  void generateNoDataReport() {
    when(groupingQueryService.generate(any())).thenReturn(
        FetchGroupedDataResponse.builder().rows(emptyList()).build());

    CsvReportContentDto reportContent = underTest.generateReport(
        now(), now(), of("index123"), getRbsReportDefinition());
    assertThat(reportContent.getLines()).isEmpty();
  }

  @Test
  void generateTwoRowsReport() {
    Row first = buildRow(
        "o7uPxWV913+ljhPW2uH+g7eAFeQ=",
        "policies/1234",
        "steps/3456",
        "FALSE_POSITIVE",
        "I", "SAN", "MATCH", "NO_MATCH", "FAILED", "", 1);
    Row second = buildRow(
        "o7uPxWV913+ljhPW2uH+g7eAFeQ=",
        "policies/1234",
        "steps/3456",
        "FALSE_POSITIVE",
        "I", "SAN", "MATCH", "NO_MATCH", "QUEUE", "", 1);
    Row third = buildRow(
        "o7uPxWV913+ljhPW2uH+g7eAFeQ=",
        "policies/1234",
        "steps/3456",
        "FALSE_POSITIVE",
        "I", "SAN", "MATCH", "NO_MATCH", "", "", 31);
    Row fourth = buildRow(
        "o7uPxWV913+ljhPW2uH+g7eAFeQ=",
        "policies/1234",
        "steps/94526",
        "INVESTIGATE",
        "I", "SAN", "MATCH", "NO_MATCH", "", "PTP", 2);
    Row fifth = buildRow(
        "o7uPxWV913+ljhPW2uH+g7eAFeQ=",
        "policies/1234",
        "steps/94526",
        "INVESTIGATE",
        "I", "SAN", "MATCH", "NO_MATCH", "", "FP", 8);

    FetchGroupedDataResponse response = FetchGroupedDataResponse
        .builder().rows(asList(first, second, third, fourth, fifth)).build();
    when(groupingQueryService.generate(any())).thenReturn(response);

    CsvReportContentDto reportContent = underTest.generateReport(
        now(), now(), of("index123"), getRbsReportDefinition());

    assertThat(reportContent.getReport()).isEqualTo(
        "FV Signature,policy_name,step_name,recommended_action,"
            + "categories/apType,categories/riskType,features/name,features/dob,matches_count,"
            + "QA_decision_false_positive,QA_decision_true_positive,"
            + "analyst_decision_FP,analyst_decision_PTP\n"
            + "o7uPxWV913+ljhPW2uH+g7eAFeQ=,policies/1234,steps/3456,"
            + "FALSE_POSITIVE,I,SAN,MATCH,NO_MATCH,33,0,1,0,0\n"
            + "o7uPxWV913+ljhPW2uH+g7eAFeQ=,policies/1234,steps/94526,"
            + "INVESTIGATE,I,SAN,MATCH,NO_MATCH,10,0,0,8,2\n"
    );
  }

  private Row buildRow(
      String fvs,
      String policy,
      String step,
      String action,
      String apType,
      String riskType,
      String name,
      String dob,
      String qa,
      String learning,
      long count) {
    Map<String, String> map = Map
        .of(RbScorerFixtures.FV_SIGNATURE_FIELD_NAME, fvs,
            RbScorerFixtures.POLICY_NAME_FIELD, policy,
            RbScorerFixtures.STEP_NAME_FIELD, step,
            RbScorerFixtures.RECOMMENDED_ACTION, action,
            RbScorerFixtures.CATEGORIES_AP_TYPE_FIELD, apType,
            RbScorerFixtures.CATEGORIES_RISK_TYPE_FIELD, riskType,
            RbScorerFixtures.FEATURES_NAME_FIELD, name,
            RbScorerFixtures.FEATURES_DOB_FIELD, dob,
            RbScorerFixtures.QA_DECISION_FIELD, qa,
            RbScorerFixtures.ANALYSIS_DECISION_FIELD, learning);
    return Row.builder().data(map).count(count).build();
  }

  private GroupingColumnProperties getGroupingColumn(
      String name,
      List<GroupingValues> groupingValues) {

    return new GroupingColumnProperties(name, groupingValues);
  }

  private ColumnProperties getColumn(String name, String label) {
    return new ColumnProperties(name, label);
  }

  @NotNull
  private RbsReportDefinition getRbsReportDefinition() {
    return new RbsReportDefinition(
        "alert_date",
        asList(
            getColumn(
                RbScorerFixtures.FV_SIGNATURE_FIELD_NAME,
                RbScorerFixtures.FV_SIGNATURE_FIELD_LABEL),
            getColumn(RbScorerFixtures.POLICY_NAME_FIELD),
            getColumn(RbScorerFixtures.STEP_NAME_FIELD),
            getColumn(RbScorerFixtures.RECOMMENDED_ACTION),
            getColumn(RbScorerFixtures.CATEGORIES_AP_TYPE_FIELD),
            getColumn(RbScorerFixtures.CATEGORIES_RISK_TYPE_FIELD),
            getColumn(RbScorerFixtures.FEATURES_NAME_FIELD),
            getColumn(RbScorerFixtures.FEATURES_DOB_FIELD)),
        getGroupingColumn(),
        asList(
            getFilter(RbScorerFixtures.ALERT_STATUS_FIELD, of("COMPLETED"))));

  }

  private ColumnProperties getColumn(String name) {
    return getColumn(name, name);
  }

  private FilterProperties getFilter(String name, List<String> values) {
    return new FilterProperties(name, values);
  }

  public static GroupingValues getGroupingValue(String value, String label) {
    return new GroupingValues(value, label);
  }

  private List<GroupingColumnProperties> getGroupingColumn() {
    GroupingColumnProperties groupingColumnForQa = getGroupingColumn(
        "qa_decision",
        of(
            getGroupingValue("PASS", "QA_decision_false_positive"),
            getGroupingValue("FAILED", "QA_decision_true_positive")));

    GroupingColumnProperties groupingColumnForAnalyst =
        getGroupingColumn(
            ANALYSIS_DECISION_FIELD,
            of(
                getGroupingValue("FP", "analyst_decision_FP"),
                getGroupingValue("PTP", "analyst_decision_PTP")));

    return of(groupingColumnForQa, groupingColumnForAnalyst);

  }
}
