import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-navigation-element',
  templateUrl: './navigation-element.component.html',
  styleUrls: ['./navigation-element.component.scss']
})
export class NavigationElementComponent implements OnInit {

  constructor() { }
  @Input() navElement;

  ngOnInit() {
  }

}
