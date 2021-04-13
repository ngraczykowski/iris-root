package com.silenteight.adjudication.engine.solve;

import lombok.*;

import java.io.Serializable;
import javax.persistence.EmbeddedId;

import static lombok.AccessLevel.NONE;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Data
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
@Setter(NONE)
@Builder(access = PACKAGE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
class MissingFeatureAgentConfigEntity implements Serializable {

  private static final long serialVersionUID = 8159383389765171059L;

  @EmbeddedId
  private MissingFeatureAgentConfigKey id;
}
