package com.silenteight.adjudication.engine.comments.comment;

import lombok.*;
import lombok.EqualsAndHashCode.Include;

import com.silenteight.sep.base.common.entity.BaseEntity;
import com.silenteight.sep.base.common.entity.IdentifiableEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.*;

@Data
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
@Setter(NONE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Entity
@Builder(access = PACKAGE)
public class CommentTemplate extends BaseEntity implements IdentifiableEntity {

  @Id
  @Column(name = "comment_template_id", insertable = false, updatable = false, nullable = false)
  @GeneratedValue(strategy = IDENTITY)
  @Setter(PUBLIC)
  @Include
  private Long id;

  @Column(nullable = false)
  @NonNull
  private String templateName;

  @Column(nullable = false)
  private Integer revision;

  @Column(nullable = false)
  @NonNull
  private String template;

}
