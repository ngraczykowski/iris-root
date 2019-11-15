import { Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { DynamicComponent } from '../../../../../components/dynamic-view/dynamic-view.component';
import { InboxMessage } from '../../../../model/inbox.model';
import { DisabledBranchDescriptionMapper } from './disabled-branch-description-mapper';

export interface DisabledBranchDescription {
  messageKey: string;
  branchLink: AlertLink;
  aiDecision: string;
  alertInfos: AlertInfo[];
}

export interface AlertInfo {
  analystDecision: string;
  link: AlertLink;
}

export interface AlertLink {
  name: string;
  url: string;
}

@Component({
  selector: 'app-disabled-branch-description-view',
  templateUrl: './disabled-branch-description-view.component.html',
  styleUrls: ['./disabled-branch-description-view.component.scss'],
  providers: [DisabledBranchDescriptionMapper]
})
export class DisabledBranchDescriptionViewComponent implements DynamicComponent, OnInit {

  showEverything: number;
  description: DisabledBranchDescription;

  @ViewChild('messageContent', {static: true}) elementView: ElementRef;

  showMore() {
    this.showEverything = this.elementView.nativeElement.offsetHeight;
  }

  hideMore() {
    this.showEverything = null;
  }


  @Input()
  set data(data: InboxMessage) {
    this.description = this.mapper.map(data);
  }

  constructor(private mapper: DisabledBranchDescriptionMapper) { }

  ngOnInit() {
  }
}
