import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { UserDetails } from '@app/app-bar/models/user-details';

@Component({
  selector: 'app-user-menu',
  templateUrl: './user-menu.component.html',
  styleUrls: ['./user-menu.component.scss']
})
export class UserMenuComponent implements OnInit {
  @Input() userDetails: UserDetails;
  @Input() appVersion;
  @Output() logOutClick = new EventEmitter();

  constructor() { }

  ngOnInit() {
  }
}
