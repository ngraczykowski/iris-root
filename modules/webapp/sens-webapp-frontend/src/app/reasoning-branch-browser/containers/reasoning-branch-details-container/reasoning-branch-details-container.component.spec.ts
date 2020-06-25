import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ReasoningBranchDetailsContainerComponent } from './reasoning-branch-details-container.component';

describe('ReasoningBranchDetailsContainerComponent', () => {
  let component: ReasoningBranchDetailsContainerComponent;
  let fixture: ComponentFixture<ReasoningBranchDetailsContainerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ReasoningBranchDetailsContainerComponent]
    })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ReasoningBranchDetailsContainerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
