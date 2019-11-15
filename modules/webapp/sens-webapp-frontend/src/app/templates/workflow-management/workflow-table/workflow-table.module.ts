import { NgModule } from '@angular/core';
import { SharedModule } from '../../../shared/shared.module';
import { WorkflowServiceModule } from '../workflow-service/workflow-service.module';
import { WorkflowViewModule } from './views/workflow-view.module';
import { WorkflowTableComponent } from './workflow-table.component';

@NgModule({
  imports: [
    SharedModule,
    WorkflowViewModule,
    WorkflowServiceModule
  ],
  declarations: [
    WorkflowTableComponent
  ],
  exports: [
    WorkflowTableComponent
  ]
})
export class WorkflowTableModule {}
