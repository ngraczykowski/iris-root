import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ChangelogModule } from '@app/components/changelog/changelog.module';
import { TestModule } from '@app/test/test.module';
import { of } from 'rxjs';
import { ChangelogClient } from './changelog-client';
import { ChangelogComponent } from './changelog.component';
import { ChangelogView } from './changelog.model';

describe('ChangelogComponent', () => {
  let component: ChangelogComponent;
  let fixture: ComponentFixture<ChangelogComponent>;
  let changelogService: ChangelogClient;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [
            TestModule,
            ChangelogModule
          ],
        }).compileComponents();

    changelogService = TestBed.get(ChangelogClient);
    spyOn(changelogService, 'getChangelog').and.returnValue(of(mockChangelogView()));
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ChangelogComponent);
    component = fixture.componentInstance;
    component.decisionTreeId = 1;
    component.matchGroupId = 2;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  function mockChangelogView(): ChangelogView {
    return {
      reasoningBranchId: {decisionTreeId: 1, matchGroupId: 2},
      solutionChange: {
        current: 'POTENTIAL_TRUE_POSITIVE',
        proposed: 'FALSE_POSITIVE'
      },
      statusChange: {current: false, proposed: true}, changelog: [
        {comment: 'Aprover 2 comment', userName: 'approver2', timestamp: 1544711090000, level: 2},
        {comment: 'Approver 1 comment', userName: 'approver1', timestamp: 1444711090000, level: 1},
        {comment: 'Maker comment', userName: 'maker', timestamp: 1344711090000, level: 0}
      ]
    };
  }
});
