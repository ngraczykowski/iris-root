import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { DiscrepantBranchesResponse } from '@app/circuit-breaker-dashboard/models/circuit-breaker';
import { CircuitBreakerService } from '@app/circuit-breaker-dashboard/services/circuit-breaker.service';
import { DialogComponent } from '@app/ui-components/dialog/dialog.component';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-discrepant-branch-action',
  templateUrl: './discrepant-branch-action.component.html',
  styleUrls: ['./discrepant-branch-action.component.scss']
})
export class DiscrepantBranchActionComponent implements OnInit {

  @Input() discrepantBranch: DiscrepantBranchesResponse;
  @Input() discrepanciesIds: [];

  @Output() reloadDiscrepantBranches: EventEmitter<void> = new EventEmitter<void>();

  translatePrefix = 'circuitBreakerDashboard.discrepantBranches.';

  constructor(
      private circuitBreakerService: CircuitBreakerService,
      private router: Router,
      private snackbar: MatSnackBar,
      private translate: TranslateService,
      private dialog: MatDialog,
  ) { }

  ngOnInit() {
  }

  createChangeRequest(discrepantBranch: DiscrepantBranchesResponse) {
    const decisionTreeId = discrepantBranch.branchId.decisionTreeId;
    const featureVectorId = discrepantBranch.branchId.featureVectorId;

    this.router.navigate(
        ['reasoning-branches/change-request'],
        {
          queryParams: {
            dt_id: decisionTreeId,
            fv_ids: featureVectorId
          }
        });
  }

  archive(decisionTreeId, featureVectorId): void {
    this.circuitBreakerService.archiveDiscrepancies([...this.discrepanciesIds])
        .subscribe(
            () => this.archivisationSuccess(decisionTreeId, featureVectorId),
            () => this.archivisationFailure()
        );
  }

  private archivisationSuccess(decisionTreeId, featureVectorId): void {
    const feedbackContent = this.translate.instant(
        'circuitBreakerDashboard.discrepantBranches.archiveDialog.success.content', {
          reasoningBranch: `${decisionTreeId}-${featureVectorId}`
        });
    this.snackbar.open(feedbackContent, null, {
      duration: 2000
    });
    this.reloadDiscrepantBranches.emit();
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
}
