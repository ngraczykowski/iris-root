import { Component } from '@angular/core';
import { FormControl } from '@angular/forms';
import { WorkflowEditFormService } from '../workflow-edit-form.service';

@Component({
  selector: 'app-workflow-edit-makers',
  templateUrl: './workflow-edit-makers.component.html',
  styleUrls: ['./workflow-edit-makers.component.scss']
})
export class WorkflowEditMakersComponent {

  get control(): FormControl {
    return this.formService.getMakersControl();
  }

  constructor(
      private formService: WorkflowEditFormService,
  ) { }

  updateMakersList(newMakersList) {
    this.formService.updateMakers(newMakersList);
  }

}
