import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { SharedModule } from '../../../../../shared/shared.module';
import { UserOptionsModule } from '../../options/user-options.module';
import { EditUserTemplateComponent } from './edit-user-template.component';

@NgModule({
  imports: [
    SharedModule,
    BrowserModule,
    ReactiveFormsModule,
    UserOptionsModule
  ],
  declarations: [
    EditUserTemplateComponent,
  ],
  exports: [
    EditUserTemplateComponent,
  ]
})
export class EditUserTemplateModule {}
