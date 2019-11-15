import { async, ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { of, throwError } from 'rxjs';
import { EventKey } from '../../../../shared/event/event.service.model';
import { LocalEventService } from '../../../../shared/event/local-event.service';
import { TestModule } from '../../../../test/test.module';
import { DecisionTreeOperationsModule } from '../decision-tree-operations.module';
import { RemoveDecisionTreeComponent } from './remove-decision-tree.component';
import { RemoveDecisionTreeService } from './remove-decision-tree.service';

describe('RemoveDecisionTreeComponent', () => {
  let component: RemoveDecisionTreeComponent;
  let fixture: ComponentFixture<RemoveDecisionTreeComponent>;

  let removeDecisionTreeService: RemoveDecisionTreeService;
  let eventService: LocalEventService;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [TestModule, DecisionTreeOperationsModule]
        })
        .compileComponents();

    removeDecisionTreeService = TestBed.get(RemoveDecisionTreeService);
    eventService = TestBed.get(LocalEventService);
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RemoveDecisionTreeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have valid state when open component', () => {
    component.open({id: 'id1', name: 'name1'});

    expect(component.show).toBeTruthy();
    expect(component.decisionTree).toEqual({id: 'id1', name: 'name1'});
    expect(component.confirmInputValue).toBeNull();
    expect(component.shouldDisableConfirmButton()).toBeTruthy();
  });

  it('should have valid state when close component after opening ', () => {
    component.open({id: 'id1', name: 'name1'});
    component.close();

    expect(component.show).toBeFalsy();
    expect(component.decisionTree).toEqual({id: 'id1', name: 'name1'});
    expect(component.confirmInputValue).toBeNull();
    expect(component.shouldDisableConfirmButton()).toBeTruthy();
  });

  it('should have valid state when open and set confirm value to Delete', () => {
    component.open({id: 'id1', name: 'name1'});
    component.confirmInputValue = 'Delete';

    expect(component.show).toBeTruthy();
    expect(component.decisionTree).toEqual({id: 'id1', name: 'name1'});
    expect(component.confirmInputValue).toEqual('Delete');
    expect(component.shouldDisableConfirmButton()).toBeFalsy();
  });

  it('should have valid state when open, set confirm value to Delete and close', () => {
    component.open({id: 'id1', name: 'name1'});
    component.confirmInputValue = 'Delete';
    component.close();

    expect(component.show).toBeFalsy();
    expect(component.decisionTree).toEqual({id: 'id1', name: 'name1'});
    expect(component.confirmInputValue).toEqual('Delete');
    expect(component.shouldDisableConfirmButton()).toBeFalsy();
  });

  it('should have valid state when open, set confirm value to Delete, close and open new', () => {
    component.open({id: 'id1', name: 'name1'});
    component.confirmInputValue = 'Delete';
    component.close();
    component.open({id: 'id2', name: 'name2'});

    expect(component.show).toBeTruthy();
    expect(component.decisionTree).toEqual({id: 'id2', name: 'name2'});
    expect(component.confirmInputValue).toBeNull();
    expect(component.shouldDisableConfirmButton()).toBeTruthy();
  });

  it('should have valid state when open, set confirm value to Delete, confirm without error', fakeAsync(() => {
    spyOn(removeDecisionTreeService, 'remove').and.returnValue(of({}));
    spyOn(eventService, 'sendEvent');

    component.open({id: 'id1', name: 'name1'});
    component.confirmInputValue = 'Delete';
    component.confirm();
    tick();

    expect(component.show).toBeFalsy();
    expect(component.decisionTree).toEqual({id: 'id1', name: 'name1'});
    expect(component.confirmInputValue).toEqual('Delete');
    expect(component.shouldDisableConfirmButton()).toBeFalsy();
    expect(eventService.sendEvent).toHaveBeenCalledWith({
      key: EventKey.NOTIFICATION,
      data: {
        type: 'success',
        message: 'decisionTree.message.remove.success'
      }
    });
  }));

  it('should have valid state when open, set confirm value to Delete, confirm with error', fakeAsync(() => {
    spyOn(removeDecisionTreeService, 'remove').and.returnValue(throwError({error: {key: 'key'}}));
    spyOn(eventService, 'sendEvent');

    component.open({id: 'id1', name: 'name1'});
    component.confirmInputValue = 'Delete';
    component.confirm();
    tick();

    expect(component.show).toBeTruthy();
    expect(component.decisionTree).toEqual({id: 'id1', name: 'name1'});
    expect(component.confirmInputValue).toEqual('Delete');
    expect(component.shouldDisableConfirmButton()).toBeFalsy();
    expect(eventService.sendEvent).toHaveBeenCalledWith({
      key: EventKey.NOTIFICATION,
      data: {
        type: 'error',
        message: 'decisionTree.message.remove.error.UNKNOWN'
      }
    });
  }));
});
