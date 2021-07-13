package com.silenteight.warehouse.report.rbs.generation;

import com.silenteight.warehouse.indexer.indexing.IndexesQuery;
import com.silenteight.warehouse.indexer.query.grouping.FetchGroupedDataResponse;
import com.silenteight.warehouse.indexer.query.grouping.FetchGroupedDataResponse.Row;
import com.silenteight.warehouse.indexer.query.grouping.GroupingQueryService;
import com.silenteight.warehouse.report.rbs.generation.dto.CsvReportContentDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static java.time.OffsetDateTime.now;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportGenerationServiceTest {

  @Mock
  private GroupingQueryService groupingQueryService;
  @Mock
  private IndexesQuery indexerQuery;

  private RbsReportGenerationService underTest;

  @BeforeEach
  void setUp() {
    RbsReportProperties rbScorerProperties = new RbsReportProperties(
        "alert_date",
        asList(
            getColumn(RbScorerFixtures.FV_SIGNATURE_FIELD_NAME,
                      RbScorerFixtures.FV_SIGNATURE_FIELD_LABEL),
            getColumn(RbScorerFixtures.POLICY_NAME_FIELD),
            getColumn(RbScorerFixtures.STEP_NAME_FIELD),
            getColumn(RbScorerFixtures.RECOMMENDED_ACTION),
            getColumn(RbScorerFixtures.CATEGORIES_AP_TYPE_FIELD),
            getColumn(RbScorerFixtures.CATEGORIES_RISK_TYPE_FIELD),
            getColumn(RbScorerFixtures.FEATURES_NAME_FIELD),
            getColumn(RbScorerFixtures.FEATURES_DOB_FIELD)),
        asList(
            getGroupingColumn(
                RbScorerFixtures.QA_DECISION_FIELD, "QA", asList("PASS", "FAILED"), true),
            getGroupingColumn(
                RbScorerFixtures.ANALYST_DECISION_FIELD, "Learning", asList("FP", "PTP"), false)));
    underTest = new RbsReportGenerationConfiguration().rbsReportGenerationService(
        groupingQueryService, indexerQuery, rbScorerProperties);
  }

  @Test
  void generateNoDataReport() {
    when(groupingQueryService.generate(any())).thenReturn(
        FetchGroupedDataResponse.builder().rows(emptyList()).build());

    CsvReportContentDto reportContent = underTest.generateReport(
        now(), now(), "analysis/production");
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
        "MANUAL_INVESTIGATION",
        "I", "SAN", "MATCH", "NO_MATCH", "", "PTP", 2);
    Row fifth = buildRow(
        "o7uPxWV913+ljhPW2uH+g7eAFeQ=",
        "policies/1234",
        "steps/94526",
        "MANUAL_INVESTIGATION",
        "I", "SAN", "MATCH", "NO_MATCH", "", "FP", 8);

    FetchGroupedDataResponse response = FetchGroupedDataResponse
        .builder().rows(asList(first, second, third, fourth, fifth)).build();
    when(groupingQueryService.generate(any())).thenReturn(response);

    CsvReportContentDto reportContent = underTest.generateReport(
        now(), now(), "analysis/production");

    assertThat(reportContent.getReport()).isEqualTo(
        "FV Signature,policy_name,step_name,recommended_action,"
            + "categories/apType,categories/riskType,features/name,features/dob,"
            + "QA_count,QA_PASS,QA_FAILED,Learning_FP,Learning_PTP\n"
            + "o7uPxWV913+ljhPW2uH+g7eAFeQ=,policies/1234,steps/3456,"
            + "FALSE_POSITIVE,I,SAN,MATCH,NO_MATCH,2,0,1,0,0\n"
            + "o7uPxWV913+ljhPW2uH+g7eAFeQ=,policies/1234,steps/94526,"
            + "MANUAL_INVESTIGATION,I,SAN,MATCH,NO_MATCH,0,0,0,8,2\n"
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
            RbScorerFixtures.ANALYST_DECISION_FIELD, learning);
    return Row.builder().data(map).count(count).build();
  }

  private GroupingColumnProperties getGroupingColumn(
      String name, String label, List<String> groupingValues, boolean addCounter) {
    return new GroupingColumnProperties(name, label, groupingValues, addCounter);
  }

  private ColumnProperties getColumn(String name, String label) {
    return new ColumnProperties(name, label);
  }

  private ColumnProperties getColumn(String name) {
    return getColumn(name, name);
  }
}
