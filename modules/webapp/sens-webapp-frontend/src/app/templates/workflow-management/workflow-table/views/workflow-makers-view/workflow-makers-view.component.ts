import { Component, Input, OnInit } from '@angular/core';
import { DynamicComponent } from '@app/components/dynamic-view/dynamic-view.component';

export interface WorkflowMakersViewData {
  makers: string[];
}

@Component({
  selector: 'app-workflow-makers-view',
  templateUrl: './workflow-makers-view.component.html',
  styleUrls: ['./workflow-makers-view.component.scss']
})
export class WorkflowMakersViewComponent implements DynamicComponent, OnInit {

  @Input() data: WorkflowMakersViewData;

  constructor() { }

  ngOnInit() {
  }
}
