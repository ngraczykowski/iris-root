import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ChangeRequestDecisionDialogComponent } from '@app/pending-changes/components/change-request-decision-dialog/change-request-decision-dialog.component';
import { Header } from '@app/ui-components/header/header';
import { StateContent } from '@app/ui-components/state/state';

@Component({
  selector: 'app-change-request-preview-container',
  templateUrl: './pending-changes-preview-container.component.html',
  styleUrls: ['./pending-changes-preview-container.component.scss']
})
export class PendingChangesPreviewContainerComponent implements OnInit {

  translatePrefix = 'pendingChanges.changeRequestDetails.';
  translatePrefixDialogReject = this.translatePrefix + 'dialogReject.';
  translatePrefixDialogApprove = this.translatePrefix + 'dialogApprove.';
  translatePrefixDecision = this.translatePrefix + 'decision.';

  previewEmptyState: StateContent = {
    centered: true,
    image: null,
    inProgress: false,
    title: null,
    description: this.translatePrefix + 'emptyState.description'
  };

  changeRequestID: Header = {
    title: this.translatePrefix + 'title',
    parameter: 'CR-2032'
  };

  changeRequestData = {
    details: [
      {
        label: 'pendingChanges.changeRequestDetails.details.labels.changeRequestID',
        value: 'CR-2032'
      },
      {
        label: 'pendingChanges.changeRequestDetails.details.labels.reasoningBranchesCount',
        value: 3456
      },
      {
        label: 'pendingChanges.changeRequestDetails.details.labels.requestDate',
        value: '2020-04-12 16:34'
      },
      {
        label: 'pendingChanges.changeRequestDetails.details.labels.author',
        value: 'Patryk Górski'
      },
    ],
    changes: [
      {
        label: 'pendingChanges.changeRequestDetails.changes.labels.solution',
        value: 'Potential True Positive'
      },
      {
        label: 'pendingChanges.changeRequestDetails.changes.labels.status',
        value: 'Disabled'
      },
    ],
    comment: 'Lorem ipsum'
  };

  constructor(public dialog: MatDialog) { }

  ngOnInit() {
  }

  openApproveDialog(): void {
    const dialogRef = this.dialog.open(ChangeRequestDecisionDialogComponent, {
      width: '400px',
      data: {
        title: this.translatePrefixDialogApprove + 'title',
        description: this.translatePrefixDialogApprove + 'description',
        buttonCta: this.translatePrefixDialogApprove + 'buttonCancel',
        buttonCancel: this.translatePrefixDialogApprove + 'buttonCta'
      }
    });
  }

  openRejectDialog(): void {
    const dialogRef = this.dialog.open(ChangeRequestDecisionDialogComponent, {
      width: '400px',
      data: {
        title: this.translatePrefixDialogReject + 'title',
        description: this.translatePrefixDialogReject + 'description',
        buttonCta: this.translatePrefixDialogReject + 'buttonCancel',
        buttonCancel: this.translatePrefixDialogReject + 'buttonCta'
      }
    });
  }

}
