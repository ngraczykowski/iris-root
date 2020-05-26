import { Component, Inject} from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-change-request-decision-dialog',
  templateUrl: './change-request-decision-dialog.component.html'
})
export class ChangeRequestDecisionDialogComponent {

  constructor(
      public dialogRef: MatDialogRef<ChangeRequestDecisionDialogComponent>,
      @Inject(MAT_DIALOG_DATA) public data) {}

  onCancelClick(): void {
    this.dialogRef.close();
  }

  onSubmitClick(): void {
    this.dialogRef.close();
  }
}
