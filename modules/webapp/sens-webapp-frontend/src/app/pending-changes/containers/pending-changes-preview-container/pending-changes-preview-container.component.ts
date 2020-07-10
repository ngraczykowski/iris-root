import { Component, EventEmitter, Input, Output, ViewChild } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ChangeRequestPreviewComponent } from '@app/pending-changes/components/change-request-preview/change-request-preview.component';
import { ChangeRequestDecisionRequestBody } from '@app/pending-changes/models/change-request-decision-request-body';
import { ChangeRequestDecision } from '@app/pending-changes/models/change-request-decision.enum';
import { PendingChange } from '@app/pending-changes/models/pending-changes';
import { PendingChangesService } from '@app/pending-changes/services/pending-changes.service';
import { AuthenticatedUserFacade } from '@app/shared/security/authenticated-user-facade.service';
import { Authority } from '@app/core/authorities/model/authority.enum';
import { DialogComponent } from '@app/ui-components/dialog/dialog.component';
import { StateContent } from '@app/ui-components/state/state';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-change-request-preview-container',
  templateUrl: './pending-changes-preview-container.component.html',
  styleUrls: ['./pending-changes-preview-container.component.scss']
})
export class PendingChangesPreviewContainerComponent {

  @Input() changeRequestDetails: PendingChange;
  @Output() refreshList = new EventEmitter();

  @ViewChild('previewComponent', {static: false}) previewComponent: ChangeRequestPreviewComponent;

  translatePrefix = 'pendingChanges.changeRequestDetails.';
  translatePrefixDialogReject = this.translatePrefix + 'dialogReject.';
  translatePrefixDialogApprove = this.translatePrefix + 'dialogApprove.';
  translatePrefixDialogCancel = this.translatePrefix + 'dialogCancel.';
  translatePrefixDecision = this.translatePrefix + 'decision.';
  translatePrefixFeedback = this.translatePrefix + 'feedback.';

  canCancel: boolean = this.authenticatedUserFacade.hasRole(Authority.BUSINESS_OPERATOR);
  canApproveOrReject: boolean = this.authenticatedUserFacade.hasRole(Authority.APPROVER);

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
      private translate: TranslateService) {
  }

  approve() {
    this.submit(ChangeRequestDecision.APPROVE, {
      autoFocus: false,
      data: {
        title: this.translatePrefixDialogApprove + 'title',
        description: this.translatePrefixDialogApprove + 'description',
        buttonCta: this.translatePrefixDialogApprove + 'buttonCta',
        buttonClose: this.translatePrefixDialogApprove + 'buttonClose'
      }
    }, {
      approverComment: this.previewComponent.comment
    });
  }

  reject() {
    this.submit(ChangeRequestDecision.REJECT, {
      autoFocus: false,
      data: {
        title: this.translatePrefixDialogReject + 'title',
        description: this.translatePrefixDialogReject + 'description',
        buttonCta: this.translatePrefixDialogReject + 'buttonCta',
        buttonClose: this.translatePrefixDialogReject + 'buttonClose'
      }
    }, {
      rejectorComment: this.previewComponent.comment
    });
  }

  cancel() {
    this.submit(ChangeRequestDecision.CANCEL, {
      autoFocus: false,
      data: {
        title: this.translatePrefixDialogCancel + 'title',
        description: this.translatePrefixDialogCancel + 'description',
        buttonCta: this.translatePrefixDialogCancel + 'buttonCta',
        buttonClose: this.translatePrefixDialogCancel + 'buttonClose'
      }
    }, {
      cancellerComment: this.previewComponent.comment
    });
  }

  submit(decision: ChangeRequestDecision, dialogConfig: MatDialogConfig, params: ChangeRequestDecisionRequestBody): void {
    if (this.previewComponent.checkValidity()) {
      this.dialog.open(DialogComponent, {
        ...dialogConfig,
        width: '440px',
      }).afterClosed().subscribe((result: any) => {
        if (result === 'submit') {
          this.sendDecision(decision, params);
        }
      });
    }
  }

  sendDecision(decision: ChangeRequestDecision, body: ChangeRequestDecisionRequestBody) {
    this.pendingChangesService.changeRequestDecision(
        this.changeRequestDetails.id.toString(),
        decision,
        body
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

}
