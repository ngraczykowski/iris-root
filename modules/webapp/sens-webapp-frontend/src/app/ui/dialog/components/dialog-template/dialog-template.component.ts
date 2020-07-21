import {
  ChangeDetectionStrategy,
  Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';
import { DialogButtonsTemplateType, DialogConfigData } from '@ui/dialog/model/dialog-config';

@Component({
  selector: 'app-dialog-template',
  templateUrl: './dialog-template.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class DialogTemplateComponent {

  buttonsTemplateType: typeof DialogButtonsTemplateType = DialogButtonsTemplateType;

  constructor(
      @Inject(MAT_DIALOG_DATA) public data: DialogConfigData,
      private dialogRef: MatDialogRef<any>) {
  }

  close(): void {
    this.dialogRef.close();
  }

}
