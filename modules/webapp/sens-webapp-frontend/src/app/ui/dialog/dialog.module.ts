import { NgModule, Type } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatDialogModule } from '@angular/material';
import { DialogInstanceComponent } from './components/dialog-instance/dialog-instance.component';
import { DialogTemplateComponent } from './components/dialog-template/dialog-template.component';
import { DialogButtonDirective } from './directives/dialog-button.directive';

const publicDeclarations: Type<any>[] = [DialogInstanceComponent, DialogButtonDirective];

@NgModule({
  declarations: [...publicDeclarations, DialogTemplateComponent],
  exports: [...publicDeclarations],
  imports: [
    CommonModule,
    MatDialogModule
  ],
  entryComponents: [DialogTemplateComponent]
})
export class DialogModule { }
