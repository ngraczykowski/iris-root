import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmChangeRequestDialogComponent } from '@app/change-request/components/confirm-change-request-dialog/confirm-change-request-dialog.component';
import { ChangeRequestService } from '@app/change-request/services/change-request.service';

@Component({
  selector: 'app-change-request-properties-container',
  templateUrl: './change-request-properties-container.component.html'
})
export class ChangeRequestPropertiesContainerComponent implements OnInit {
  @Output() goPreviousStep = new EventEmitter();
  @Output() createChangeRequestSuccess = new EventEmitter();

  decisionTreeId$ = this.changeRequestService.decisionTreeId$;
  reasoningBranchesCount$ = this.changeRequestService.reasoningBranchesCount$;

  translatePrefix = 'changeRequest.configureForm.';

  constructor(
    public dialog: MatDialog,
    private changeRequestService: ChangeRequestService
  ) { }

  ngOnInit() {}

  openDialog(formData): void {
    this.dialog.open(ConfirmChangeRequestDialogComponent, {
      width: '400px',
      data: {
        title: this.translatePrefix + 'confirmDialog.title',
        description: this.translatePrefix + 'confirmDialog.description',
        buttonCta: this.translatePrefix + 'confirmDialog.buttonCta',
        branchesCount: this.reasoningBranchesCount$.getValue(),
        buttonCancel: this.translatePrefix + 'confirmDialog.buttonCancel'
      }
    }).afterClosed().subscribe(result => {
      if (result === 'submit') {
        this.changeRequestService.setBulkChangeId();
        this.changeRequestService.setComment(formData.comment);
        this.changeRequestService.setAiSolution(formData.aiSolution);
        this.changeRequestService.setAiStatus(formData.aiStatus);
        this.changeRequestService.registerChangeRequest().
          subscribe(res => this.createChangeRequestSuccess.emit());
        this.changeRequestService.resetService();
      }
    });
  }
}
