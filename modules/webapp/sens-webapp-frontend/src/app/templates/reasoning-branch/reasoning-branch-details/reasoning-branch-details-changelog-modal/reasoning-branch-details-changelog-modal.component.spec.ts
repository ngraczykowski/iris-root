import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { ChangelogModule } from '@app/components/changelog/changelog.module';

import { SharedModule } from '@app/shared/shared.module';

import { TestModule } from '@app/test/test.module';

import { ReasoningBranchDetailsChangelogModalComponent } from './reasoning-branch-details-changelog-modal.component';

const fakeDecisionTreeId = 1;
const fakeMatchGroupId = 14;

describe('ReasoningBranchDetailsChangelogModalComponent', () => {
  let component: ReasoningBranchDetailsChangelogModalComponent;
  let fixture: ComponentFixture<ReasoningBranchDetailsChangelogModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ReasoningBranchDetailsChangelogModalComponent],
      imports: [SharedModule, ChangelogModule, TestModule] })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ReasoningBranchDetailsChangelogModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    component.decisionTreeId = fakeDecisionTreeId;
    component.matchGroupId = fakeMatchGroupId;
    fixture.detectChanges();
  });

  it('should create', () => {
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });

  it('Should show Modal when "showChangeLog" is "true"', () => {
    component.showChangeLog = true;

    fixture.detectChanges();

    const popupWindow = fixture.debugElement.query(By.css('.popup-window'));
    expect(popupWindow.classes['is-active']).toBeTruthy();
  });

  it('Should hide Modal when "showChangeLog" is "false"', () => {
    component.showChangeLog = false;

    fixture.detectChanges();

    const popupWindow = fixture.debugElement.query(By.css('.popup-window'));
    expect(popupWindow.classes['is-active']).toBeFalsy();
  });
});
