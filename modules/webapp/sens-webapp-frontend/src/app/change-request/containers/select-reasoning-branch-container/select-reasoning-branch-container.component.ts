import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { RbVerificationDialogComponent } from '@app/change-request/components/rb-verification-dialog/rb-verification-dialog.component';
import { StateContent } from '@app/ui-components/state/state';

@Component({
  selector: 'app-select-reasoning-branch-container',
  templateUrl: './select-reasoning-branch-container.component.html',
  styleUrls: ['./select-reasoning-branch-container.component.scss']
})
export class SelectReasoningBranchContainerComponent implements OnInit {

  translatePrefix = 'changeRequest.select.';

  verificationError = {
    margin: true,
    title: this.translatePrefix + 'verificationError.title',
    description: this.translatePrefix + 'verificationError.description',
    elements: [
      '5', '10', '15'
    ],
  };

  rbVerification: StateContent = {
    inProgress: true,
    image: null,
    title: this.translatePrefix + 'verifying.title',
    description: this.translatePrefix + 'verifying.description',
  };

  constructor(public dialog: MatDialog) { }

  ngOnInit() {
    this.openDialog();
  }

  openDialog(): void {
    const dialogRef = this.dialog.open(RbVerificationDialogComponent, {
      width: '450px',
      data: {
        title: this.translatePrefix + 'dialogVerificationError.title',
        description: this.translatePrefix + 'dialogVerificationError.description',
        cta: this.translatePrefix + 'dialogVerificationError.cta'
      }
    });
  }
}
