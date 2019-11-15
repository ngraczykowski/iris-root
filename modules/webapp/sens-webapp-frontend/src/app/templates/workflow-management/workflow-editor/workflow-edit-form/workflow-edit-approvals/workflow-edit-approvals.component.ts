import { Component, OnInit } from '@angular/core';
import { FormArray, FormControl } from '@angular/forms';
import { WorkflowEditFormService } from '../workflow-edit-form.service';

@Component({
  selector: 'app-workflow-edit-approvals',
  templateUrl: './workflow-edit-approvals.component.html',
  styleUrls: ['./workflow-edit-approvals.component.scss']
})
export class WorkflowEditApprovalsComponent implements OnInit {

  get enableApprovalsControl(): FormControl {
    return this.formService.getEnableApprovalsControl();
  }

  get approvalLevelForm(): FormArray {
    return this.formService.getApprovalLevelForm();
  }

  constructor(private formService: WorkflowEditFormService) { }

  ngOnInit() {
  }

  uploadApprovalsList(newApprovalsList, approvalLevel) {
    this.formService.updateApprovals(newApprovalsList, approvalLevel);
  }

  areApprovalLevelsEnabled() {
    return this.formService.areApprovalLevelsEnabled();
  }

  shouldHideRemoveButton() {
    return !this.formService.hasApprovalLevels();
  }

  onRemove(index) {
    this.formService.removeApprovalLevel(index);
  }

  onAdd() {
    this.formService.addApprovalLevel();
  }
}
