import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { MatDialog, MatDialogRef, MatSnackBar } from '@angular/material';
import { PageEvent } from '@angular/material/paginator';
import { CircuitBreakerAlertsTableComponent } from '@app/circuit-breaker-dashboard/components/circuit-breaker-alerts-table/circuit-breaker-alerts-table.component';
import { CircuitBreakerService } from '@app/circuit-breaker-dashboard/services/circuit-breaker.service';
import { DialogComponent } from '@app/ui-components/dialog/dialog.component';
import { StateContent } from '@app/ui-components/state/state';
import { Router } from '@angular/router';
import { environment } from '@env/environment';
import { TranslateService } from '@ngx-translate/core';
import { EMPTY } from 'rxjs';
import { switchMap } from 'rxjs/operators';

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
  rowsPerPage = environment.tablePagination.rowsPerPage;

  constructor(
      private circuitBreakerService: CircuitBreakerService,
      private translate: TranslateService,
      private snackbar: MatSnackBar,
      private dialog: MatDialog,
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

  archive(decisionTreeId, featureVectorId): void {
    this.openArchiveDialog(decisionTreeId, featureVectorId).afterClosed().pipe(
      switchMap((result) => result === 'submit' ?
          this.circuitBreakerService.archiveDiscrepancies([...this.discrepanciesIds]) : EMPTY))
    .subscribe(
      () => this.archivisationSuccess(decisionTreeId, featureVectorId),
        () => this.archivisationFailure()
    );
  }

  private archivisationSuccess(decisionTreeId, featureVectorId): void {
    const feedbackContent = this.translate.instant(
      'circuitBreakerDashboard.element.archiveSuccessSnackbar.content', {
        reasoningBranch: `${decisionTreeId}-${featureVectorId}`
      });
    this.snackbar.open(feedbackContent, null, {
      duration: 2000
    });
  }

  private archivisationFailure(): void {
    this.dialog.open(DialogComponent, {
      autoFocus: false,
      width: '450px',
      data: {
        title: 'circuitBreakerDashboard.element.archiveFailureDialog.title',
        description: 'circuitBreakerDashboard.element.archiveFailureDialog.description',
        buttonClose: 'circuitBreakerDashboard.element.archiveFailureDialog.confirmation'
      }
    });
  }

  private openArchiveDialog(decisionTreeId, featureVectorId): MatDialogRef<DialogComponent> {
    return this.dialog.open(DialogComponent, {
      autoFocus: false,
      width: '450px',
      data: {
        title: 'circuitBreakerDashboard.element.archiveDialog.title',
        description: 'circuitBreakerDashboard.element.archiveDialog.description',
        value: `${decisionTreeId}-${featureVectorId}`,
        buttonClose: 'circuitBreakerDashboard.element.archiveDialog.cancellation',
        buttonCta: 'circuitBreakerDashboard.element.archiveDialog.confirmation'
      }
    });
  }
}
