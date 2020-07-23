import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';
import { ActivatedRoute } from '@angular/router';
import {
  stateError,
  stateLoading,
  translateKey
} from '@app/circuit-breaker-dashboard/components/discrepant-branch/discrepant-branch.constants';
import {
  CircuitBreakerDiscrepancyStatus,
  DiscrepantBranchesResponse
} from '@app/circuit-breaker-dashboard/models/circuit-breaker';
import { CircuitBreakerService } from '@app/circuit-breaker-dashboard/services/circuit-breaker.service';
import { environment } from '@env/environment';

@Component({
  selector: 'app-discrepant-branch',
  templateUrl: './discrepant-branch.component.html',
  styleUrls: ['./discrepant-branch.component.scss']
})
export class DiscrepantBranchComponent implements OnInit {

  @Input() discrepantBranch: DiscrepantBranchesResponse;
  @Output() refreshBranchList: EventEmitter<void> = new EventEmitter<void>();

  constants = {
    stateLoading: stateLoading,
    stateError: stateError,
    translateKey: translateKey,
    dateFormatting: environment.dateFormatting,
    tableInit: environment.tablePagination,
    rowsPerPage: environment.tablePagination.rowsPerPage,
  };

  loadingDiscrepancies = false;
  failedToLoadDiscrepancies = false;

  discrepanciesList = [];
  discrepanciesIds = [];
  numberOfRows: number;

  readonly discrepancyStatuses: CircuitBreakerDiscrepancyStatus[];
  showActionButtons: boolean;

  constructor(
      private circuitBreakerService: CircuitBreakerService,
      private route: ActivatedRoute,
  ) {
    this.discrepancyStatuses = route.snapshot.data.discrepancyStatuses;
  }

  ngOnInit() {
    this.showActionButtons =
        this.discrepancyStatuses.includes(CircuitBreakerDiscrepancyStatus.ACTIVE);
  }

  initDiscrepanciesTable(discrepantBranch: DiscrepantBranchesResponse) {
    const decisionTreeId = discrepantBranch.branchId.decisionTreeId;
    const featureVectorId = discrepantBranch.branchId.featureVectorId;

    this.loadDiscrepanciesIds(decisionTreeId, featureVectorId).subscribe(ids => {
          this.discrepanciesIds = this.sortDiscrepanciesIds(ids);
          this.numberOfRows = ids.length;
          this.loadDiscrepanciesList(this.generateIdsList(
              this.constants.tableInit.firstPage,
              this.constants.tableInit.defaultPageSize
          ));
        },
        error => {
          this.setErrorState();
        });
  }

  sortDiscrepanciesIds(ids) {
    return ids.sort((a, b) => a - b);
  }

  loadDiscrepanciesIds(decisionTreeId, reasoningBranchId) {
    const discrepancyId = `${decisionTreeId}-${reasoningBranchId}`;
    this.resetView();
    this.loadingDiscrepancies = true;

    return this.circuitBreakerService.getDiscrepanciesIds(discrepancyId, this.discrepancyStatuses);
  }

  loadDiscrepanciesList(ids) {
    this.circuitBreakerService.getDiscrepanciesList(ids).subscribe(discrepancies => {
      this.discrepanciesList = discrepancies;
      this.resetView();
    }, error => {
      this.setErrorState();
    });
  }

  resetView() {
    this.loadingDiscrepancies = false;
    this.failedToLoadDiscrepancies = false;
  }

  setErrorState() {
    this.failedToLoadDiscrepancies = true;
    this.loadingDiscrepancies = false;
  }

  generateIdsList(page, pageSize) {
    const from = page * pageSize;
    const to = from + pageSize;
    return this.discrepanciesIds.slice(from, to);
  }

  reloadTableContent($event: PageEvent) {
    this.loadingDiscrepancies = true;
    this.loadDiscrepanciesList(this.generateIdsList($event.pageIndex, $event.pageSize));
  }

  reloadBranchList() {
    this.refreshBranchList.emit();
  }
}
