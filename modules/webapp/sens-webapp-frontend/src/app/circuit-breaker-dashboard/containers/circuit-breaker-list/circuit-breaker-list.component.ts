import { ChangeDetectorRef, Component, Input, OnInit, ViewChild } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';
import { CircuitBreakerAlertsTableComponent } from '@app/circuit-breaker-dashboard/components/circuit-breaker-alerts-table/circuit-breaker-alerts-table.component';
import { CircuitBreakerService } from '@app/circuit-breaker-dashboard/services/circuit-breaker.service';
import { StateContent } from '@app/ui-components/state/state';
import { Router } from '@angular/router';
import { environment } from '@env/environment';

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
  @ViewChild(CircuitBreakerAlertsTableComponent, {static: false}) circuitBreakerAlertsTableComponent: CircuitBreakerAlertsTableComponent;

  discrepanciesList = [];

  stateLoading: StateContent = {
    title: this.loadingTranslatePrefix + 'title',
    inProgress: true
  };

  stateError: StateContent = {
    title: this.errorTranslatePrefix + 'title',
    description: this.errorTranslatePrefix + 'description',
    button: this.errorTranslatePrefix + 'button',
    centered: true,
  };

  loadingDetails = true;
  loadingTableContent = false;
  showDetails = false;
  showError = false;

  discrepanciesIds = [];
  numberOfRows: number;

  tableInit = environment.tablePagination;

  dateFormatting = environment.dateFormatting;

  constructor(
      private circuitBreakerService: CircuitBreakerService,
      private router: Router
  ) { }

  ngOnInit() {
  }

  initDiscrepanciesTable(decisionTreeId, reasoningBranchId) {
    this.loadDiscrepanciesIds(decisionTreeId, reasoningBranchId).subscribe(ids => {
          this.discrepanciesIds = this.sortDiscrepanciesIds(ids);
          this.numberOfRows = ids.length;
          this.loadDiscrepanciesList(this.generateIdsList(
              this.tableInit.firstPage,
              this.tableInit.defaultPageSize
          ));
        },
        error => {
          this.setErrorState();
        });
  }

  sortDiscrepanciesIds(ids) {
    return ids.sort((a, b) => a - b);
  }

  resetDiscrepanciesTable() {
    this.discrepanciesIds = [];
  }

  loadDiscrepanciesIds(decisionTreeId, reasoningBranchId) {
    const discrepancyId = `${decisionTreeId}-${reasoningBranchId}`;
    this.resetView();
    this.loadingDetails = true;

    return this.circuitBreakerService.getDiscrepanciesIds(discrepancyId);
  }

  loadDiscrepanciesList(ids) {
    this.circuitBreakerService.getDiscrepanciesList(ids).subscribe(discrepancies => {
      this.discrepanciesList = discrepancies;
      this.resetView();
      this.showDetails = true;
    }, error => {
      this.setErrorState();
    });
  }

  resetView() {
    this.loadingDetails = false;
    this.showDetails = false;
    this.showError = false;
    this.loadingTableContent = false;
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

  reloadTableContent($event: PageEvent) {
    this.loadingTableContent = true;
    this.loadDiscrepanciesList(this.generateIdsList($event.pageIndex, $event.pageSize));
  }

  generateIdsList(page, pageSize) {
    const from = page * pageSize;
    const to = from + pageSize;
    return this.discrepanciesIds.slice(from, to);
  }
}
