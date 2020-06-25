import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ReasoningBranchesListContainerComponent } from './reasoning-branches-list-container.component';

describe('ReasoningBranchesListContainerComponent', () => {
  let component: ReasoningBranchesListContainerComponent;
  let fixture: ComponentFixture<ReasoningBranchesListContainerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ReasoningBranchesListContainerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ReasoningBranchesListContainerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
