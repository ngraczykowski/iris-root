import { Component, Input, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { UserOptionBase } from '../user-option-base';

@Component({
  selector: 'app-user-username-option',
  templateUrl: './user-username-option.component.html',
  styleUrls: ['./user-username-option.component.scss']
})
export class UserUsernameOptionComponent extends UserOptionBase implements OnInit {

  @Input() control: FormControl;

  ngOnInit() {
  }

}
