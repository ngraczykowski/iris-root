import { Component, Input, OnInit } from '@angular/core';
import { DynamicComponent } from '../../../../../components/dynamic-view/dynamic-view.component';

export interface WorkflowApprovalsViewData {
  approvalLevels: number;
}

@Component({
  selector: 'app-workflow-approvals-view',
  templateUrl: './workflow-approvals-view.component.html',
  styleUrls: ['./workflow-approvals-view.component.scss']
})
export class WorkflowApprovalsViewComponent implements DynamicComponent, OnInit {

  @Input() data: WorkflowApprovalsViewData;

  constructor() { }

  ngOnInit() {
  }

}
