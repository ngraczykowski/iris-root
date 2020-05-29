import { Component, EventEmitter, Inject, Output } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-change-request-decision-dialog',
  templateUrl: './change-request-decision-dialog.component.html'
})
export class ChangeRequestDecisionDialogComponent {

  @Output() decision = new EventEmitter();

  constructor(
      public dialogRef: MatDialogRef<ChangeRequestDecisionDialogComponent>,
      @Inject(MAT_DIALOG_DATA) public data) {}

  onSubmitClick() {
    this.decision.emit();
    this.dialogRef.close();
  }
}
