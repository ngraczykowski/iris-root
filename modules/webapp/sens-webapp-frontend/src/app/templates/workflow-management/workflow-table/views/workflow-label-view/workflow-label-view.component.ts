import { Component, Input, OnInit } from '@angular/core';
import { DynamicComponent } from '../../../../../components/dynamic-view/dynamic-view.component';

export interface WorkflowLabelViewData {
  text: string;
}

@Component({
  selector: 'app-workflow-label-view',
  templateUrl: './workflow-label-view.component.html',
  styleUrls: ['./workflow-label-view.component.scss']
})
export class WorkflowLabelViewComponent implements DynamicComponent, OnInit {

  @Input() data: WorkflowLabelViewData;

  constructor() { }

  ngOnInit() {
  }

}
