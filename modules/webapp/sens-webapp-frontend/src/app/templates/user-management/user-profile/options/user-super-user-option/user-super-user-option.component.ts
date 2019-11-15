import { Component, Input, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { UserOptionBase } from '../user-option-base';

@Component({
  selector: 'app-user-super-user-option',
  templateUrl: './user-super-user-option.component.html',
  styleUrls: ['./user-super-user-option.component.scss']
})
export class UserSuperUserOptionComponent extends UserOptionBase implements OnInit {

  @Input() control: FormControl;

  ngOnInit() {
  }
}
