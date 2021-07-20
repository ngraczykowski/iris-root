package com.silenteight.warehouse.report.billing.generation;

import com.silenteight.warehouse.indexer.indexing.IndexesQuery;
import com.silenteight.warehouse.indexer.query.grouping.FetchGroupedDataResponse;
import com.silenteight.warehouse.indexer.query.grouping.FetchGroupedDataResponse.Row;
import com.silenteight.warehouse.indexer.query.grouping.GroupingQueryService;
import com.silenteight.warehouse.report.billing.generation.dto.CsvReportContentDto;

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

  private BillingReportGenerationService underTest;

  @BeforeEach
  void setUp() {
    BillingReportProperties billingReportProperties = new BillingReportProperties(
        "alert_date",
        asList(
            getColumn(BillingScorerFixtures.BILLING_YEAR_FIELD,
                      BillingScorerFixtures.BILLING_YEAR_LABEL),
            getColumn(BillingScorerFixtures.BILLING_MONTH_FIELD,
                      BillingScorerFixtures.BILLING_MONTH_LABEL)),
        new TransposeColumnProperties(
                BillingScorerFixtures.RECOMMENDED_ACTION,
                "count_solved",
                asList("FALSE_POSITIVE", "POTENTIAL_TRUE_POSITIVE", "MANUAL_INVESTIGATION"),
                "count_alerts_received",
                asList("FALSE_POSITIVE", "POTENTIAL_TRUE_POSITIVE"),
                "count_alerts_solved"));

    underTest = new BillingReportGenerationConfiguration().billingReportGenerationService(
        groupingQueryService, indexerQuery, billingReportProperties);
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
    List<Row> responseData = asList(
        buildRow("2020", "1", "FALSE_POSITIVE", 30000),
        buildRow("2020", "1", "POTENTIAL_TRUE_POSITIVE", 100),
        buildRow("2020", "1", "MANUAL_INVESTIGATION", 3000),
        buildRow("2020", "2", "FALSE_POSITIVE", 20000),
        buildRow("2020", "2", "POTENTIAL_TRUE_POSITIVE", 150),
        buildRow("2020", "2", "MANUAL_INVESTIGATION", 4000),
        buildRow("2020", "3", "FALSE_POSITIVE", 15000),
        buildRow("2020", "3", "POTENTIAL_TRUE_POSITIVE", 90),
        buildRow("2020", "3", "MANUAL_INVESTIGATION", 4000));
    FetchGroupedDataResponse response = FetchGroupedDataResponse
        .builder().rows(responseData).build();
    when(groupingQueryService.generate(any())).thenReturn(response);

    CsvReportContentDto reportContent = underTest.generateReport(
        now(), now(), "analysis/production");

    assertThat(reportContent.getReport()).isEqualTo(
        "year,month,count_solved_FALSE_POSITIVE,count_solved_POTENTIAL_TRUE_POSITIVE,"
            + "count_solved_MANUAL_INVESTIGATION,count_alerts_solved,count_alerts_received\n"
            + "2020,1,30000,100,3000,30100,33100\n"
            + "2020,2,20000,150,4000,20150,24150\n"
            + "2020,3,15000,90,4000,15090,19090\n"
            + "\n"
            + "checksum,B8263AB2929D9CA175611C2F000D09072A0E522B466665086A49595B63A367C9\n"
    );
  }

  private Row buildRow(
      String year,
      String month,
      String action,
      long count) {
    Map<String, String> map = Map.of(
        BillingScorerFixtures.BILLING_YEAR_FIELD, year,
        BillingScorerFixtures.BILLING_MONTH_FIELD, month,
        BillingScorerFixtures.RECOMMENDED_ACTION, action);
    return Row.builder().data(map).count(count).build();
  }

  private ColumnProperties getColumn(String name, String label) {
    return new ColumnProperties(name, label);
  }

  private ColumnProperties getColumn(String name) {
    return getColumn(name, name);
  }
}
