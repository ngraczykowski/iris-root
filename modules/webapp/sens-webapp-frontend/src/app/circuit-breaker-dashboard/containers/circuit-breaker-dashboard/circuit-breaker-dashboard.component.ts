import { Component, OnInit } from '@angular/core';
import { Header } from '@app/ui-components/header/header';
import { StateContent } from '@app/ui-components/state/state';

@Component({
  selector: 'app-circuit-breaker-dashboard',
  templateUrl: './circuit-breaker-dashboard.component.html',
  styleUrls: ['./circuit-breaker-dashboard.component.scss']
})
export class CircuitBreakerDashboardComponent implements OnInit {

  translatePrefix = 'circuitBreakerDashboard.';
  loadingTranslatePrefix = this.translatePrefix + 'stateLoading.';
  emptyStateTranslatePrefix = this.translatePrefix + 'emptyState.';

  header: Header;

  stateLoading: StateContent = {
    title: this.loadingTranslatePrefix + 'title',
    inProgress: true,
    centered: true
  };

  stateEmptyList: StateContent = {
    title: this.emptyStateTranslatePrefix + 'title',
    centered: true
  };

  circuitBreakerLoading = true;
  circuitBreakerEmptyState = true;
  circuitBreakerList = true;

  circuitBreakerCurrentList = [
    {
      'discrepantBranchId': {
        'decisionTreeId': 1,
        'featureVectorId': 1
      },
      'detectedAt': '2020-03-03T00:00:00Z'
    },
    {
      'discrepantBranchId': {
        'decisionTreeId': 1,
        'featureVectorId': 2
      },
      'detectedAt': '2020-03-03T00:00:00Z'
    },
    {
      'discrepantBranchId': {
        'decisionTreeId': 1,
        'featureVectorId': 2
      },
      'detectedAt': '2020-03-03T00:00:00Z'
    }
  ];

  constructor() { }

  ngOnInit() {
    this.generateHeader();
  }

  generateHeader() {
    if (this.circuitBreakerCurrentList.length > 0) {
      this.header = {
        title: this.translatePrefix + 'title',
        count: this.circuitBreakerCurrentList.length.toString(),
      };
    } else {
      this.header = {
        title: this.translatePrefix + 'title',
      };
    }
  }
}
