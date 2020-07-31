import { TemplateRef } from '@angular/core';

export interface DialogConfigData {
  header: string;
  description?: string;
  contentRef?: TemplateRef<any>;
  buttonsTemplate?: TemplateRef<any>;
  buttonsTemplateType?: DialogButtonsTemplateType;
}

export enum DialogButtonsTemplateType {
  CUSTOM = 'CUSTOM', CONFIRMATION = 'CONFIRMATION'
}
