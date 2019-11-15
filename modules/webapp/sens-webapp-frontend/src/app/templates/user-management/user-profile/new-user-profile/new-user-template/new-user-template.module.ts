import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { SharedModule } from '../../../../../shared/shared.module';
import { UserOptionsModule } from '../../options/user-options.module';
import { NewUserTemplateComponent } from './new-user-template.component';

@NgModule({
  imports: [
    SharedModule,
    BrowserModule,
    ReactiveFormsModule,
    UserOptionsModule
  ],
  declarations: [
    NewUserTemplateComponent
  ],
  exports: [
    NewUserTemplateComponent
  ]
})
export class NewUserTemplateModule {}
