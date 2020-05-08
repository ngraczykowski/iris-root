import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NavigationElementComponent } from './navigation-element.component';

describe('NavigationElementComponent', () => {
  let component: NavigationElementComponent;
  let fixture: ComponentFixture<NavigationElementComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NavigationElementComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NavigationElementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
