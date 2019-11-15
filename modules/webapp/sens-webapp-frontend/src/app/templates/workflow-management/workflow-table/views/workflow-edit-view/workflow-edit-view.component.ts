import { Component, Input, OnInit } from '@angular/core';
import { DynamicComponent } from '../../../../../components/dynamic-view/dynamic-view.component';
import { Workflow } from '../../../../model/workflow.model';
import { WorkflowService } from '../../../workflow-service/workflow.service';

export interface WorkflowEditViewData {
  workflow: Workflow;
}

@Component({
  selector: 'app-workflow-edit-view',
  templateUrl: './workflow-edit-view.component.html',
  styleUrls: ['./workflow-edit-view.component.scss']
})
export class WorkflowEditViewComponent implements DynamicComponent, OnInit {

  @Input() data: WorkflowEditViewData;

  constructor(private workflowService: WorkflowService) { }

  ngOnInit() {
  }

  onEdit() {
    this.workflowService.edit(this.data.workflow);
  }
}
