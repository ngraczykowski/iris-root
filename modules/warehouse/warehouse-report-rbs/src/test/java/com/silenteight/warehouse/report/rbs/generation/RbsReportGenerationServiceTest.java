package com.silenteight.warehouse.report.rbs.generation;

import com.silenteight.warehouse.indexer.query.grouping.FetchGroupedDataResponse;
import com.silenteight.warehouse.indexer.query.grouping.FetchGroupedDataResponse.Row;
import com.silenteight.warehouse.indexer.query.grouping.GroupingQueryService;
import com.silenteight.warehouse.report.rbs.generation.dto.CsvReportContentDto;
import com.silenteight.warehouse.report.reporting.*;
import com.silenteight.warehouse.report.reporting.ColumnProperties;
import com.silenteight.warehouse.report.reporting.FilterProperties;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static com.silenteight.warehouse.report.rbs.RbsReportTestFixtures.DATE_FIELD_NAME;
import static com.silenteight.warehouse.report.rbs.RbsReportTestFixtures.TEST_INDEX;
import static com.silenteight.warehouse.report.rbs.generation.GenerationRbScorerReportTestFixtures.*;
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
    underTest =
        new RbsReportGenerationConfiguration().rbsReportGenerationService(groupingQueryService);
  }

  @Test
  void generateNoDataReport() {
    when(groupingQueryService.generate(any())).thenReturn(
        FetchGroupedDataResponse.builder().rows(emptyList()).build());

    CsvReportContentDto reportContent = underTest.generateReport(
        now(), now(), of(TEST_INDEX), getRbsReportDefinition());

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
        now(), now(), of(TEST_INDEX), getRbsReportDefinition());

    assertThat(reportContent.getReport()).isEqualTo(
        "FV Signature,policy_name,step_name,recommended_action,"
            + "categories/apType,categories/riskType,features/name,features/dob,Hits Count,"
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

    Map<String, String> map = Map.of(
            GenerationRbScorerReportTestFixtures.FV_SIGNATURE_FIELD_NAME, fvs,
            GenerationRbScorerReportTestFixtures.POLICY_NAME_FIELD, policy,
            GenerationRbScorerReportTestFixtures.STEP_NAME_FIELD, step,
            GenerationRbScorerReportTestFixtures.RECOMMENDED_ACTION, action,
            GenerationRbScorerReportTestFixtures.CATEGORIES_AP_TYPE_FIELD, apType,
            GenerationRbScorerReportTestFixtures.CATEGORIES_RISK_TYPE_FIELD, riskType,
            GenerationRbScorerReportTestFixtures.FEATURES_NAME_FIELD, name,
            GenerationRbScorerReportTestFixtures.FEATURES_DOB_FIELD, dob,
            GenerationRbScorerReportTestFixtures.QA_DECISION_FIELD, qa,
            ANALYSIS_DECISION_FIELD, learning);

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
        DATE_FIELD_NAME,
        asList(
            getColumn(
                GenerationRbScorerReportTestFixtures.FV_SIGNATURE_FIELD_NAME,
                GenerationRbScorerReportTestFixtures.FV_SIGNATURE_FIELD_LABEL),
            getColumn(GenerationRbScorerReportTestFixtures.POLICY_NAME_FIELD),
            getColumn(GenerationRbScorerReportTestFixtures.STEP_NAME_FIELD),
            getColumn(GenerationRbScorerReportTestFixtures.RECOMMENDED_ACTION),
            getColumn(GenerationRbScorerReportTestFixtures.CATEGORIES_AP_TYPE_FIELD),
            getColumn(GenerationRbScorerReportTestFixtures.CATEGORIES_RISK_TYPE_FIELD),
            getColumn(GenerationRbScorerReportTestFixtures.FEATURES_NAME_FIELD),
            getColumn(GenerationRbScorerReportTestFixtures.FEATURES_DOB_FIELD)),
        getGroupingColumn(),
        asList(
            getFilter(GenerationRbScorerReportTestFixtures.ALERT_STATUS_FIELD, of(COMPLETED))),
        TEST_INDEX, false, null, null);

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
        QA_DECISION_FIELD,
        of(
            getGroupingValue(PASS_VALUE, QA_DECISION_FALSE_POSITIVE),
            getGroupingValue(FAILED_VALUE, QA_DECISION_TRUE_POSITIVE)));

    GroupingColumnProperties groupingColumnForAnalyst =
        getGroupingColumn(
            ANALYSIS_DECISION_FIELD,
            of(
                getGroupingValue(FP_VALUE, ANALYST_DECISION_FP),
                getGroupingValue(PTP_VALUE, ANALYST_DECISION_PTP)));

    return of(groupingColumnForQa, groupingColumnForAnalyst);
  }
}
