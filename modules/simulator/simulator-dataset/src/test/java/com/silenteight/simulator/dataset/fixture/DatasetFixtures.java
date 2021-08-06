package com.silenteight.simulator.dataset.fixture;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.adjudication.api.v1.Dataset;
import com.silenteight.adjudication.api.v1.FilteredAlerts;
import com.silenteight.adjudication.api.v1.FilteredAlerts.AlertTimeRange;
import com.silenteight.adjudication.api.v1.FilteredAlerts.LabelValues;
import com.silenteight.adjudication.api.v1.FilteredAlerts.LabelsFilter;
import com.silenteight.simulator.dataset.archive.ArchiveDatasetRequest;
import com.silenteight.simulator.dataset.create.CreateDatasetRequest;
import com.silenteight.simulator.dataset.create.dto.CreateDatasetRequestDto;
import com.silenteight.simulator.dataset.domain.DatasetState;
import com.silenteight.simulator.dataset.dto.AlertSelectionCriteriaDto;
import com.silenteight.simulator.dataset.dto.DatasetDto;
import com.silenteight.simulator.dataset.dto.RangeQueryDto;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static com.silenteight.protocol.utils.MoreTimestamps.toTimestamp;
import static com.silenteight.simulator.dataset.domain.DatasetState.ACTIVE;
import static java.time.ZoneOffset.UTC;
import static java.util.Map.of;
import static java.util.UUID.fromString;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DatasetFixtures {

  public static final UUID ID = fromString("b4708d8c-4832-6fde-8dc0-d17b4708d8ca");
  public static final UUID SECOND_ID = fromString("d17b4708-6fde-8dc0-4832-d17b4708d8ca");
  public static final String RESOURCE_NAME = "datasets/" + ID;
  public static final String SECOND_RESOURCE_NAME = "datasets/" + SECOND_ID;
  public static final String DATASET_NAME = "Dataset name";
  public static final String DESCRIPTION = "Dataset description";
  public static final String EXTERNAL_RESOURCE_NAME =
      "datasets/b6855a6f-fc63-422f-84a7-677a0c8f9a9a";
  public static final DatasetState STATE = ACTIVE;
  public static final long ALERTS_COUNT = 5L;
  public static final long SECOND_ALERTS_COUNT = 4L;
  public static final long NO_ALERTS_COUNT = 0;
  public static final OffsetDateTime FROM = OffsetDateTime.of(2019, 1, 1, 0, 0, 0, 0, UTC);
  public static final OffsetDateTime TO = OffsetDateTime.of(2020, 10, 10, 23, 59, 59, 0, UTC);
  public static final OffsetDateTime CREATED_AT =
      OffsetDateTime.of(2021, 3, 12, 11, 25, 10, 0, UTC);
  public static final String CREATED_BY = "asmith";
  public static final String COUNTRY_LABEL = "country";
  public static final List<String> COUNTRIES = List.of("PL", "RU", "DE");
  public static final String ARCHIVED_BY = "jdoe";

  public static final CreateDatasetRequestDto CREATE_DATASET_REQUEST_DTO =
      new CreateDatasetRequestDto(
          ID, DATASET_NAME, DESCRIPTION, selectionCriteria(FROM, TO, COUNTRIES));

  public static final DatasetDto DATASET_DTO = DatasetDto.builder()
      .id(ID)
      .name(RESOURCE_NAME)
      .datasetName(DATASET_NAME)
      .description(DESCRIPTION)
      .state(STATE)
      .alertsCount(ALERTS_COUNT)
      .query(selectionCriteria(FROM, TO, COUNTRIES))
      .createdAt(CREATED_AT)
      .createdBy(CREATED_BY)
      .build();

  public static final CreateDatasetRequest CREATE_DATASET_REQUEST =
      CreateDatasetRequest.builder()
          .id(ID)
          .datasetName(DATASET_NAME)
          .description(DESCRIPTION)
          .rangeFrom(FROM)
          .rangeTo(TO)
          .countries(COUNTRIES)
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

  private static final LabelValues LABEL_VALUES =
      LabelValues.newBuilder()
          .addAllValue(COUNTRIES)
          .build();

  private static final LabelsFilter LABELS_FILTER =
      LabelsFilter.newBuilder()
          .putAllLabels(of(COUNTRY_LABEL, LABEL_VALUES))
          .build();

  public static final FilteredAlerts FILTERED_ALERTS =
      FilteredAlerts.newBuilder()
          .setAlertTimeRange(ALERT_TIME_RANGE)
          .setLabelsFilter(LABELS_FILTER)
          .build();

  public static final ArchiveDatasetRequest ARCHIVE_DATASET_REQUEST =
      ArchiveDatasetRequest.builder()
          .id(ID)
          .archivedBy(ARCHIVED_BY)
          .build();
}
