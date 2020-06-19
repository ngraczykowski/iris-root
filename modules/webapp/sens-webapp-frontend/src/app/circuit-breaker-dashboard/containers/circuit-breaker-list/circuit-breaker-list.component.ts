import { Component, Input, OnInit } from '@angular/core';
import { CircuitBreakerService } from '@app/circuit-breaker-dashboard/services/circuit-breaker.service';
import { StateContent } from '@app/ui-components/state/state';
import { Router } from '@angular/router';

@Component({
  selector: 'app-circuit-breaker-list',
  templateUrl: './circuit-breaker-list.component.html',
  styleUrls: ['./circuit-breaker-list.component.scss']
})
export class CircuitBreakerListComponent implements OnInit {

  translatePrefix = 'circuitBreakerDashboard.element.';
  loadingTranslatePrefix = this.translatePrefix + 'loading.';
  errorTranslatePrefix = this.translatePrefix + 'errorState.';

  @Input() circuitBreakerList;

  discrepanciesList = [];

  stateLoading: StateContent = {
    title: this.loadingTranslatePrefix + 'title',
    inProgress: true
  };

  stateError: StateContent = {
    title: this.errorTranslatePrefix + 'title',
    description: this.errorTranslatePrefix + 'description',
    button: this.errorTranslatePrefix + 'button'
  };

  loadingDetails = true;
  showDetails = false;
  showError = false;

  constructor(
      private circuitBreakerService: CircuitBreakerService,
      private router: Router
  ) { }

  ngOnInit() {
  }

  loadDiscrepancies(decisionTreeId, reasoningBranchId) {
    const discrepancyId = `${decisionTreeId}-${reasoningBranchId}`;
    this.resetView();
    this.loadingDetails = true;

    this.circuitBreakerService.getDiscrepanciesIds(discrepancyId)
        .subscribe(list => {
          this.circuitBreakerService.getDiscrepanciesList(list).subscribe(discrepancies => {
            this.discrepanciesList = discrepancies;
            this.resetView();
            this.showDetails = true;
          }, error => {
            this.setErrorState();
          });
        }, error => {
          this.setErrorState();
        });
  }

  resetView() {
    this.loadingDetails = false;
    this.showDetails = false;
    this.showError = false;
  }

  setErrorState() {
    this.resetView();
    this.showError = true;
  }

  createChangeRequest(decisionTreeId, featureVectorId) {
    this.router.navigate(
        ['reasoning-branches/change-request'],
        {
          queryParams: {
            dt_id: decisionTreeId,
            fv_ids: featureVectorId
          }
        });
  }
}
