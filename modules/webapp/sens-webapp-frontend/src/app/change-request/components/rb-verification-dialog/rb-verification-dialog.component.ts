import { Component, Inject} from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-rb-verification-dialog',
  templateUrl: './rb-verification-dialog.component.html',
  styleUrls: ['./rb-verification-dialog.component.scss']
})
export class RbVerificationDialogComponent {

  constructor(
      public dialogRef: MatDialogRef<RbVerificationDialogComponent>,
      @Inject(MAT_DIALOG_DATA) public data) {}

  onCloseClick(): void {
    this.dialogRef.close();
  }

}
