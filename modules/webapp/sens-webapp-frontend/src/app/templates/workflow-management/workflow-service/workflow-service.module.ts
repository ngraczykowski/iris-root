import { NgModule } from '@angular/core';
import { SharedModule } from '../../../shared/shared.module';
import { WorkflowClient } from './workflow-client';
import { WorkflowService } from './workflow.service';

@NgModule({
  imports: [
    SharedModule
  ],
  providers: [
    WorkflowClient,
    WorkflowService
  ]
})
export class WorkflowServiceModule {}
