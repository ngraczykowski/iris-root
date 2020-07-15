import { Injectable,  } from '@angular/core';
import { MatDialog, MatDialogConfig, MatDialogRef } from '@angular/material';
import { DialogTemplateComponent } from '@ui/dialog/components/dialog-template/dialog-template.component';

@Injectable({
  providedIn: 'root'
})
export class DialogService {

  constructor(public matDialog: MatDialog) { }

  public get defaultConfig(): MatDialogConfig {
    return {
      autoFocus: true,
      width: '450px'
    };
  }

  public open<T>(config: MatDialogConfig = {}): MatDialogRef<any> {
    return this.matDialog.open(DialogTemplateComponent, {
      ...this.defaultConfig,
      ...config
    });
  }
}
