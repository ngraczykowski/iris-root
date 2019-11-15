import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { TestModule } from '../../../../../test/test.module';
import { InboxMessage } from '../../../../model/inbox.model';
import { DisabledBranchDescriptionMapper } from './disabled-branch-description-mapper';
import {
  DisabledBranchDescription,
  DisabledBranchDescriptionViewComponent
} from './disabled-branch-description-view.component';

class BranchDescriptionMapperStub {
  map() {}
}

describe('DisabledBranchDescriptionViewComponent', () => {
  let component: DisabledBranchDescriptionViewComponent;
  let fixture: ComponentFixture<DisabledBranchDescriptionViewComponent>;

  let mapper: BranchDescriptionMapperStub;

  beforeEach(async(() => {
    mapper = new BranchDescriptionMapperStub();
    TestBed
        .configureTestingModule({
          declarations: [DisabledBranchDescriptionViewComponent],
          imports: [TestModule]
        })
        .overrideComponent(DisabledBranchDescriptionViewComponent, {
          set: {
            providers: [
              {
                provide: DisabledBranchDescriptionMapper,
                useValue: mapper
              }
            ]
          }
        })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DisabledBranchDescriptionViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should update description after change data', () => {
    const data = <InboxMessage> {
      referenceId: '1-1',
      type: 'type',
      message: 'message',
      extra: {
        decisionTreeId: 1,
        matchGroupId: 1,
        decisionTreeName: 'name',
        suspendingAlerts: [],
        aiDecision: 'aiDecision'
      }
    };
    const description = <DisabledBranchDescription> {
      messageKey: 'message',
      aiDecision: 'aiDecision',
      branchLink: {name: 'branchName', url: 'branchUrl'},
      alertInfos: [{
        analystDecision: 'analystDecision',
        link: {name: 'alertName', url: 'alertUrl'}
      }]
    };
    spyOn(mapper, 'map').and.returnValue(description);

    component.data = data;

    expect(mapper.map).toHaveBeenCalledWith(data);
    expect(component.description).toEqual(description);
  });
});
