import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import {
  stateEmptyList,
  stateError,
  stateLoading
} from '@app/circuit-breaker-dashboard/containers/circuit-breaker-branch-list/circuit-breaker-branch-list.states';
import {
  CircuitBreakerDiscrepancyStatus,
  DiscrepantBranchesResponse
} from '@app/circuit-breaker-dashboard/models/circuit-breaker';
import { CircuitBreakerService } from '@app/circuit-breaker-dashboard/services/circuit-breaker.service';

@Component({
  selector: 'app-circuit-breaker-branch-list',
  templateUrl: './circuit-breaker-branch-list.component.html',
  styleUrls: ['./circuit-breaker-branch-list.component.scss']
})
export class CircuitBreakerBranchListComponent implements OnInit {

  constants = {
    stateLoading: stateLoading,
    stateEmptyList: stateEmptyList,
    stateError: stateError,
  };

  discrepantBranches: DiscrepantBranchesResponse[] = [];

  loadingDiscrepantBranches = true;
  discrepantBranchesFailedToLoad = false;

  discrepancyStatuses: CircuitBreakerDiscrepancyStatus[];

  constructor(
      private circuitBreakerService: CircuitBreakerService,
      private route: ActivatedRoute,
  ) {
    this.discrepancyStatuses = route.snapshot.data.discrepancyStatuses;
  }

  ngOnInit() {
    this.loadDiscrepantBranches(this.discrepancyStatuses);
  }

  loadDiscrepantBranches(discrepancyStatuses: CircuitBreakerDiscrepancyStatus[]) {
    this.loadingDiscrepantBranches = true;

    this.circuitBreakerService.getBranchesWithDiscrepancies(discrepancyStatuses)
        .subscribe(data => {
          this.discrepantBranches = data;
          this.loadingDiscrepantBranches = false;
        }, error => {
          this.discrepantBranchesFailedToLoad = true;
          this.loadingDiscrepantBranches = false;
        });
  }

}
