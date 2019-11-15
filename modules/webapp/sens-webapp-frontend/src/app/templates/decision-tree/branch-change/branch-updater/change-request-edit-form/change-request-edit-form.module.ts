import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { InputModule } from '@app/components/input/input.module';
import { SharedModule } from '@app/shared/shared.module';
import { ChangeRequestEditFormComponent } from '@app/templates/decision-tree/branch-change/branch-updater/change-request-edit-form/change-request-edit-form.component';
import { ChangeRequestEditFormService } from '@app/templates/decision-tree/branch-change/branch-updater/change-request-edit-form/change-request-edit-form.service';
import { ChangeRequestCommentOptionComponent } from './change-request-comment-option/change-request-comment-option.component';
import { ChangeRequestSolutionOptionComponent } from './change-request-solution-option/change-request-solution-option.component';
import { ChangeRequestStatusOptionComponent } from './change-request-status-option/change-request-status-option.component';

@NgModule({
  imports: [
    SharedModule,
    BrowserModule,
    ReactiveFormsModule,
    InputModule,
  ],
  providers: [
    ChangeRequestEditFormService
  ],
  declarations: [
    ChangeRequestSolutionOptionComponent,
    ChangeRequestStatusOptionComponent,
    ChangeRequestCommentOptionComponent,
    ChangeRequestEditFormComponent
  ],
  exports: [
    ChangeRequestEditFormComponent
  ]
})
export class ChangeRequestEditFormModule {

}
