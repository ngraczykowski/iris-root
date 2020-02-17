import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BranchEmptyStateComponent } from './branch-empty-state.component';

describe('EmptyStateComponent', () => {
  let component: BranchEmptyStateComponent;
  let fixture: ComponentFixture<BranchEmptyStateComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BranchEmptyStateComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BranchEmptyStateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
