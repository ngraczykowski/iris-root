import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { SharedModule } from '@app/shared/shared.module';
import { InputTextWindowComponent } from './input-text-window/input-text-window.component';
import { InputValidationComponent } from './input-validation/input-validation.component';

@NgModule({
  declarations: [
    InputValidationComponent,
    InputTextWindowComponent
  ],
  exports: [
    InputTextWindowComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
  ]
})
export class InputModule {}
