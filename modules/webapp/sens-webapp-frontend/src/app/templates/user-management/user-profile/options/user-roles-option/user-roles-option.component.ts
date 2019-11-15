import { Component, Input, OnInit } from '@angular/core';
import { FormArray } from '@angular/forms';
import { UserRole } from '../../../../model/user.model';
import { UserOptionBase } from '../user-option-base';


@Component({
  selector: 'app-user-roles-option',
  templateUrl: './user-roles-option.component.html',
  styleUrls: ['./user-roles-option.component.scss']
})
export class UserRolesOptionComponent extends UserOptionBase implements OnInit {

  @Input() control: FormArray;
  readonly availableRoles = Object.keys(UserRole);

  ngOnInit() {
  }
}
