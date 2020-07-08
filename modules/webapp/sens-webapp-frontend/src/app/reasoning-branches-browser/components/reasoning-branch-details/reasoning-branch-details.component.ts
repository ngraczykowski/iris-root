import { Component, Input, OnInit } from '@angular/core';
import { ReasoningBranchesList } from '@app/reasoning-branches-browser/model/branches-list';
import { environment } from '@env/environment';

@Component({
  selector: 'app-reasoning-branch-details',
  templateUrl: './reasoning-branch-details.component.html'
})
export class ReasoningBranchDetailsComponent implements OnInit {

  @Input() branchDetails: ReasoningBranchesList;
  @Input() rbId: string;

  translatePrefix = 'reasoningBranchesBrowser.';
  labelsTranslatePrefix = this.translatePrefix + 'labels.';

  dateFormatting = environment.dateFormatting;

  constructor() { }

  ngOnInit() {
  }

}
