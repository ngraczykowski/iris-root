import { ChangeDetectorRef, Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup, FormGroupDirective, Validators } from '@angular/forms';

@Component({
  selector: 'app-configure-reasoning-branches-report',
  templateUrl: './configure-reasoning-branches-report.component.html'
})
export class ConfigureReasoningBranchesReportComponent implements OnInit {

  @Output() formSubmit = new EventEmitter();

  translatePrefix = 'reasoningBranchesReport.configure.form.';
  decisionTreeIdTranslatePrefix = this.translatePrefix + 'decisionTreeId.';
  footerTranslatePrefix = this.translatePrefix + 'footer.';

  form = new FormGroup({
    decisionTreeId: new FormControl('', Validators.required)
  });
  private formDir: FormGroupDirective;

  constructor() { }

  ngOnInit() {
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
