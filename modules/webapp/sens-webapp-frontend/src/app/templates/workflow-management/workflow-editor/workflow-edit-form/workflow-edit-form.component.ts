import { Component, Input, OnInit } from '@angular/core';
import { WorkflowDetails } from '@model/workflow.model';
import { WorkflowEditFormService } from './workflow-edit-form.service';

@Component({
  selector: 'app-workflow-edit-form',
  templateUrl: './workflow-edit-form.component.html',
  styleUrls: ['./workflow-edit-form.component.scss']
})
export class WorkflowEditFormComponent implements OnInit {

  @Input()
  set workflow(workflow: WorkflowDetails) {
    this.formService.init(workflow);
  }

  constructor(private formService: WorkflowEditFormService) { }

  ngOnInit() {
  }

  isInitialized() {
    return this.formService.isInitialized();
  }
}
