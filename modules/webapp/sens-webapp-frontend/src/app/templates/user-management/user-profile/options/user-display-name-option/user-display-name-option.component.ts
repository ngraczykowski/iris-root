import { Component, Input, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { UserOptionBase } from '../user-option-base';

@Component({
  selector: 'app-user-display-name-option',
  templateUrl: './user-display-name-option.component.html',
  styleUrls: ['./user-display-name-option.component.scss']
})
export class UserDisplayNameOptionComponent extends UserOptionBase implements OnInit {

  @Input() control: FormControl;

  ngOnInit() {
  }
}
