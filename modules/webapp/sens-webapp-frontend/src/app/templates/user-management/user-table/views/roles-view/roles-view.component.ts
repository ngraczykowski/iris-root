import { Component, Input, OnInit } from '@angular/core';
import { DynamicComponent } from '../../../../../components/dynamic-view/dynamic-view.component';
import { UserRole } from '../../../../model/user.model';
import { UserCellViewData } from '../user-view-factories';

export interface RolesViewData extends UserCellViewData {
  roles: UserRole[];
  superUser: boolean;
}

@Component({
  selector: 'app-roles-view',
  templateUrl: './roles-view.component.html',
  styleUrls: ['./roles-view.component.scss']
})
export class RolesViewComponent implements DynamicComponent, OnInit {

  @Input() data: RolesViewData;

  constructor() { }

  ngOnInit() {
  }

}
