import { Component, OnInit, ChangeDetectorRef, Output, EventEmitter } from '@angular/core';
import { FormGroup, FormControl, Validators, FormGroupDirective } from '@angular/forms';
import { AiSolutionsService } from '@core/ai-solutions/services/ai-solutions.service';
import { Solution } from '@endpoint/configuration/model/solution.enum';
import { Observable } from 'rxjs';

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

  aiSolutions: Observable<Solution[]> = this.aiSolutionsService.availableSolutions;

  constructor(private aiSolutionsService: AiSolutionsService) { }

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
    if (this.isWithDefaultValues() &&
        !this.form.controls.comment.value) {
      this.setErrorState(true, true, true);
    } else if (this.isWithDefaultValues()) {
      this.setErrorState(true, true, false);
    } else if (!this.form.controls.comment.value) {
      this.setErrorState(false, false, true);
    } else {
      this.formSubmitted.emit(this.form.value);
    }
  }

  selectionChange() {
    if (this.isWithDefaultValues()) {
      this.setErrorState(true, true, false);
    } else {
      this.form.controls.aiSolution.setErrors(null);
      this.form.controls.aiStatus.setErrors(null);
    }
  }

  previousStep() {
    this.previousStepClicked.emit();
  }

  setErrorState(aiSolution: boolean, aiStatus: boolean, comment: boolean) {
    if (aiSolution) {
      this.form.controls.aiSolution.setErrors({
        needsChange: true
      });
    }

    if (aiStatus) {
      this.form.controls.aiStatus.setErrors({
        needsChange: true
      });
    }

    if (comment) {
      this.form.controls.comment.setErrors({
        needsChange: true
      });
    }
  }

  isWithDefaultValues() {
    return this.form.controls.aiSolution.value === this.defaultAiValue &&
        this.form.controls.aiStatus.value === this.defaultAiValue;
  }
}
