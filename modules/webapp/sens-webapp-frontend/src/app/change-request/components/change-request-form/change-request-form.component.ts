import { Component, OnInit, ChangeDetectorRef, Output, EventEmitter } from '@angular/core';
import { FormGroup, FormControl, Validators, FormGroupDirective } from '@angular/forms';

@Component({
  selector: 'app-change-request-form',
  templateUrl: './change-request-form.component.html'
})
export class ChangeRequestFormComponent implements OnInit {
  @Output() formSubmitted = new EventEmitter();
  @Output() previousStepClicked = new EventEmitter();

  defaultAiValue = 'Leave unchanged';
  _initialState = new FormGroup({
    aiSolution: new FormControl('Leave unchanged', [Validators.required]),
    aiStatus: new FormControl('Leave unchanged', [Validators.required]),
    comment: new FormControl('', [Validators.required])
  });
  form = this._initialState;
  translatePrefix = 'changeRequest.configureForm.';

  constructor() { }

  ngOnInit() {
  }

  resetForm(formDir: FormGroupDirective): void {
    formDir.resetForm({
      aiSolution: 'Leave unchanged',
      aiStatus: 'Leave unchanged',
      comment: ''
    });

    this.form.reset({
      aiSolution: 'Leave unchanged',
      aiStatus: 'Leave unchanged',
      comment: ''
    });
  }

  onFormSubmit(event) {
    event.preventDefault();
    if (
      this.form.controls.aiSolution.value === this.defaultAiValue &&
      this.form.controls.aiStatus.value === this.defaultAiValue ) {
        this.form.controls.aiSolution.setErrors({
          needsChange: true
        });
        this.form.controls.aiStatus.setErrors({
          needsChange: true
        });
    } else {
      this.formSubmitted.emit(this.form.value);
    }
  }

  selectionChange() {
    if (
      this.form.controls.aiSolution.value === this.defaultAiValue &&
      this.form.controls.aiStatus.value === this.defaultAiValue ) {
        this.form.controls.aiSolution.setErrors({
          needsChange: true
        });
        this.form.controls.aiStatus.setErrors({
          needsChange: true
        });
    } else {
      this.form.controls.aiSolution.setErrors(null);
      this.form.controls.aiStatus.setErrors(null);
    }
  }

  previousStep() {
    this.previousStepClicked.emit();
  }

}
