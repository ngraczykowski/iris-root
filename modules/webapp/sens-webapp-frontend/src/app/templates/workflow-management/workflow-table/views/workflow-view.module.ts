import { NgModule } from '@angular/core';
import { SharedModule } from '../../../../shared/shared.module';
import { WorkflowServiceModule } from '../../workflow-service/workflow-service.module';
import { WorkflowApprovalsViewComponent } from './workflow-approvals-view/workflow-approvals-view.component';
import { WorkflowDecisionTreeIdViewComponent } from './workflow-decision-tree-id-view/workflow-decision-tree-id-view.component';
import { WorkflowDecisionTreeNameViewComponent } from './workflow-decision-tree-name-view/workflow-decision-tree-name-view.component';
import { WorkflowEditViewComponent } from './workflow-edit-view/workflow-edit-view.component';
import { WorkflowLabelViewComponent } from './workflow-label-view/workflow-label-view.component';
import { WorkflowMakersViewComponent } from './workflow-makers-view/workflow-makers-view.component';

@NgModule({
  imports: [
    SharedModule,
    WorkflowServiceModule,
  ],
  declarations: [
    WorkflowMakersViewComponent,
    WorkflowApprovalsViewComponent,
    WorkflowEditViewComponent,
    WorkflowDecisionTreeNameViewComponent,
    WorkflowDecisionTreeIdViewComponent,
    WorkflowLabelViewComponent
  ],
  entryComponents: [
    WorkflowMakersViewComponent,
    WorkflowApprovalsViewComponent,
    WorkflowEditViewComponent,
    WorkflowDecisionTreeNameViewComponent,
    WorkflowDecisionTreeIdViewComponent,
    WorkflowLabelViewComponent
  ]
})
export class WorkflowViewModule {}
