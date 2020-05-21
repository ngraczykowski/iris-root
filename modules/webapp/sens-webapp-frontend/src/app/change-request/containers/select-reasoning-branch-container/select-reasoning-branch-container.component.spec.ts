import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SelectReasoningBranchContainerComponent } from './select-reasoning-branch-container.component';

describe('SelectReasoningBranchContainerComponent', () => {
  let component: SelectReasoningBranchContainerComponent;
  let fixture: ComponentFixture<SelectReasoningBranchContainerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SelectReasoningBranchContainerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SelectReasoningBranchContainerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
