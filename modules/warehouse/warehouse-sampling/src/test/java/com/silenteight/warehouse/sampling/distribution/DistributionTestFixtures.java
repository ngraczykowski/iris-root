package com.silenteight.warehouse.sampling.distribution;

import com.silenteight.model.api.v1.AlertsDistributionServiceProto.AlertsDistributionRequest;
import com.silenteight.warehouse.indexer.query.grouping.FetchGroupedDataResponse;
import com.silenteight.warehouse.indexer.query.grouping.FetchGroupedDataResponse.Row;

import java.util.List;
import java.util.Map;

import static com.silenteight.warehouse.common.opendistro.roles.RolesMappedConstants.COUNTRY_KEY;
import static com.silenteight.warehouse.sampling.alert.SamplingTestFixtures.*;
import static java.util.List.of;

public class DistributionTestFixtures {

  static final List<String> GROUPING_FIELDS = of(ALERT_RISK_TYPE_KEY);
  static final List<String> GROUPING_FIELDS_2 = of(ALERT_RISK_TYPE_KEY,COUNTRY_KEY);

  static final AlertsDistributionRequest ALERTS_DISTRIBUTION_REQUEST =
      AlertsDistributionRequest.newBuilder()
          .setTimeRangeFrom(FIRST_DAY_OF_APRIL)
          .setTimeRangeTo(FIRST_DAY_OF_JUNE)
          .addAllGroupingFields(GROUPING_FIELDS)
          .build();

  static final AlertsDistributionRequest ALERTS_DISTRIBUTION_REQUEST_2 =
      AlertsDistributionRequest.newBuilder()
          .setTimeRangeFrom(FIRST_DAY_OF_APRIL)
          .setTimeRangeTo(FIRST_DAY_OF_JUNE)
          .addAllGroupingFields(GROUPING_FIELDS_2)
          .build();

  static final Map<String, String> RISK_TYPE_PEP_MAP =
      Map.of(ALERT_RISK_TYPE_KEY, ALERT_RISK_TYPE_PEP);

  static final Map<String, String> RISK_TYPE_SAN_MAP =
      Map.of(ALERT_RISK_TYPE_KEY, ALERT_RISK_TYPE_SAN);

  static final Map<String, String> COUNTRY_AND_RISK_TYPE_MAP =
      Map.of(COUNTRY_KEY, ALERT_COUNTRY_UK, ALERT_RISK_TYPE_KEY, ALERT_RISK_TYPE_SAN);

  static final long ALERT_RISK_TYPE_PEP_COUNT = 3;
  static final long ALERT_RISK_TYPE_SAN_COUNT = 7;
  static final long COUNTRY_AND_RISK_TYPE_COUNT = 8;

  static final Row ROW = Row.builder()
      .data(RISK_TYPE_PEP_MAP)
      .count(ALERT_RISK_TYPE_PEP_COUNT)
      .build();

  static final Row ROW_2 = Row.builder()
      .data(RISK_TYPE_SAN_MAP)
      .count(ALERT_RISK_TYPE_SAN_COUNT)
      .build();

  static final Row ROW_3 = Row.builder()
      .data(COUNTRY_AND_RISK_TYPE_MAP)
      .count(COUNTRY_AND_RISK_TYPE_COUNT)
      .build();

  static final List<Row> ROWS_LIST = of(ROW, ROW_2);

  static final List<Row> ROWS_LIST_2 = of(ROW_3);

  static final FetchGroupedDataResponse FETCH_GROUPED_DATA_RESPONSE =
      FetchGroupedDataResponse.builder()
          .rows(ROWS_LIST)
          .build();

  static final FetchGroupedDataResponse FETCH_GROUPED_DATA_RESPONSE_2 =
      FetchGroupedDataResponse.builder()
          .rows(ROWS_LIST_2)
          .build();
}
