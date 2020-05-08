import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-navigation-section',
  templateUrl: './navigation-section.component.html',
  styleUrls: ['./navigation-section.component.scss']
})
export class NavigationSectionComponent implements OnInit {

  constructor() { }
  @Input() sectionData;

  ngOnInit() {
  }

}
