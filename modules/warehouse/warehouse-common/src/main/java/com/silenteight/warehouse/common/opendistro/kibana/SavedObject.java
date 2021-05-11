package com.silenteight.warehouse.common.opendistro.kibana;

import lombok.*;
import lombok.Builder.Default;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.List;
import javax.annotation.Nullable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class SavedObject<T> {

  @Nullable
  private String type;
  @Nullable
  private String id;
  @NonNull
  private T attributes;
  @NonNull
  @Default
  private List<SavedObjectReference> references = List.of();
  @Nullable
  @JsonProperty("updated_at")
  private OffsetDateTime updatedAt;
  @Nullable
  private String version;
}
