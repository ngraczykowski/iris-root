import { Component, EventEmitter, Input, OnInit, Output, OnChanges, ChangeDetectorRef } from '@angular/core';
import { ReasoningBranchDetails } from '@app/reasoning-branch-management/models/reasoning-branch-management';
import { FormGroup, FormControl, Validators } from '@angular/forms';

@Component({
  selector: 'app-branch-details',
  templateUrl: './branch-details.component.html',
  styleUrls: ['./branch-details.component.scss']
})
export class BranchDetailsComponent implements OnInit, OnChanges {
  isOriginalValue = true;
  branchForm: FormGroup;
  aiSolutions = [
    {
      label: 'No Decision',
      key: 'NO_DECISION'
    },
    {
      label: 'False Positive',
      key: 'FALSE_POSITIVE'
    },
    {
      label: 'Hinted False Positive',
      key: 'HINTED_FALSE_POSITIVE'
    },
    {
      label: 'Hinted Potential True Positive',
      key: 'HINTED_POTENTIAL_TRUE_POSITIVE'
    },
    {
      label: 'Potential True Positive',
      key: 'POTENTIAL_TRUE_POSITIVE'
    }
  ];

  @Input() fullId: string;
  @Input() branchDetails: ReasoningBranchDetails;
  @Output() confirm = new EventEmitter();

  constructor(
    private changeDetector: ChangeDetectorRef
  ) { }

  ngOnInit() {
    this.branchForm = new FormGroup({
      aiSolution: new FormControl(this.branchDetails.aiSolution, Validators.required),
      active: new FormControl(this.branchDetails.active, Validators.required)
    });

    this.branchForm.valueChanges.subscribe(data => {
      this.isOriginalValue =
        this.checkIsFormValueOriginal(data, {aiSolution: this.branchDetails.aiSolution, active: this.branchDetails.active});
    });
  }

  ngOnChanges() {
    this.changeDetector.detectChanges();
    this.reset();
  }

  reset() {
    this.branchForm.controls.aiSolution.setValue(this.branchDetails.aiSolution);
    this.branchForm.controls.active.setValue(this.branchDetails.active);
    this.branchForm.markAsPristine();
  }

  submit() {
    this.confirm.emit(this.branchForm.value);
  }

  private checkIsFormValueOriginal(currentData, originalData): boolean {
    return JSON.stringify(currentData) === JSON.stringify(originalData);
  }
}
