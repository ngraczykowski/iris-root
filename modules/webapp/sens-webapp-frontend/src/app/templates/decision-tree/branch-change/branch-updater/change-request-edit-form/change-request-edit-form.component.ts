import { Component, OnInit } from '@angular/core';
import { AbstractControl } from '@angular/forms';
import { ErrorData } from '@app/components/error-feedback/error-feedback.component';
import { ChangeRequestEditFormService } from '@app/templates/decision-tree/branch-change/branch-updater/change-request-edit-form/change-request-edit-form.service';
import { StatusChange } from '@app/templates/decision-tree/branch-change/branch-updater/change-request.model';

@Component({
  selector: 'app-change-request-edit-form',
  templateUrl: './change-request-edit-form.component.html',
  styleUrls: ['./change-request-edit-form.component.scss']
})
export class ChangeRequestEditFormComponent implements OnInit {

  get availableStatusOptions(): StatusChange[] {
    return this.formService.getAvailableStatusList();
  }

  get statusControl(): AbstractControl {
    return this.formService.getStatusControl();
  }

  get solutionControl(): AbstractControl {
    return this.formService.getSolutionControl();
  }

  get commentControl(): AbstractControl {
    return this.formService.getCommentControl();
  }

  get errors(): ErrorData[] {
    return this.formService.getFormErrorKeys().map(k => <ErrorData> {key: k});
  }

  constructor(private formService: ChangeRequestEditFormService) { }

  ngOnInit() {
    this.formService.init();
  }

  isInitialized() {
    return this.formService.isInitialized();
  }
}
