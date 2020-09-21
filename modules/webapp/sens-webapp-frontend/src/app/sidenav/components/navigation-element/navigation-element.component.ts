import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-navigation-element',
  templateUrl: './navigation-element.component.html',
  styleUrls: ['./navigation-element.component.scss']
})
export class NavigationElementComponent {

  @Input() navElement;

  constructor(private router: Router) { }

  action(): void {
    if (this.navElement.url) {
      this.router.navigate([this.navElement.url]);
    } else if (this.navElement.action) {
      this.navElement.action();
    }
  }

}
