import { Component, Input, OnChanges, OnInit } from '@angular/core';

export class Breadcrumb {
  links: BreadcrumbLink[];
}

export class BreadcrumbLink {
  name: string;
  url?: string;
}

export interface BreadcrumbsProvider {
  get(): Breadcrumb[];
}

@Component({
  selector: 'app-breadcrumbs',
  templateUrl: './breadcrumbs.component.html',
  styleUrls: ['./breadcrumbs.component.scss']
})
export class BreadcrumbsComponent implements OnInit, OnChanges {

  @Input()
  breadcrumbsProvider: BreadcrumbsProvider;

  breadcrumbs: Breadcrumb[];
  error: boolean;

  constructor() { }

  ngOnInit() {
    this.update();
  }

  ngOnChanges() {
    this.update();
  }

  update() {
    try {
      this.breadcrumbs = this.breadcrumbsProvider.get();
    } catch (e) {
      this.error = true;
      console.error(e);
    }
  }
}
