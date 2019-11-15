import { Injectable } from '@angular/core';
import { AbstractControl, FormControl, FormGroup, Validators } from '@angular/forms';
import { ChangeRequestValidators } from '@app/templates/decision-tree/branch-change/branch-updater/change-request-edit-form/change-request-validators';
import { StatusChange } from '@app/templates/decision-tree/branch-change/branch-updater/change-request.model';


@Injectable()
export class ChangeRequestEditFormService {

  private form: FormGroup;

  getAvailableStatusList(): StatusChange[] {
    return Object.keys(StatusChange).map(k => <StatusChange> k);
  }

  getSolutionControl(): AbstractControl {
    return this.form.get('solution');
  }

  getStatusControl(): AbstractControl {
    return this.form.get('status');
  }

  getCommentControl(): AbstractControl {
    return this.form.get('comment');
  }

  getSolutionChange() {
    return this.getSolutionControl().value;
  }

  getStatusChange(): StatusChange {
    return <StatusChange> this.getStatusControl().value;
  }

  getComment(): string {
    return <string> this.getCommentControl().value;
  }

  isInitialized() {
    return !!this.form;
  }

  isValid() {
    return this.form.valid;
  }

  isDirty() {
    return this.form.dirty;
  }

  init() {
    this.initForm();
  }

  reset() {
    this.initForm();
  }

  getFormErrorKeys(): string[] {
    if (this.form.dirty && this.form.errors) {
      return Object.keys(this.form.errors).filter(k => !!this.form.errors[k]);
    }
    return [];
  }

  private initForm() {
    this.form = new FormGroup({
      solution: new FormControl('NONE'),
      status: new FormControl(StatusChange.NONE),
      comment: new FormControl('', Validators.required)
    }, ChangeRequestValidators.atLeastOneChangeValidator());
  }
}
