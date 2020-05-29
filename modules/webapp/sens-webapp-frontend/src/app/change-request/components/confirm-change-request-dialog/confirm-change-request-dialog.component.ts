import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-confirm-change-request-dialog',
  templateUrl: './confirm-change-request-dialog.component.html'
})
export class ConfirmChangeRequestDialogComponent {

  constructor(
      public dialogRef: MatDialogRef<ConfirmChangeRequestDialogComponent>,
      @Inject(MAT_DIALOG_DATA) public data) {}

  onCancelClick(): void {
    this.dialogRef.close('cancel');
  }

  onSubmitClick(): void {
    this.dialogRef.close('submit');
  }
}
