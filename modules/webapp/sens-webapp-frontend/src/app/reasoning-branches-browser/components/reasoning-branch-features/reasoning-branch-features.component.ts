import { ChangeDetectionStrategy, Component, Input } from '@angular/core';

@Component({
  selector: 'app-reasoning-branch-features',
  templateUrl: './reasoning-branch-features.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ReasoningBranchFeaturesComponent {

  @Input() featuresList;

  constructor() { }

}
