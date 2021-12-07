package com.silenteight.universaldatasource.api.library.ispep.v2;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.api.ispep.v2.WatchlistItem;

import java.util.List;

@Value
@Builder(access = AccessLevel.PACKAGE)
public class WatchlistItemOut {

  String id;

  String type;

  @Builder.Default
  List<String> countries = List.of();

  String furtherInformation;

  @Builder.Default
  List<String> linkedPepsUids = List.of();

  static WatchlistItemOut createFrom(WatchlistItem input) {
    return WatchlistItemOut.builder()
        .id(input.getId())
        .type(input.getType())
        .countries(input.getCountriesList())
        .furtherInformation(input.getFurtherInformation())
        .linkedPepsUids(input.getLinkedPepsUidsList())
        .build();
  }
}
