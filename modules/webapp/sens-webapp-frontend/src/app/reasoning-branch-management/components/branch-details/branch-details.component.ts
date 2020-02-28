import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { ReasoningBranchDetails } from '@app/reasoning-branch-management/models/reasoning-branch-management';
import { FormGroup, FormControl, Validators } from '@angular/forms';

@Component({
  selector: 'app-branch-details',
  templateUrl: './branch-details.component.html',
  styleUrls: ['./branch-details.component.scss']
})
export class BranchDetailsComponent implements OnInit {
  branchForm: FormGroup;
  aiSolutions = [
    'NO_DECISION',
    'FALSE_POSITIVE',
    'HINTED_FALSE_POSITIVE',
    'HINTED_POTENTIAL_TRUE_POSITIVE',
    'POTENTIAL_TRUE_POSITIVE'
  ];

  @Input() branchDetails: ReasoningBranchDetails;
  @Output() confirm = new EventEmitter();

  constructor() { }

  ngOnInit() {
    this.branchForm = new FormGroup({
      aiSolution: new FormControl(this.branchDetails.aiSolution, Validators.required),
      active: new FormControl(this.branchDetails.active, Validators.required)
    });
  }

  reset() {
    this.branchForm.controls.aiSolution.setValue(this.branchDetails.aiSolution);
    this.branchForm.controls.active.setValue(this.branchDetails.active);
    this.branchForm.markAsPristine();
  }

  submit() {
    this.confirm.emit(this.branchForm.value);
  }
}
