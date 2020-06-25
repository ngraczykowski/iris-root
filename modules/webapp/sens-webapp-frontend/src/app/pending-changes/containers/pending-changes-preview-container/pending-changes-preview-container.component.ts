import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { PendingChange } from '@app/pending-changes/models/pending-changes';
import { PendingChangesService } from '@app/pending-changes/services/pending-changes.service';
import { AuthenticatedUserFacade } from '@app/shared/security/authenticated-user-facade.service';
import { DialogComponent } from '@app/ui-components/dialog/dialog.component';
import { StateContent } from '@app/ui-components/state/state';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-change-request-preview-container',
  templateUrl: './pending-changes-preview-container.component.html',
  styleUrls: ['./pending-changes-preview-container.component.scss']
})
export class PendingChangesPreviewContainerComponent implements OnInit {

  @Input() changeRequestDetails: PendingChange;
  @Output() refreshList = new EventEmitter();

  translatePrefix = 'pendingChanges.changeRequestDetails.';
  translatePrefixDialogReject = this.translatePrefix + 'dialogReject.';
  translatePrefixDialogApprove = this.translatePrefix + 'dialogApprove.';
  translatePrefixDialogCancel = this.translatePrefix + 'dialogCancel.';
  translatePrefixDecision = this.translatePrefix + 'decision.';
  translatePrefixFeedback = this.translatePrefix + 'feedback.';

  previewEmptyState: StateContent = {
    centered: true,
    image: null,
    inProgress: false,
    title: null,
    description: this.translatePrefix + 'emptyState.description'
  };

  constructor(
      public dialog: MatDialog,
      private readonly authenticatedUserFacade: AuthenticatedUserFacade,
      private pendingChangesService: PendingChangesService,
      private _snackBar: MatSnackBar,
      private translate: TranslateService) { }

  ngOnInit() {
  }

  openApproveDialog(): void {
    const dialogRef = this.dialog.open(DialogComponent, {
      width: '440px',
      data: {
        title: this.translatePrefixDialogApprove + 'title',
        description: this.translatePrefixDialogApprove + 'description',
        buttonCta: this.translatePrefixDialogApprove + 'buttonCta',
        buttonClose: this.translatePrefixDialogApprove + 'buttonClose'
      }
    });
    const sub = dialogRef.afterClosed().subscribe((action) => {
      if (action === 'submit') {
        this.sendDecision('approve');
      }
    });
  }

  openRejectDialog(): void {
    const dialogRef = this.dialog.open(DialogComponent, {
      width: '440px',
      data: {
        title: this.translatePrefixDialogReject + 'title',
        description: this.translatePrefixDialogReject + 'description',
        buttonCta: this.translatePrefixDialogReject + 'buttonCta',
        buttonClose: this.translatePrefixDialogReject + 'buttonClose'
      }
    });
    const sub = dialogRef.afterClosed().subscribe((action) => {
      if (action === 'submit') {
        this.sendDecision('reject');
      }
    });
  }

  openCancelDialog(): void {
    const dialogRef = this.dialog.open(DialogComponent, {
      width: '440px',
      data: {
        title: this.translatePrefixDialogCancel + 'title',
        description: this.translatePrefixDialogCancel + 'description',
        buttonCta: this.translatePrefixDialogCancel + 'buttonCta',
        buttonClose: this.translatePrefixDialogCancel + 'buttonClose'
      }
    });
    const sub = dialogRef.afterClosed().subscribe((action) => {
      if (action === 'submit') {
        this.sendDecision('cancel');
      }
    });
  }

  sendDecision(decision) {
    this.pendingChangesService.changeRequestDecision(
        this.changeRequestDetails.id.toString(),
        decision
    ).subscribe((res: any) => {
      this.refreshList.emit();
      this.feedbackMessage(decision);
    });
  }

  feedbackMessage(message: string) {
    const feedbackContent = this.translate.instant(
        this.translatePrefixFeedback + message,
        {
          changeRequestID: this.changeRequestDetails.id.toString()
        });
    this._snackBar.open(feedbackContent, '', {
      duration: 2000
    });
  }

  generateChangeRequestHeader() {
    return {
      title: 'pendingChanges.changeRequestDetails.title',
      parameter: this.changeRequestDetails.id.toString(),
    };
  }

  generateChangeRequestContent() {
    return {
      details: {
        id: {
          label: 'pendingChanges.changeRequestDetails.details.labels.changeRequestID',
          value: this.changeRequestDetails.id.toString(),
        },
        branches: {
          label: 'pendingChanges.changeRequestDetails.details.labels.reasoningBranchesCount',
          value: this.changeRequestDetails.reasoningBranchIds.length.toString(),
        },
        createdAt: {
          label: 'pendingChanges.changeRequestDetails.details.labels.requestDate',
          value: this.changeRequestDetails.createdAt,
        },
        createdBy: {
          label: 'pendingChanges.changeRequestDetails.details.labels.author',
          value: this.changeRequestDetails.createdBy,
        },
      },
      changes: {
        aiSolution: {
          label: 'pendingChanges.changeRequestDetails.changes.labels.solution',
          value: this.changeRequestDetails.aiSolution,
        },
        active: {
          label: 'pendingChanges.changeRequestDetails.changes.labels.status',
          value: this.changeRequestDetails.active,
        },
      },
      comment: this.changeRequestDetails.comment,
    };
  }

  hasProperRoleToDecide(requiredRole) {
    const roles = this.authenticatedUserFacade.getUserRoles();

    return roles.includes(requiredRole);
  }

  canCancel() {
    return this.hasProperRoleToDecide('Business Operator');
  }

  canApproveOrReject() {
    return this.hasProperRoleToDecide('Approver');
  }
}
