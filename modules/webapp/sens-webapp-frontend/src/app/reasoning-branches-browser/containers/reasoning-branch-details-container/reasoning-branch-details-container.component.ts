import { Component, OnInit } from '@angular/core';
import { Header } from '@app/ui-components/header/header';

@Component({
  selector: 'app-reasoning-branch-details-container',
  templateUrl: './reasoning-branch-details-container.component.html',
  styleUrls: ['./reasoning-branch-details-container.component.scss']
})
export class ReasoningBranchDetailsContainerComponent implements OnInit {

  translatePrefix = 'reasoningBranchesBrowser.';
  labelsTranslatePrefix = this.translatePrefix + 'labels.';

  header: Header = {
    title: this.translatePrefix + 'details.title',
    parameter: '1-1'
  };

  constructor() { }

  ngOnInit() {
  }

}
