import { NgModule } from '@angular/core';
import { SharedModule } from '../../shared/shared.module';
import { WorkflowEditorModule } from './workflow-editor/workflow-editor.module';
import { WorkflowManagementComponent } from './workflow-management.component';
import { WorkflowTableModule } from './workflow-table/workflow-table.module';

@NgModule({
  imports: [
    SharedModule,
    WorkflowTableModule,
    WorkflowEditorModule
  ],
  providers: [],
  declarations: [
    WorkflowManagementComponent,
  ],
  exports: [
    WorkflowManagementComponent
  ]
})
export class WorkflowManagementModule {}
