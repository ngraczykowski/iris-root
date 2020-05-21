import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NavigationSectionComponent } from './navigation-section.component';
import { NO_ERRORS_SCHEMA } from '@angular/core';

describe('NavigationSectionComponent', () => {
  let component: NavigationSectionComponent;
  let fixture: ComponentFixture<NavigationSectionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NavigationSectionComponent ],
      schemas: [ NO_ERRORS_SCHEMA ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NavigationSectionComponent);
    component = fixture.componentInstance;

    component.sectionData = {
      visible: true
    };

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
