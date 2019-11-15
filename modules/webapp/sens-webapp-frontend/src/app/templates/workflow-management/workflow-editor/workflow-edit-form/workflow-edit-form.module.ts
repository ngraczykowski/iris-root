import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { SharedModule } from '@app/shared/shared.module';
import { WorkflowEditApprovalsComponent } from './workflow-edit-approvals/workflow-edit-approvals.component';
import { WorkflowEditFormComponent } from './workflow-edit-form.component';
import { WorkflowEditFormService } from './workflow-edit-form.service';
import { WorkflowEditMakersComponent } from './workflow-edit-makers/workflow-edit-makers.component';
import { WorkflowEditUsersManagementComponent } from './workflow-edit-users-management/workflow-edit-users-management.component';

@NgModule({
  imports: [
    SharedModule,
    BrowserModule,
    ReactiveFormsModule
  ],
  providers: [
    WorkflowEditFormService
  ],
  declarations: [
    WorkflowEditApprovalsComponent,
    WorkflowEditFormComponent,
    WorkflowEditUsersManagementComponent,
    WorkflowEditMakersComponent
  ],
  exports: [
    WorkflowEditFormComponent
  ]
})
export class WorkflowEditFormModule {}
