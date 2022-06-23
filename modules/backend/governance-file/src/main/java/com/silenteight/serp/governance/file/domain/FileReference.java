package com.silenteight.serp.governance.file.domain;

import lombok.*;

import com.silenteight.sep.base.common.entity.BaseEntity;
import com.silenteight.sep.base.common.entity.IdentifiableEntity;
import com.silenteight.serp.governance.file.domain.dto.FileReferenceDto;

import java.util.UUID;
import javax.persistence.*;

@Entity
@Data
@Table(name = "governance_file")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
class FileReference extends BaseEntity implements IdentifiableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @EqualsAndHashCode.Include
  @Column(updatable = false)
  Long id;

  @NonNull
  @ToString.Include
  @Column(name = "file_id", nullable = false)
  UUID fileId;

  @NonNull
  @ToString.Include
  @Column(name = "original_name", nullable = false)
  String originalName;

  @NonNull
  @ToString.Include
  @Column(name = "uploader_name", nullable = false)
  String uploaderName;

  @NonNull
  @ToString.Include
  @Column(name = "size", nullable = false)
  Long size;

  @NonNull
  @ToString.Include
  @Column(name = "mime_type", nullable = false)
  String mimeType;

  FileReferenceDto toDto() {
    return FileReferenceDto.builder()
        .fileId(getFileId())
        .fileName(getOriginalName())
        .fileSize(getSize())
        .uploaderName(getUploaderName())
        .mimeType(getMimeType())
        .uploadDate(getCreatedAt().toLocalDate())
        .build();
  }
}
