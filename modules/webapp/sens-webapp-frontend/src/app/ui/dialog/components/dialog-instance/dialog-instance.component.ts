import { ChangeDetectionStrategy, Component, Input, TemplateRef, ViewChild } from '@angular/core';
import { MatDialogRef } from '@angular/material';
import { DialogButtonsTemplateType } from '@ui/dialog/model/dialog-config';
import { DialogService } from '@ui/dialog/services/dialog.service';

@Component({
  selector: 'app-dialog-instance',
  templateUrl: './dialog-instance.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class DialogInstanceComponent {

  @Input() public header: string;
  @Input() public description: string;

  @Input() public autoFocus: boolean = true;

  @Input() public buttonsContentType: DialogButtonsTemplateType = DialogButtonsTemplateType.CUSTOM;

  private dialogRef: MatDialogRef<any>;

  constructor(public dialogService: DialogService) {}

  @ViewChild('dialogTemplate', {static: true}) private dialogTemplate: TemplateRef<any>;

  public open(): void {
    this.dialogRef = this.dialogService.open({
      autoFocus: this.autoFocus,
      data: {
        header: this.header,
        description: this.description,
        buttonsTemplate: this.dialogTemplate,
        buttonsTemplateType: this.buttonsContentType
      }
    });
  }

  public close(): void {
    if (this.dialogRef) {
      this.dialogRef.close();
      this.dialogRef = null;
    }
  }

}
