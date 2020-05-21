import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmChangeRequestDialogComponent } from '@app/change-request/components/confirm-change-request-dialog/confirm-change-request-dialog.component';

@Component({
  selector: 'app-change-request-properties-container',
  templateUrl: './change-request-properties-container.component.html',
  styleUrls: ['./change-request-properties-container.component.scss']
})
export class ChangeRequestPropertiesContainerComponent implements OnInit {

  translatePrefix = 'changeRequest.configureForm.';

  constructor(public dialog: MatDialog) { }

  ngOnInit() {}

  openDialog(): void {
    const dialogRef = this.dialog.open(ConfirmChangeRequestDialogComponent, {
      width: '400px',
      data: {
        title: this.translatePrefix + 'confirmDialog.title',
        description: this.translatePrefix + 'confirmDialog.description',
        buttonCta: this.translatePrefix + 'confirmDialog.buttonCancel',
        buttonCancel: this.translatePrefix + 'confirmDialog.buttonCta'
      }
    });
  }
}
