import { Injectable } from '@angular/core';
import { FormArray, FormControl, FormGroup, Validators } from '@angular/forms';
import { ApprovalLevel, WorkflowChange, WorkflowDetails } from '@model/workflow.model';

class TextValueHelper {

  private static readonly JOIN_SEPARATOR = '\n';
  private static readonly SPLIT_REGEX = /[\s,]+/;

  static getStringArrayFromTextValue(value: string) {
    return value.split(TextValueHelper.SPLIT_REGEX).filter(v => v.length !== 0);
  }

  static getTextValueFromStringArray(values: string[]) {
    return values.join(TextValueHelper.JOIN_SEPARATOR);
  }
}

class FormControlFactory {

  static createApprovalLevelsFormArray(levels: ApprovalLevel[]): FormArray {
    return new FormArray(levels.map(l => this.createApprovalLevelFormControl(l.approvers)));
  }

  static createApprovalLevelFormControl(approvers: string[]) {
    return new FormControl(
        approvers,
        Validators.compose([Validators.required,
          Validators.pattern(/\w+/)])
    );
  }

  static createMakersFormControl(makers: string[]): FormControl {
    return new FormControl(makers);
  }

  static createEnableApprovalFormControl(levels: ApprovalLevel[]) {
    return new FormControl(levels.length > 0);
  }
}

@Injectable()
export class WorkflowEditFormService {

  decisionTreeId: number;
  form: FormGroup;

  init(workflow: WorkflowDetails) {
    this.decisionTreeId = workflow.decisionTreeId;

    const makersControl = FormControlFactory.createMakersFormControl(workflow.makers);
    const levelsControl = FormControlFactory.createApprovalLevelsFormArray(workflow.approvalLevels);
    const enabledControl = FormControlFactory.createEnableApprovalFormControl(workflow.approvalLevels);
    this.listenToEnabledControlChanges(enabledControl);
    this.form = new FormGroup({
      makers: makersControl,
      approvals: new FormGroup({
        enabled: enabledControl,
        levels: levelsControl
      })
    });
  }

  getMakersControl(): FormControl {
    return <FormControl>this.form.get('makers');
  }

  getEnableApprovalsControl(): FormControl {
    return <FormControl>this.form.get('approvals').get('enabled');
  }

  getApprovalLevelForm(): FormArray {
    return <FormArray>this.form.get('approvals').get('levels');
  }

  updateMakers(makersList) {
    (<FormControl>this.form.controls['makers']).setValue(makersList);
  }

  updateApprovals(newApprovalsList, approvalLevel) {
    this.getApprovalLevelForm().controls[approvalLevel].setValue(newApprovalsList);
  }

  addApprovalLevel() {
    const form = this.getApprovalLevelForm();
    form.push(FormControlFactory.createApprovalLevelFormControl([]));
    form.markAsDirty();
  }

  removeApprovalLevel(index) {
    const form = this.getApprovalLevelForm();
    if (form.length > index) {
      form.removeAt(index);
      form.markAsDirty();
    }
  }

  hasApprovalLevels() {
    return this.getApprovalLevelForm().length > 0;
  }

  areApprovalLevelsEnabled() {
    return !!this.getEnableApprovalsControl().value;
  }

  isInitialized() {
    return this.form;
  }

  isValid() {
    return !!this.form.valid;
  }

  isDirty() {
    return !!this.form.dirty;
  }

  createWorkflowChange(): WorkflowChange {
    return <WorkflowChange>{
      decisionTreeId: this.decisionTreeId,
      makers: this.readMakers(),
      approvalLevels: this.readApprovalLevels()
    };
  }

  private readMakers() {
    return this.getMakersControl().value;
  }

  private readApprovalLevels() {
    return this.getApprovalLevelForm().controls.map(c => <ApprovalLevel>{
      approvers: c.value
    });
  }

  private listenToEnabledControlChanges(enabledControl) {
    enabledControl.valueChanges
        .subscribe(value => {
          if (!value) {
            this.removeAllApprovalLevels();
          } else {
            this.addApprovalLevel();
          }
        });
  }

  private removeAllApprovalLevels() {
    const form = this.getApprovalLevelForm();
    for (let i = form.length - 1; i >= 0; i--) {
      form.removeAt(i);
    }
  }
}
