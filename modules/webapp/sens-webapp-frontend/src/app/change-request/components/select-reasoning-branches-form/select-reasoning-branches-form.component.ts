import { Component, OnInit, ViewChild, Output, EventEmitter, Input } from '@angular/core';
import { MatButtonToggleGroup } from '@angular/material/button-toggle';
import { FormGroup, FormControl, Validators, FormGroupDirective } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { environment } from '@env/environment';

@Component({
  selector: 'app-select-reasoning-branches',
  templateUrl: './select-reasoning-branches-form.component.html'
})
export class SelectReasoningBranchesFormComponent implements OnInit {
  @Output() formSubmitted = new EventEmitter();
  @Output() formReset = new EventEmitter();
  @ViewChild('toggleGroup', {static: true}) toggleGroup: MatButtonToggleGroup;

  reasoningBranchIdsControl = new FormControl('', [Validators.required, Validators.maxLength(1000)]);
  reasoningBranchSignatureControl = new FormControl('', [Validators.required, Validators.maxLength(1000)]);

  form = new FormGroup({
    decisionTreeId: new FormControl('', [Validators.required]),
    reasoningBranchIds: this.reasoningBranchIdsControl
  });

  translatePrefix = 'changeRequest.select.';

  constructor(
      private activatedRoute: ActivatedRoute
  ) {
    this.activatedRoute.queryParams.subscribe(par => {
      this.loadReasoningBranchFromUrl(par['dt_id'], par['fv_ids']);
    });
  }

  ngOnInit(): void {
    this.registerToggleGroupListener();
    this.toggleGroup.writeValue('id');
  }

  loadReasoningBranchFromUrl(decisionTreeId, reasoningBranchId) {
    const dtId = parseInt(decisionTreeId, environment.decimal);
    const rbIds = reasoningBranchId.split(',').map(Number);

    if (!isNaN(dtId) && !rbIds.some(isNaN)) {
      this.form.controls.decisionTreeId.setValue(dtId);
      this.form.controls.reasoningBranchIds.setValue(
          rbIds.join('\n'));
    }
  }

  onFormSubmit(): void {
    if (this.form.valid) {
      this.formSubmitted.emit(this.form.value);
    } else {
      this.form.updateValueAndValidity();
    }
  }

  registerToggleGroupListener(): void {
    this.toggleGroup.registerOnChange(value => {
      if (value === 'id') {
        this.form.addControl('reasoningBranchIds', this.reasoningBranchIdsControl);
        this.form.removeControl('reasoningBranchSignature');
      } else {
        this.form.removeControl('reasoningBranchIds');
        this.form.addControl('reasoningBranchSignature', this.reasoningBranchSignatureControl);
      }
    });
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
    this.formReset.emit();
  }
}
