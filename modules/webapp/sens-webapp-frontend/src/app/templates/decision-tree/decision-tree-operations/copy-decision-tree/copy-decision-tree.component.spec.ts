import { async, ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { of, throwError } from 'rxjs';
import { EventKey } from '../../../../shared/event/event.service.model';
import { LocalEventService } from '../../../../shared/event/local-event.service';
import { TestModule } from '../../../../test/test.module';
import { DecisionTreeOperationsModule } from '../decision-tree-operations.module';
import { CopyDecisionTreeComponent } from './copy-decision-tree.component';
import { CopyDecisionTreeService } from './copy-decision-tree.service';

describe('CopyDecisionTreeComponent', () => {
  let component: CopyDecisionTreeComponent;
  let fixture: ComponentFixture<CopyDecisionTreeComponent>;

  let copyDecisionTreeService: CopyDecisionTreeService;
  let eventService: LocalEventService;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [TestModule, DecisionTreeOperationsModule],
        })
        .compileComponents();

    copyDecisionTreeService = TestBed.get(CopyDecisionTreeService);
    eventService = TestBed.get(LocalEventService);
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CopyDecisionTreeComponent);
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
    expect(component.errorKey).toBeNull();
    expect(component.config.name).toBeUndefined();
    expect(component.shouldDisableConfirmButton()).toBeTruthy();
  });

  it('should have valid state when close component after opening ', () => {
    component.open({id: 'id1', name: 'name1'});
    component.close();

    expect(component.show).toBeFalsy();
    expect(component.decisionTree).toEqual({id: 'id1', name: 'name1'});
    expect(component.errorKey).toBeNull();
    expect(component.config.name).toBeUndefined();
    expect(component.shouldDisableConfirmButton()).toBeTruthy();
  });

  it('should have valid state when open and set config.name', () => {
    component.open({id: 'id1', name: 'name1'});
    component.config.name = 'name1';
    component.onChangeName();

    expect(component.show).toBeTruthy();
    expect(component.decisionTree).toEqual({id: 'id1', name: 'name1'});
    expect(component.errorKey).toBeNull();
    expect(component.config.name).toEqual('name1');
    expect(component.shouldDisableConfirmButton()).toBeFalsy();
  });

  it('should have valid state when open, set config.name and close', () => {
    component.open({id: 'id1', name: 'name1'});
    component.config.name = 'name1';
    component.onChangeName();
    component.close();

    expect(component.show).toBeFalsy();
    expect(component.decisionTree).toEqual({id: 'id1', name: 'name1'});
    expect(component.errorKey).toBeNull();
    expect(component.config.name).toEqual('name1');
    expect(component.shouldDisableConfirmButton()).toBeFalsy();
  });

  it('should have valid state when open, set config.name, close and open new', () => {
    component.open({id: 'id1', name: 'name1'});
    component.config.name = 'name1';
    component.onChangeName();
    component.close();
    component.open({id: 'id2', name: 'name2'});

    expect(component.show).toBeTruthy();
    expect(component.decisionTree).toEqual({id: 'id2', name: 'name2'});
    expect(component.errorKey).toBeNull();
    expect(component.config.name).toBeUndefined();
    expect(component.shouldDisableConfirmButton()).toBeTruthy();
  });

  it('should have valid state when open, set config.name, confirm without error', fakeAsync(() => {
    spyOn(copyDecisionTreeService, 'copy').and.returnValue(of({}));
    spyOn(eventService, 'sendEvent');

    component.open({id: 'id1', name: 'name1'});
    component.config.name = 'name1';
    component.onChangeName();
    component.confirm();
    tick();

    expect(component.show).toBeFalsy();
    expect(component.decisionTree).toEqual({id: 'id1', name: 'name1'});
    expect(component.errorKey).toBeNull();
    expect(component.config.name).toEqual('name1');
    expect(component.shouldDisableConfirmButton()).toBeTruthy();
    expect(eventService.sendEvent).toHaveBeenCalledWith({
      key: EventKey.NOTIFICATION,
      data: {
        type: 'success',
        message: 'decisionTree.message.copy.success'
      }
    });
  }));

  it('should have valid state when open, set config.name, confirm with error', fakeAsync(() => {
    spyOn(copyDecisionTreeService, 'copy').and.returnValue(throwError({
      error: {key: 'DecisionTreeNameAlreadyExists'}
    }));
    spyOn(eventService, 'sendEvent');

    component.open({id: 'id1', name: 'name1'});
    component.config.name = 'name1';
    component.onChangeName();
    component.confirm();
    tick();

    expect(component.show).toBeTruthy();
    expect(component.decisionTree).toEqual({id: 'id1', name: 'name1'});
    expect(component.errorKey).toEqual('decisionTree.message.copy.error.NAME_ALREADY_EXISTS');
    expect(component.config.name).toEqual('name1');
    expect(component.shouldDisableConfirmButton()).toBeTruthy();
    expect(eventService.sendEvent).not.toHaveBeenCalled();
  }));
});
