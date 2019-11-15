import { Injectable } from '@angular/core';
import { AbstractControl, FormControl, FormGroup, Validators } from '@angular/forms';

@Injectable()
export class ChangeRequestRejectFormService {

  private form: FormGroup;

  getCommentControl(): AbstractControl {
    return this.form.get('comment');
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
      comment: new FormControl('', Validators.required)
    }, null);
  }
}
