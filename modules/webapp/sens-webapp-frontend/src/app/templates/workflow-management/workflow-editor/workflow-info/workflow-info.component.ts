import { Component, Input, OnInit } from '@angular/core';
import { WorkflowDetails } from '../../../model/workflow.model';

@Component({
  selector: 'app-workflow-info',
  templateUrl: './workflow-info.component.html',
  styleUrls: ['./workflow-info.component.scss']
})
export class WorkflowInfoComponent implements OnInit {

  @Input() workflow: WorkflowDetails;

  constructor() { }

  ngOnInit() {
  }

}
