import { Component, Input, OnInit } from '@angular/core';
import { DynamicComponent } from '../../../../../components/dynamic-view/dynamic-view.component';
import { UserCellViewData } from '../user-view-factories';

export interface DisplayNameViewData extends UserCellViewData {
  displayName: string;
  filterQuery: string;
}

@Component({
  selector: 'app-text-view',
  templateUrl: './display-name-view.component.html',
  styleUrls: ['./display-name-view.component.scss']
})
export class DisplayNameViewComponent implements DynamicComponent, OnInit {

  @Input() data: DisplayNameViewData;

  constructor() { }

  ngOnInit() {
  }

}
