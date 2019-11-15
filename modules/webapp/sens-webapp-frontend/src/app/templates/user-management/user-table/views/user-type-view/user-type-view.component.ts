import { Component, Input, OnInit } from '@angular/core';
import { DynamicComponent } from '../../../../../components/dynamic-view/dynamic-view.component';
import { UserType } from '../../../../model/user.model';
import { UserCellViewData } from '../user-view-factories';

export interface UserTypeViewData extends UserCellViewData {
  userType: UserType;
}

@Component({
  selector: 'app-user-type-view',
  templateUrl: './user-type-view.component.html',
  styleUrls: ['./user-type-view.component.scss']
})
export class UserTypeViewComponent implements DynamicComponent, OnInit {

  @Input() data: UserTypeViewData;

  constructor() { }

  ngOnInit() {
  }

}
