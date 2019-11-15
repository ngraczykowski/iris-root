import { Component, Input, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { UserOptionBase } from '../user-option-base';

@Component({
  selector: 'app-user-status-option',
  templateUrl: './user-status-option.component.html',
  styleUrls: ['./user-status-option.component.scss']
})
export class UserStatusOptionComponent extends UserOptionBase implements OnInit {

  @Input() control: FormControl;

  ngOnInit() {
  }

}
