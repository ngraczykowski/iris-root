package com.silenteight.warehouse.report.generation;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NonNull;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Nullable;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static java.util.Optional.empty;

@Data
@Builder
public class ReportRequestData {

  @Nullable
  Long domainId;

  @NonNull
  String fileStorageName;

  @Default
  Optional<OffsetDateTime> from = empty();

  @Default
  Optional<OffsetDateTime> to = empty();

  @Default
  List<String> sqlTemplates = emptyList();

  @NonNull
  String selectSqlQuery;

  @Nullable
  String analysisId;

  @Default
  Set<String> dataAccessPermissions = emptySet();
}
