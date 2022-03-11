package com.silenteight.scb.ingest.domain.model;

import lombok.Builder;
import lombok.Value;

import com.silenteight.scb.ingest.domain.model.AlertMetadataItem.HeaderInfo;
import com.silenteight.scb.ingest.domain.model.AlertMetadataItem.MatchRecords;
import com.silenteight.scb.ingest.domain.model.AlertMetadataItem.MatchRecords.MatchRecord;
import com.silenteight.scb.ingest.domain.model.StopDescriptorsItem.StopDescriptor;

import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Value
@Builder
public class AlertWithMatchesMetadata {

  String alertId;
  AlertStatus status;
  AlertMetadata metadata;
  AlertErrorDescription alertErrorDescription;

  @Builder.Default
  List<Match> matches = List.of();

  public static AlertWithMatchesMetadata of(
      String alertId,
      AlertMetadataItem alertMetadata,
      SupplementalInfoMetadataItem infoMetadata) {
    var matches = createMatches(alertMetadata);
    if (matches.isEmpty()) {
      return ofFailure(alertId, AlertErrorDescription.NO_MATCHES);
    }

    return AlertWithMatchesMetadata.builder()
        .alertId(alertId)
        .status(AlertStatus.SUCCESS)
        .metadata(createMetadata(alertMetadata, infoMetadata))
        .matches(matches)
        .build();
  }

  public static AlertWithMatchesMetadata ofFailure(
      String alertId, AlertErrorDescription errorDescription) {
    return AlertWithMatchesMetadata.builder()
        .alertId(alertId)
        .status(AlertStatus.FAILURE)
        .alertErrorDescription(errorDescription)
        .build();
  }

  private static AlertMetadata createMetadata(
      AlertMetadataItem alertMetadataItem, SupplementalInfoMetadataItem supplementalInfoMetadata) {
    return AlertMetadata.builder()
        .currentVersionId(getCurrentVersionId(alertMetadataItem.getHeaderInfo()))
        .stopDescriptorNames(toStopDescriptorNames(alertMetadataItem.getHeaderInfo()))
        .datasetId(supplementalInfoMetadata.getSupplementalInfo().getDatasetId())
        .datasetName(supplementalInfoMetadata.getSupplementalInfo().getDatasetName())
        .uniqueCustId(supplementalInfoMetadata.getSupplementalInfo().getUniqueCustId())
        .masterId(supplementalInfoMetadata.getSupplementalInfo().getMasterId())
        .busDate(supplementalInfoMetadata.getSupplementalInfo().getBusDate())
        .build();
  }

  private static String getCurrentVersionId(AlertMetadataItem.HeaderInfo headerInfo) {
    return Optional.ofNullable(headerInfo)
        .map(HeaderInfo::getCurrentVersionId)
        .orElse(null);
  }

  private static List<String> toStopDescriptorNames(AlertMetadataItem.HeaderInfo headerInfo) {
    return Optional.ofNullable(headerInfo.getStopDescriptors())
        .map(StopDescriptorsItem::getDescriptors)
        .stream()
        .flatMap(Collection::stream)
        .map(StopDescriptor::getName)
        .collect(Collectors.toList());
  }

  private static List<Match> createMatches(AlertMetadataItem alertMetadata) {
    return Optional.ofNullable(alertMetadata.getMatchRecords())
        .map(MatchRecords::getRecords)
        .stream()
        .flatMap(Collection::stream)
        .map(AlertWithMatchesMetadata::createMatch)
        .collect(Collectors.toList());
  }

  private static Match createMatch(MatchRecord match) {
    if (StringUtils.isBlank(match.getMatchId())) {
      throw new IllegalArgumentException("MatchId is missing");
    }
    return Match.builder().id(match.getMatchId()).build();
  }
}
