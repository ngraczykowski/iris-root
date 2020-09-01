import { NgModule, Type } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  MatButtonModule,
  MatFormFieldModule,
  MatIconModule,
  MatTooltipModule
} from '@angular/material';
import { SnackbarModule } from '@ui/snackbar/snackbar.module';
import { FieldCopySuffixComponent } from './components/field-copy-suffix/field-copy-suffix.component';
import { FormFieldEmptyControlDirective } from './directives/form-field-empty-control.directive';

const publicDeclarations: Type<any>[] = [FieldCopySuffixComponent, FormFieldEmptyControlDirective];

@NgModule({
  declarations: [...publicDeclarations],
  exports: [...publicDeclarations],
  imports: [
    CommonModule,
    MatFormFieldModule,
    MatButtonModule,
    MatIconModule,
    MatTooltipModule,
    SnackbarModule
  ]
})
export class FormFieldModule { }
