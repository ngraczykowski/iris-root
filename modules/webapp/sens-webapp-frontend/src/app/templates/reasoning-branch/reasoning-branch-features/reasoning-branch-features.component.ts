import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-reasoning-branch-features',
  templateUrl: './reasoning-branch-features.component.html',
  styleUrls: ['./reasoning-branch-features.component.scss']
})
export class ReasoningBranchFeaturesComponent implements OnInit {

  constructor() { }

  @Input() features;

  emptyStatesList = [
    'AGENT_SKIPPED',
    'NO_DATA'
  ];

  ngOnInit() {
  }

  isEmptyState(featureValue) {
    return featureValue && this.emptyStatesList.includes(featureValue);
  }
}
