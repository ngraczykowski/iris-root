import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup, FormGroupDirective, Validators } from '@angular/forms';

@Component({
  selector: 'app-configure-reasoning-branches-report',
  templateUrl: './configure-reasoning-branches-report.component.html'
})
export class ConfigureReasoningBranchesReportComponent implements OnInit {

  @Output() formSubmitted = new EventEmitter();

  translatePrefix = 'reasoningBranchesReport.configure.form.';
  decisionTreeIDTranslatePrefix = this.translatePrefix + 'decisionTreeId.';
  footerTranslatePrefix = this.translatePrefix + 'footer.';

  form = new FormGroup({
    decisionTreeId: new FormControl('', Validators.required)
  });

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
  }

  onFormSubmit(): void {
    if (this.form.valid) {
      this.formSubmitted.emit(this.form.value);
    } else {
      this.form.updateValueAndValidity();
    }
  }
}
