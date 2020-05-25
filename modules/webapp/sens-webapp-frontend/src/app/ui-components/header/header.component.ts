import { Component, Input, OnInit } from '@angular/core';
import { Header } from '@app/ui-components/header/header';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  @Input() headerData: Header;

  constructor() { }

  ngOnInit() {
  }

}
