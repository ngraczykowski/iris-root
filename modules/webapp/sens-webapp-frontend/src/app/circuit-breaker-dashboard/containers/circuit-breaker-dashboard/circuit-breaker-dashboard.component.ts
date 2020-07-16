import { Component, OnInit } from '@angular/core';
import { CircuitBreakerService } from '@app/circuit-breaker-dashboard/services/circuit-breaker.service';
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
  errorTranslatePrefix = this.translatePrefix + 'errorState.';

  header: Header = {
    title: this.translatePrefix + 'title'
  };

  stateLoading: StateContent = {
    title: this.loadingTranslatePrefix + 'title',
    inProgress: true,
    centered: true
  };

  stateEmptyList: StateContent = {
    title: this.emptyStateTranslatePrefix + 'title',
    centered: true
  };

  stateError: StateContent = {
    title: this.errorTranslatePrefix + 'title',
    description: this.errorTranslatePrefix + 'description',
    button: this.errorTranslatePrefix + 'button'
  };

  circuitBreakerLoading = true;
  circuitBreakerEmptyState = false;
  circuitBreakerList = false;
  circuitBreakerError = false;

  circuitBreakerCurrentList = [];

  constructor(
      private circuitBreakerService: CircuitBreakerService
  ) { }

  ngOnInit() {
    this.loadDiscrepantBranches();
  }

  generateHeader() {
    if (this.circuitBreakerCurrentList.length > 0) {
      this.header = {
        title: this.translatePrefix + 'title',
        count: this.circuitBreakerCurrentList.length.toString(),
      };
    }
  }

  resetView() {
    this.circuitBreakerLoading = false;
    this.circuitBreakerEmptyState = false;
    this.circuitBreakerError = false;
    this.circuitBreakerList = false;
  }

  loadDiscrepantBranches() {
    this.resetView();
    this.circuitBreakerLoading = true;
    this.circuitBreakerService.getBranchesWithDiscrepancies()
        .subscribe(data => {
          this.circuitBreakerCurrentList = data;
          this.resetView();
          if (data.length > 0) {
            this.circuitBreakerList = true;
          } else {
            this.circuitBreakerEmptyState = true;
          }
          this.generateHeader();
        }, error => {
          this.resetView();
          this.circuitBreakerError = true;
        });
  }
}
