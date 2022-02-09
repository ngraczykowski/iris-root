package com.silenteight.simulator.dataset.fixture;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.adjudication.api.v1.Dataset;
import com.silenteight.adjudication.api.v1.FilteredAlerts;
import com.silenteight.adjudication.api.v1.FilteredAlerts.AlertTimeRange;
import com.silenteight.adjudication.api.v1.FilteredAlerts.LabelValues;
import com.silenteight.adjudication.api.v1.FilteredAlerts.LabelsFilter;
import com.silenteight.simulator.dataset.archive.ArchiveDatasetRequest;
import com.silenteight.simulator.dataset.create.DatasetLabel;
import com.silenteight.simulator.dataset.create.dto.CreateDatasetRequestDto;
import com.silenteight.simulator.dataset.create.dto.CreateDatasetRequestDto.CreateDatasetRequestDtoBuilder;
import com.silenteight.simulator.dataset.domain.DatasetState;
import com.silenteight.simulator.dataset.dto.AlertSelectionCriteriaDto;
import com.silenteight.simulator.dataset.dto.DatasetDto;
import com.silenteight.simulator.dataset.dto.RangeQueryDto;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.silenteight.protocol.utils.MoreTimestamps.toTimestamp;
import static com.silenteight.simulator.dataset.domain.DatasetState.ACTIVE;
import static java.time.ZoneOffset.UTC;
import static java.util.UUID.fromString;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DatasetFixtures {

  public static final UUID ID_1 = fromString("b4708d8c-4832-6fde-8dc0-d17b4708d8ca");
  public static final UUID ID_2 = fromString("65608792-1086-4fe8-bc80-55a351bd2018");
  public static final UUID ID_3 = fromString("794d362c-0a1d-4794-ae71-ac715bf956f3");
  public static final UUID ID_4 = fromString("f36a59e8-5793-4053-86b4-01dca35b9e63");
  public static final UUID ID_5 = fromString("0258e788-793a-45e6-b577-858283ee6165");
  public static final UUID ID_6 = fromString("b95eb010-7f98-45b1-8c13-78eee94923fc");
  public static final String RESOURCE_NAME_1 = "datasets/" + ID_1;
  public static final String RESOURCE_NAME_2 = "datasets/" + ID_2;
  public static final String DATASET_NAME = "Dataset name";
  public static final String DESCRIPTION = "Dataset description";
  public static final String EXTERNAL_RESOURCE_NAME =
      "datasets/b6855a6f-fc63-422f-84a7-677a0c8f9a9a";
  public static final DatasetState STATE = ACTIVE;
  public static final long ALERTS_COUNT = 5L;
  public static final long SECOND_ALERTS_COUNT = 4L;
  public static final long NO_ALERTS_COUNT = 0;
  public static final OffsetDateTime FROM = OffsetDateTime.of(2019, 1, 1, 0, 0, 0, 0, UTC);
  public static final String FROM_AS_STRING = "2019-01-01T00:00:00Z";
  public static final String DISPLAY_RANGE_FROM = "2019-01-01";
  public static final OffsetDateTime TO = OffsetDateTime.of(2020, 10, 10, 23, 59, 59, 0, UTC);
  public static final String TO_AS_STRING = "2020-10-10T23:59:59Z";
  public static final String DISPLAY_RANGE_TO = "2020-10-10";
  public static final OffsetDateTime CREATED_AT =
      OffsetDateTime.of(2021, 3, 12, 11, 25, 10, 0, UTC);
  public static final String CREATED_BY = "asmith";
  public static final String ARCHIVED_BY = "jdoe";
  public static final String COUNTRY_LABEL = "country";
  public static final List<String> COUNTRIES = List.of("PL", "RU", "DE");
  private static final String MATCH_QUANTITY_LABEL = "matchQuantity";
  private static final List<String> MATCH_QUANTITIES = List.of("single");
  public static final List<DatasetLabel> LABELS = List.of(
      new DatasetLabel(COUNTRY_LABEL, COUNTRIES),
      new DatasetLabel(MATCH_QUANTITY_LABEL, MATCH_QUANTITIES));

  public static final CreateDatasetRequestDto CREATE_DATASET_REQUEST_DTO = createDatasetRequestDto()
      .build();

  public static final CreateDatasetRequestDto
      CREATE_DATASET_REQUEST_WITH_NULL_DESCRIPTION =
      createDatasetRequestDto()
          .description(null)
          .build();

  public static final DatasetDto DATASET_DTO = DatasetDto.builder()
      .id(ID_1)
      .name(RESOURCE_NAME_1)
      .datasetName(DATASET_NAME)
      .description(DESCRIPTION)
      .state(STATE)
      .alertsCount(ALERTS_COUNT)
      .query(selectionCriteria(FROM, TO, COUNTRIES))
      .createdAt(CREATED_AT)
      .createdBy(CREATED_BY)
      .build();

  public static final Dataset DATASET =
      Dataset.newBuilder()
          .setName(EXTERNAL_RESOURCE_NAME)
          .setAlertCount(ALERTS_COUNT)
          .build();

  public static final Dataset EMPTY_DATASET =
      Dataset.newBuilder()
          .setName(EXTERNAL_RESOURCE_NAME)
          .setAlertCount(NO_ALERTS_COUNT)
          .build();

  private static AlertSelectionCriteriaDto selectionCriteria(
      OffsetDateTime from, OffsetDateTime to, List<String> countries) {

    return AlertSelectionCriteriaDto.builder()
        .alertGenerationDate(dateRange(from, to))
        .countries(countries)
        .build();
  }

  private static RangeQueryDto dateRange(OffsetDateTime from, OffsetDateTime to) {
    return RangeQueryDto.builder()
        .from(from)
        .to(to)
        .build();
  }

  private static final AlertTimeRange ALERT_TIME_RANGE =
      AlertTimeRange.newBuilder()
          .setStartTime(toTimestamp(FROM))
          .setEndTime(toTimestamp(TO))
          .build();

  private static final LabelValues COUNTRY_LABEL_VALUES =
      LabelValues.newBuilder()
          .addAllValue(COUNTRIES)
          .build();

  private static final LabelValues MATCH_QUANTITY_LABEL_VALUES =
      LabelValues.newBuilder()
          .addAllValue(MATCH_QUANTITIES)
          .build();

  private static final Map<String, LabelValues> LABELS_MAP =
      Map.of(
          COUNTRY_LABEL, COUNTRY_LABEL_VALUES,
          MATCH_QUANTITY_LABEL, MATCH_QUANTITY_LABEL_VALUES);

  private static final LabelsFilter LABELS_FILTER =
      LabelsFilter.newBuilder()
          .putAllLabels(LABELS_MAP)
          .build();

  public static final FilteredAlerts FILTERED_ALERTS =
      FilteredAlerts.newBuilder()
          .setAlertTimeRange(ALERT_TIME_RANGE)
          .setLabelsFilter(LABELS_FILTER)
          .build();

  public static final ArchiveDatasetRequest ARCHIVE_DATASET_REQUEST =
      ArchiveDatasetRequest.builder()
          .id(ID_1)
          .archivedBy(ARCHIVED_BY)
          .build();

  private static CreateDatasetRequestDtoBuilder createDatasetRequestDto() {
    return CreateDatasetRequestDto.builder()
        .id(ID_1)
        .datasetName(DATASET_NAME)
        .description(DESCRIPTION)
        .query(selectionCriteria(FROM, TO, COUNTRIES));
  }
}
