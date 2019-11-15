import { NgModule } from '@angular/core';
import { SharedModule } from '../../../shared/shared.module';
import { WorkflowServiceModule } from '../workflow-service/workflow-service.module';
import { WorkflowEditFormModule } from './workflow-edit-form/workflow-edit-form.module';
import { WorkflowEditorComponent } from './workflow-editor.component';
import { WorkflowInfoComponent } from './workflow-info/workflow-info.component';

@NgModule({
  imports: [
    SharedModule,
    WorkflowServiceModule,
    WorkflowEditFormModule
  ],
  declarations: [
    WorkflowEditorComponent,
    WorkflowInfoComponent,
  ],
  exports: [
    WorkflowEditorComponent
  ]
})
export class WorkflowEditorModule {}
