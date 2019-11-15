import { Component, ComponentFactoryResolver, Input, OnInit, Type, ViewChild } from '@angular/core';
import { ViewDirective } from './view.directive';

export interface DynamicComponent {
  data;
}

export class View {
  constructor(public component: Type<DynamicComponent>, public data) {}
}

@Component({
  selector: 'app-dynamic-view',
  templateUrl: './dynamic-view.component.html',
  styleUrls: ['./dynamic-view.component.scss']
})
export class DynamicViewComponent implements OnInit {

  @Input() view: View;
  @ViewChild(ViewDirective, {static: true}) viewDirective: ViewDirective;

  constructor(private componentFactoryResolver: ComponentFactoryResolver) { }

  ngOnInit() {
    this.loadComponent();
  }

  private loadComponent() {
    const componentFactory =
        this.componentFactoryResolver.resolveComponentFactory(this.view.component);

    const viewContainerRef = this.viewDirective.viewContainerRef;
    viewContainerRef.clear();

    const componentRef = viewContainerRef.createComponent(componentFactory);
    componentRef.instance.data = this.view.data;
  }
}
