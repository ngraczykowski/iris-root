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

import static com.silenteight.warehouse.indexer.alert.AlertMapperConstants.ALERT_PREFIX;
import static com.silenteight.warehouse.report.billing.generation.BillingScorerFixtures.ACTION_PREFIX;
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
    TransposeColumnProperties transposeColumnProperties = new TransposeColumnProperties(
        "alert_s8_recommendation",
        asList(
            getColumn("ACTION_FALSE_POSITIVE", "count_solved_FP"),
            getColumn("ACTION_POTENTIAL_TRUE_POSITIVE", "count_solved_PTP"),
            getColumn("ACTION_INVESTIGATE", "count_solved_MI")),
        "count_alerts_received",
        asList("ACTION_FALSE_POSITIVE", "ACTION_POTENTIAL_TRUE_POSITIVE"),
        "count_alerts_solved");

    BillingReportProperties billingReportProperties = new BillingReportProperties("date",
        "index_timestamp", "alert_recommendationYear", "alert_recommendationMonth",
        transposeColumnProperties, emptyList()
    );

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
        buildRow("2020", "1", ACTION_PREFIX + "FALSE_POSITIVE", 30000),
        buildRow("2020", "1", ACTION_PREFIX + "POTENTIAL_TRUE_POSITIVE", 100),
        buildRow("2020", "1", ACTION_PREFIX + "INVESTIGATE", 3000),
        buildRow("2020", "2", ACTION_PREFIX + "FALSE_POSITIVE", 20000),
        buildRow("2020", "2", ACTION_PREFIX + "POTENTIAL_TRUE_POSITIVE", 150),
        buildRow("2020", "2", ACTION_PREFIX + "INVESTIGATE", 4000),
        buildRow("2020", "10", ACTION_PREFIX + "FALSE_POSITIVE", 15000),
        buildRow("2020", "10", ACTION_PREFIX + "POTENTIAL_TRUE_POSITIVE", 90),
        buildRow("2020", "10", ACTION_PREFIX + "INVESTIGATE", 4000)
    );

    FetchGroupedDataResponse response = FetchGroupedDataResponse
        .builder().rows(responseData).build();
    when(groupingQueryService.generate(any())).thenReturn(response);

    CsvReportContentDto reportContent = underTest.generateReport(
        now(), now(), "analysis/production");

    assertThat(reportContent.getReport()).isEqualTo(
        "date,count_solved_FP,count_solved_PTP,"
            + "count_solved_MI,count_alerts_solved,count_alerts_received\n"
            + "2020-01,30000,100,3000,30100,33100\n"
            + "2020-02,20000,150,4000,20150,24150\n"
            + "2020-10,15000,90,4000,15090,19090\n"
            + "\n"
            + "checksum,FB78D401B5A04D0A4CFFCD4485A0FCF229494C4E7768D54CCFEA4214DCAD6842\n"
    );
  }

  private Row buildRow(
      String year,
      String month,
      String action,
      long count) {
    Map<String, String> map = Map.of(
        ALERT_PREFIX + BillingScorerFixtures.BILLING_YEAR_FIELD, year,
        ALERT_PREFIX + BillingScorerFixtures.BILLING_MONTH_FIELD, month,
        ALERT_PREFIX + BillingScorerFixtures.RECOMMENDED_ACTION_PREFIX_FIELD, action);
    return Row.builder().data(map).count(count).build();
  }

  private ColumnProperties getColumn(String name, String label) {
    return new ColumnProperties(name, label);
  }
}
