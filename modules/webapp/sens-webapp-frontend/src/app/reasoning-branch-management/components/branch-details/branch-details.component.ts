import {
  ChangeDetectorRef,
  Component,
  EventEmitter,
  Input,
  OnChanges,
  OnInit,
  Output
} from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ReasoningBranchDetails } from '@app/reasoning-branch-management/models/reasoning-branch-management';

@Component({
  selector: 'app-branch-details',
  templateUrl: './branch-details.component.html',
  styleUrls: ['./branch-details.component.scss']
})
export class BranchDetailsComponent implements OnInit, OnChanges {
  isOriginalValue = true;

  branchForm: FormGroup = new FormGroup({
    aiSolution: new FormControl(null, Validators.required),
    active: new FormControl(null, Validators.required)
  });

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
    this.reset();

    this.branchForm.valueChanges.subscribe(data => {
      this.isOriginalValue =
        this.checkIsFormValueOriginal(data, {aiSolution: this.branchDetails.aiSolution, active: this.branchDetails.active});
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
