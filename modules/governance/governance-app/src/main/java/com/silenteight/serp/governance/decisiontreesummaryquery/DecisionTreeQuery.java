package com.silenteight.serp.governance.decisiontreesummaryquery;

import lombok.*;

import com.silenteight.proto.serp.v1.governance.DecisionTreeSummary;

import org.hibernate.annotations.Immutable;

import java.util.List;
import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Immutable
class DecisionTreeQuery {

  @Id
  private Long decisionTreeId;

  private String decisionTreeName;

  @ElementCollection
  @CollectionTable(joinColumns = @JoinColumn(name = "decision_tree_id"))
  @Column(name = "decision_group_name")
  List<String> decisionGroupNames;

  DecisionTreeSummary summarize() {
    return DecisionTreeSummary.newBuilder()
        .setName(getDecisionTreeName())
        .addAllDecisionGroup(getDecisionGroupNames())
        .setId(getDecisionTreeId())
        .build();
  }
}
