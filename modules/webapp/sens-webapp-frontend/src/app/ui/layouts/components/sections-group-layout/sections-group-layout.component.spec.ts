import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SectionsGroupLayoutComponent } from './sections-group-layout.component';

describe('SectionsGroupLayoutComponent', () => {
  let component: SectionsGroupLayoutComponent;
  let fixture: ComponentFixture<SectionsGroupLayoutComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SectionsGroupLayoutComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SectionsGroupLayoutComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
