package com.silenteight.adjudication.engine.dataset.dataset;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.Dataset;
import com.silenteight.adjudication.api.v1.FilteredAlerts;
import com.silenteight.adjudication.api.v1.FilteredAlerts.AlertTimeRange;
import com.silenteight.adjudication.api.v1.NamedAlerts;
import com.silenteight.adjudication.engine.common.protobuf.TimestampConverter;
import com.silenteight.adjudication.engine.common.resource.ResourceName;
import com.silenteight.sep.base.common.exception.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
class CreateDatasetUseCase {

  private static final String NAME_PREFIX = "datasets/";

  @NonNull
  private final DatasetRepository datasetRepository;
  @NonNull
  private final DatasetAlertRepository datasetAlertRepository;

  @Transactional
  Dataset createDataset(NamedAlerts namedAlerts) {
    var dataset = datasetRepository.save(DatasetEntity.builder().build());

    namedAlerts
        .getAlertsList()
        .stream()
        .map(alertName -> mapNamedAlert(alertName, dataset))
        .forEach(datasetAlertRepository::save);

    return Dataset
        .newBuilder()
        .setName(NAME_PREFIX + dataset.getId())
        .setCreateTime(TimestampConverter.fromOffsetDateTime(dataset.getCreatedAt()))
        .setAlertCount(namedAlerts.getAlertsCount())
        .build();
  }

  @Transactional
  Dataset createDataset(FilteredAlerts filteredAlerts) {
    var dataset = datasetRepository.save(DatasetEntity.builder().build());

    createFilteredDatasetAlerts(dataset.getId(), filteredAlerts.getAlertTimeRange());
    var alertCount = datasetAlertRepository.countByIdDatasetId(dataset.getId());

    return Dataset
        .newBuilder()
        .setName(NAME_PREFIX + dataset.getId())
        .setCreateTime(TimestampConverter.fromOffsetDateTime(dataset.getCreatedAt()))
        .setAlertCount(alertCount)
        .build();
  }

  private void createFilteredDatasetAlerts(long datasetId, AlertTimeRange alertTimeRange) {
    datasetAlertRepository.createFilteredDataset(
        datasetId,
        TimestampConverter.toOffsetDateTime(alertTimeRange.getStartTime()),
        TimestampConverter.toOffsetDateTime(alertTimeRange.getEndTime()));
  }

  Dataset getDataset(String name) {
    var datasetId = ResourceName.create(name).getLong("datasets");
    var dataset = datasetRepository.findById(datasetId);
    if (dataset.isEmpty()) {
      throw new EntityNotFoundException(datasetId);
    }

    var datasetEntity = dataset.get();
    var alertCount = datasetAlertRepository.countByIdDatasetId(datasetId);
    return fromEntity(datasetEntity, alertCount);
  }

  Page<Dataset> listDataset(Pageable pageable) {
    return datasetRepository
        .findAll(pageable)
        .map(datasetEntity -> {
          var alertCount =
              datasetAlertRepository.countByIdDatasetId(datasetEntity.getId());
          return fromEntity(datasetEntity, alertCount);
        });
  }

  Page<String> listDatasetAlerts(Pageable pageable, long datasetId) {
    return datasetAlertRepository
        .findAllByIdDatasetId(pageable, datasetId)
        .map(datasetEntity -> NAME_PREFIX + datasetId + "/alerts/"
            + datasetEntity.getId().getAlertId());

  }

  private Dataset fromEntity(DatasetEntity entity, long alertCount) {
    return Dataset.newBuilder()
        .setName(NAME_PREFIX + entity.getId())
        .setCreateTime(TimestampConverter.fromOffsetDateTime(entity.getCreatedAt()))
        .setAlertCount(alertCount)
        .build();
  }

  private DatasetAlertEntity mapNamedAlert(String alertName, DatasetEntity dataset) {
    return DatasetAlertEntity
        .builder()
        .id(DatasetAlertKey.builder()
            .alertId(ResourceName.create(alertName).getLong("alerts"))
            .datasetId(dataset.getId())
            .build())
        .build();
  }
}
