import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { InputModule } from '@app/components/input/input.module';
import { SharedModule } from '@app/shared/shared.module';
import { ChangeRequestRejectFormComponent } from '@app/templates/decision-tree/branch-change/branch-reject/change-request-reject-form/change-request-reject-form.component';
import { ChangeRequestRejectFormService } from '@app/templates/decision-tree/branch-change/branch-reject/change-request-reject-form/change-request-reject-form.service';
import { ChangeRequestCommentOptionComponent } from './change-request-comment-option/change-request-comment-option.component';

@NgModule({
  imports: [
    SharedModule,
    BrowserModule,
    ReactiveFormsModule,
    InputModule,
  ],
  providers: [
    ChangeRequestRejectFormService
  ],
  declarations: [
    ChangeRequestCommentOptionComponent,
    ChangeRequestRejectFormComponent
  ],
  exports: [
    ChangeRequestRejectFormComponent
  ]
})
export class ChangeRequestRejectFormModule {

}
