import { Component, Input } from '@angular/core';
import { DecisionTreeFeatures } from '@model/decision-tree.model';

@Component({
  selector: 'app-decision-tree-info-features',
  templateUrl: './decision-tree-info-features.component.html',
  styleUrls: ['./decision-tree-info-features.component.scss']
})
export class DecisionTreeInfoFeaturesComponent {

  constructor() { }

  @Input() decisionTreeAgents: DecisionTreeFeatures[];
  @Input() decisionTreeFeatures: DecisionTreeFeatures[];

  @Input() hasDecisionTreeViewAccess: boolean;

  shouldShowEmptyState() {
    return this.decisionTreeAgents.length === 0 && this.decisionTreeFeatures.length === 0;
  }
}
