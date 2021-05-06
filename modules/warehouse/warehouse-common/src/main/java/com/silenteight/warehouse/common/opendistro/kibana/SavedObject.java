package com.silenteight.warehouse.common.opendistro.kibana;

import lombok.*;
import lombok.Builder.Default;

import com.silenteight.warehouse.common.opendistro.kibana.dto.SavedObjectDto;
import com.silenteight.warehouse.common.opendistro.kibana.dto.SavedObjectDto.SavedObjectAttributes;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SavedObject {

  @Nullable
  private String type;
  @Nullable
  private String id;
  @NonNull
  @Default
  private Map<String, String> attributes = Map.of();
  @NonNull
  @Default
  private List<SavedObjectReference> references = List.of();
  @Nullable
  @JsonProperty("updated_at")
  private OffsetDateTime updatedAt;

  SavedObjectDto toDto() {
    return SavedObjectDto.builder()
        .id(getId())
        .attributes(new SavedObjectAttributes(getAttributes()))
        .build();
  }
}
