import { ComponentFactoryResolver, ViewContainerRef } from '@angular/core';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { TestModule } from '../../test/test.module';

import { DynamicComponent, DynamicViewComponent } from './dynamic-view.component';
import { ViewDirective } from './view.directive';

describe('DynamicViewComponent', () => {
  let component: DynamicViewComponent;
  let fixture: ComponentFixture<DynamicViewComponent>;

  let callOrder;

  class FactoryMock {

  }

  class DynamicComponentMock implements DynamicComponent {
    data;
  }

  class ComponentFactoryResolverMock {
    resolveComponentFactory() {}
  }

  class ViewContainerRefMock {
    clear() {}

    createComponent() {}
  }

  let componentFactoryResolver: ComponentFactoryResolver;
  let viewContainerRef: ViewContainerRef;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [TestModule],
          providers: [
            {provide: ComponentFactoryResolver, useClass: ComponentFactoryResolverMock},
            {provide: ViewContainerRef, useClass: ViewContainerRefMock}]
        })
        .compileComponents();

    componentFactoryResolver = TestBed.get(ComponentFactoryResolver);
    viewContainerRef = TestBed.get(ViewContainerRef);

    callOrder = [];
  }));

  function initComponent() {
    fixture = TestBed.createComponent(DynamicViewComponent);
    component = fixture.componentInstance;
    component.view = {component: DynamicComponentMock, data: {}};
    component.viewDirective = new ViewDirective(viewContainerRef);
    fixture.detectChanges();
  }

  it('should init component correctly', () => {
    spyOn(componentFactoryResolver, 'resolveComponentFactory').and.callFake(
        () => {
          callOrder.push('resolveComponentFactory');
          return new FactoryMock();
        });
    spyOn(viewContainerRef, 'clear').and.callFake(
        () => callOrder.push('clear'));
    spyOn(viewContainerRef, 'createComponent').and.callFake(
        () => {
          callOrder.push('createComponent');
          return {instance: new DynamicComponentMock()};
        });

    initComponent();

    expect(component).toBeTruthy();
    expect(callOrder).toEqual(
        ['resolveComponentFactory', 'clear', 'createComponent']);
    expect(componentFactoryResolver.resolveComponentFactory)
        .toHaveBeenCalledWith(DynamicComponentMock);
    expect(viewContainerRef.clear)
        .toHaveBeenCalled();
    expect(viewContainerRef.createComponent)
        .toHaveBeenCalledWith(new FactoryMock());
  });
});
