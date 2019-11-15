import {
  Label,
  Row,
  TableData
} from '../../../components/dynamic-view-table/dynamic-view-table.component';
import { View } from '../../../components/dynamic-view/dynamic-view.component';
import { Workflow } from '../../model/workflow.model';
import {
  WorkflowApprovalsViewComponent,
  WorkflowApprovalsViewData
} from './views/workflow-approvals-view/workflow-approvals-view.component';
import {
  WorkflowDecisionTreeIdViewComponent,
  WorkflowDecisionTreeIdViewData
} from './views/workflow-decision-tree-id-view/workflow-decision-tree-id-view.component';
import {
  WorkflowDecisionTreeNameViewComponent,
  WorkflowDecisionTreeNameViewData
} from './views/workflow-decision-tree-name-view/workflow-decision-tree-name-view.component';
import {
  WorkflowEditViewComponent,
  WorkflowEditViewData
} from './views/workflow-edit-view/workflow-edit-view.component';
import {
  WorkflowLabelViewComponent,
  WorkflowLabelViewData
} from './views/workflow-label-view/workflow-label-view.component';
import {
  WorkflowMakersViewComponent,
  WorkflowMakersViewData
} from './views/workflow-makers-view/workflow-makers-view.component';

export class WorkflowTableDataFactory {

  static create(workflows: Workflow[]): TableData {
    return <TableData>{
      total: workflows.length,
      labels: this.getLabelViews().map(v => <Label> {view: v}),
      rows: workflows
          .map(w => this.getRowViews(w))
          .map(views => <Row>{views: views})
    };
  }

  private static getLabelViews(): View[] {
    return [
      {
        component: WorkflowLabelViewComponent,
        data: <WorkflowLabelViewData> {text: 'workflowManagement.table.labels.decisionTreeId'}
      },
      {
        component: WorkflowLabelViewComponent,
        data: <WorkflowLabelViewData> {text: 'workflowManagement.table.labels.decisionTreeName'}
      },
      {
        component: WorkflowLabelViewComponent,
        data: <WorkflowLabelViewData> {text: 'workflowManagement.table.labels.makers'}
      },
      {
        component: WorkflowLabelViewComponent,
        data: <WorkflowLabelViewData> {text: 'workflowManagement.table.labels.approvals'}
      },
      {
        component: WorkflowLabelViewComponent,
        data: <WorkflowLabelViewData> {text: 'workflowManagement.table.labels.edit'}
      }
    ];
  }

  private static getRowViews(workflow: Workflow): View[] {
    return [
      {
        component: WorkflowDecisionTreeIdViewComponent,
        data: <WorkflowDecisionTreeIdViewData> {decisionTreeId: workflow.decisionTreeId}
      },
      {
        component: WorkflowDecisionTreeNameViewComponent,
        data: <WorkflowDecisionTreeNameViewData> {
          decisionTreeId: workflow.decisionTreeId,
          decisionTreeName: workflow.decisionTreeName
        }
      },
      {
        component: WorkflowMakersViewComponent,
        data: <WorkflowMakersViewData> {makers: workflow.makers}
      },
      {
        component: WorkflowApprovalsViewComponent,
        data: <WorkflowApprovalsViewData> {approvalLevels: workflow.approvalLevels}
      },
      {
        component: WorkflowEditViewComponent,
        data: <WorkflowEditViewData> {workflow: workflow}
      }
    ];
  }
}
