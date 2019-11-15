import { Component, Input } from '@angular/core';
import { DynamicComponent } from '../../../../../components/dynamic-view/dynamic-view.component';
import { BranchChanges } from './branch-changes';

@Component({
  selector: 'app-branch-changes-view',
  templateUrl: './branch-changes-view.component.html',
  styleUrls: ['./branch-changes-view.component.scss']
})
export class BranchChangesViewComponent implements DynamicComponent {
  @Input() data: BranchChanges;
  changelogVisible = false;

  constructor() { }

  openChangelog() {
    this.changelogVisible = true;
  }

  closeChangelog() {
    this.changelogVisible = false;
  }

}
