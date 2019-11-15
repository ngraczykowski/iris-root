import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { LocalEventService } from '../../../../shared/event/local-event.service';
import { TestModule } from '../../../../test/test.module';
import { DecisionTreeOperationsModule } from '../decision-tree-operations.module';
import { EditDecisionTreeComponent } from './edit-decision-tree.component';
import { EditDecisionTreeService } from './edit-decision-tree.service';

describe('EditDecisionTreeComponent', () => {
  let component: EditDecisionTreeComponent;
  let fixture: ComponentFixture<EditDecisionTreeComponent>;

  let editDecisionTreeService: EditDecisionTreeService;
  let eventService: LocalEventService;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [TestModule, DecisionTreeOperationsModule]
        })
        .compileComponents();

    editDecisionTreeService = TestBed.get(EditDecisionTreeService);
    eventService = TestBed.get(LocalEventService);
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EditDecisionTreeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

});
