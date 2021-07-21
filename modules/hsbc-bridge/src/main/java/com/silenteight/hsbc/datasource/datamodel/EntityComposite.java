package com.silenteight.hsbc.datasource.datamodel;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import javax.annotation.Nullable;

import static java.util.Objects.nonNull;

public interface EntityComposite {

  @Nullable
  CustomerEntity getCustomerEntity();

  List<WorldCheckEntity> getWorldCheckEntities();

  List<PrivateListEntity> getPrivateListEntities();

  List<CtrpScreening> getCtrpScreeningEntities();

  default boolean hasWorldCheckEntities() {
    return nonNull(getWorldCheckEntities()) && !getWorldCheckEntities().isEmpty();
  }

  default boolean hasPrivateListEntities() {
    return nonNull(getPrivateListEntities()) && !getPrivateListEntities().isEmpty();
  }

  default boolean hasCtrpScreeningEntities() {
    return nonNull(getCtrpScreeningEntities()) && !getCtrpScreeningEntities().isEmpty();
  }

  default Optional<String> getEntityWatchlistId() {
    var listRecordId =
        Stream.concat(getWorldCheckEntities().stream(), getPrivateListEntities().stream())
            .map(ListRecordId::getListRecordId).findFirst();

    return listRecordId.or(this::getCtrpScreeningEntityId);
  }

  private Optional<String> getCtrpScreeningEntityId() {
    return getCtrpScreeningEntities().stream()
        .map(CtrpScreening::getCountryCode)
        .findFirst();
  }
}
