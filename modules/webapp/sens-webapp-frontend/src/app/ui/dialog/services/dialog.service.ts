import { Injectable, } from '@angular/core';
import { MatDialog, MatDialogConfig, MatDialogRef } from '@angular/material';
import { DialogTemplateComponent } from '@ui/dialog/components/dialog-template/dialog-template.component';
import { DialogButtonsTemplateType, DialogConfigData } from '@ui/dialog/model/dialog-config';

@Injectable({
  providedIn: 'root'
})
export class DialogService {

  constructor(public matDialog: MatDialog) { }

  public get defaultConfig(): MatDialogConfig<Partial<DialogConfigData>> {
    return {
      autoFocus: true,
      width: '450px',
      data: {
        buttonsTemplateType: DialogButtonsTemplateType.CONFIRMATION
      }
    };
  }

  public open(config: MatDialogConfig<DialogConfigData> = {}): MatDialogRef<DialogTemplateComponent> {
    const configData: Partial<DialogConfigData> = {
      ...this.defaultConfig.data,
      ...(config.data || {}),

    };
    return this.matDialog.open(DialogTemplateComponent, {
      ...this.defaultConfig,
      ...config,
      data: configData
    });
  }
}
