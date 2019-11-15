import { Component, Input, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { UserOptionBase } from '../user-option-base';

@Component({
  selector: 'app-user-password-option',
  templateUrl: './user-password-option.component.html',
  styleUrls: ['./user-password-option.component.scss']
})
export class UserPasswordOptionComponent extends UserOptionBase implements OnInit {

  @Input() passwordControl: FormControl;
  @Input() repeatPasswordControl: FormControl;

  ngOnInit() {
  }
}
