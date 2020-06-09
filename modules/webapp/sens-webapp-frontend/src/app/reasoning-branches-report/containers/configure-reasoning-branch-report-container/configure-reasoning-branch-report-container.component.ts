import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { DialogComponent } from '@app/ui-components/dialog/dialog.component';

@Component({
  selector: 'app-configure-reasoning-branch-report-container',
  templateUrl: './configure-reasoning-branch-report-container.component.html'
})
export class ConfigureReasoningBranchReportContainerComponent implements OnInit {

  translatePrefix = 'reasoningBranchesReport.configure.';
  verificationFailedDialogTranslatePrefix = this.translatePrefix + 'verificationFailedDialog.';

  constructor(
      public dialog: MatDialog,
  ) { }

  ngOnInit() {
    this.openDialog();
  }

  openDialog(): void {
    this.dialog.open(DialogComponent, {
      width: '450px',
      data: {
        title: this.verificationFailedDialogTranslatePrefix + 'title',
        description: this.verificationFailedDialogTranslatePrefix + 'description',
        buttonClose: this.verificationFailedDialogTranslatePrefix + 'buttonClose'
      }
    });
  }
}
