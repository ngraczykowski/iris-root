import {
  Component,
  EventEmitter,
  Input,
  OnInit,
  Output
} from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ReasoningBranchDetails } from '@app/reasoning-branch-management/models/reasoning-branch-management';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-branch-details',
  templateUrl: './branch-details.component.html',
  styleUrls: ['./branch-details.component.scss']
})
export class BranchDetailsComponent implements OnInit {

  branchForm: FormGroup = new FormGroup({
    aiSolution: new FormControl(null, Validators.required),
    active: new FormControl(null, Validators.required)
  });

  @Input() fullId: string;
  @Input() branchDetails: ReasoningBranchDetails;
  @Output() confirm = new EventEmitter();

  constructor(
      private translate: TranslateService
  ) { }

  ngOnInit() {
  }

  convertSolution(solution) {
    const solutionTranslatePrefix = 'aiSolutions.';

    switch (solution) {
      case 'NO_DECISION': {
        return this.translate.instant(solutionTranslatePrefix + 'noDecision');
      }
      case 'FALSE_POSITIVE': {
        return this.translate.instant(solutionTranslatePrefix + 'falsePositive');
      }
      case 'POTENTIAL_TRUE_POSITIVE': {
        return this.translate.instant(solutionTranslatePrefix + 'potentialTruePositive');
      }
      default: {
        return solution;
      }
    }
  }
}
