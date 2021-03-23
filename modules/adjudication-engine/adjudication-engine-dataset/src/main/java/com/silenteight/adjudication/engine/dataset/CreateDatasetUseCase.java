package com.silenteight.adjudication.engine.dataset;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.Dataset;
import com.silenteight.adjudication.api.v1.FilteredAlerts;
import com.silenteight.adjudication.api.v1.NamedAlerts;
import com.silenteight.adjudication.engine.common.protobuf.TimestampConverter;
import com.silenteight.adjudication.engine.common.resource.ResourceName;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
class CreateDatasetUseCase {

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
        .forEach(datasetAlertEntity -> datasetAlertRepository.save(datasetAlertEntity));

    return Dataset
        .newBuilder()
        .setName("datasets/" + dataset.getId())
        .setCreateTime(TimestampConverter.fromOffsetDateTime(dataset.getCreatedAt()))
        .setAlertCount(namedAlerts.getAlertsCount())
        .build();
  }

  Dataset createDataset(FilteredAlerts filteredAlerts) {
    throw new UnsupportedOperationException();
  }

  Dataset getDataset(String name) {
    var dataset = datasetRepository.findById(ResourceName.getResource(name).getId("datasets"));
    if (dataset.isPresent()) {
      var datasetEntity = dataset.get();
      var alertCount = datasetAlertRepository.countByIdDatasetId(datasetEntity.getId());
      return fromEntity(datasetEntity, alertCount);
    }
    throw new RuntimeException(String.format("Entity not found by name:%s", name));
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
        .map(datasetEntity -> "datasets/" + datasetId + "/alerts/"
            + datasetEntity.getId().getAlertId());

  }

  private Dataset fromEntity(DatasetEntity entity, long alertCount) {
    return Dataset.newBuilder()
        .setName("datasets/" + entity.getId())
        .setCreateTime(TimestampConverter.fromOffsetDateTime(entity.getCreatedAt()))
        .setAlertCount(alertCount)
        .build();
  }

  private DatasetAlertEntity mapNamedAlert(String alertName, DatasetEntity dataset) {
    return DatasetAlertEntity
        .builder()
        .id(DatasetAlertKey.builder()
            .alertId(ResourceName.getResource(alertName).getId("alerts"))
            .datasetId(dataset.getId())
            .build())
        .build();
  }

}
