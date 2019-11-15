import { Component, Input, OnInit } from '@angular/core';
import { DynamicComponent } from '../../../../../components/dynamic-view/dynamic-view.component';
import { UserCellViewData } from '../user-view-factories';

export class StatusViewData implements UserCellViewData {
  active: boolean;
}

@Component({
  selector: 'app-status-view',
  templateUrl: './status-view.component.html',
  styleUrls: ['./status-view.component.scss']
})
export class StatusViewComponent implements DynamicComponent, OnInit {

  @Input() data: StatusViewData;

  constructor() { }

  ngOnInit() {
  }

}
