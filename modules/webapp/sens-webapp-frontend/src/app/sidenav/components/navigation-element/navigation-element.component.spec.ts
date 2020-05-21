import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NavigationElementComponent } from './navigation-element.component';
import { NO_ERRORS_SCHEMA } from '@angular/core';

describe('NavigationElementComponent', () => {
  let component: NavigationElementComponent;
  let fixture: ComponentFixture<NavigationElementComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NavigationElementComponent ],
      schemas: [ NO_ERRORS_SCHEMA ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NavigationElementComponent);
    component = fixture.componentInstance;

    component.navElement = {
      url: 'test'
    };

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
