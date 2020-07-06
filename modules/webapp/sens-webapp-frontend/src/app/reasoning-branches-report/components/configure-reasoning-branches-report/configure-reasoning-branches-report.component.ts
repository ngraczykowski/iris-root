import { Component, EventEmitter, Output } from '@angular/core';
import {
  FormControl,
  FormGroup,
  FormGroupDirective,
  ValidationErrors,
  Validators
} from '@angular/forms';
import { DECISION_TREE_EXISTS_VALIDATOR_ERROR } from '@core/decision-trees/validators/decision-tree-exists.validator';

@Component({
  selector: 'app-configure-reasoning-branches-report',
  templateUrl: './configure-reasoning-branches-report.component.html'
})
export class ConfigureReasoningBranchesReportComponent {

  @Output() formSubmit = new EventEmitter();

  decisionTreeExistsErrorCode: string = DECISION_TREE_EXISTS_VALIDATOR_ERROR;

  translatePrefix = 'reasoningBranchesReport.configure.form.';
  decisionTreeIdTranslatePrefix = this.translatePrefix + 'decisionTreeId.';
  footerTranslatePrefix = this.translatePrefix + 'footer.';

  form = new FormGroup({
    decisionTreeId: new FormControl('', Validators.required)
  });
  private formDir: FormGroupDirective;

  constructor() { }

  public setErrors(errors: ValidationErrors): void {
    if (errors[DECISION_TREE_EXISTS_VALIDATOR_ERROR]) {
      this.form.get('decisionTreeId').setErrors(errors);
    }
  }

  resetForm(formDir: FormGroupDirective): void {
    formDir.resetForm();
    this.form.reset();
    this.form.markAsPristine();
    Object.keys(this.form.controls).forEach(key => {
      this.form.get(key).setErrors(null);
      this.form.get(key).setValue('');
    });
    this.form.setErrors(null);
    this.form.markAsPristine();
    this.form.markAsUntouched();
  }

  onFormSubmit(): void {
    if (this.form.valid) {
      this.formSubmit.emit(this.form.value.decisionTreeId);
    } else {
      this.form.updateValueAndValidity();
    }
  }

  invalidDecisionTreeID() {
    this.form.controls.decisionTreeId.setErrors({'wrongId': true});
    this.form.controls.decisionTreeId.markAsTouched();
  }
}
