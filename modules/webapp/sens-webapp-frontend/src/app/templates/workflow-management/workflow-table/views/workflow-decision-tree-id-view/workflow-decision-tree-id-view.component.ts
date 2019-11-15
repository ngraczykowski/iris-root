import { Component, Input, OnInit } from '@angular/core';
import { DynamicComponent } from '../../../../../components/dynamic-view/dynamic-view.component';

export interface WorkflowDecisionTreeIdViewData {
  decisionTreeId: number;
}

@Component({
  selector: 'app-workflow-decision-tree-id-view',
  templateUrl: './workflow-decision-tree-id-view.component.html',
  styleUrls: ['./workflow-decision-tree-id-view.component.scss']
})
export class WorkflowDecisionTreeIdViewComponent implements DynamicComponent, OnInit {

  @Input() data: WorkflowDecisionTreeIdViewData;

  constructor() { }

  ngOnInit() {
  }

}
