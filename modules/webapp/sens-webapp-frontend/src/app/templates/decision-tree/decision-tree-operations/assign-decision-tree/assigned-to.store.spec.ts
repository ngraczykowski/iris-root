import { async, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { TestModule } from '../../../../test/test.module';
import { BatchType, BatchTypes, BatchTypeUpdates } from './assigned-to.model';
import { AssignedToService } from './assigned-to.service';
import { AssignedToStore } from './assigned-to.store';

describe('AssignedToStore', () => {
  let underTest: AssignedToStore;
  let assignedToService: AssignedToService;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [TestModule],
      providers: [AssignedToStore, AssignedToService]
    }).compileComponents();
  }));

  beforeEach(() => {
    assignedToService = TestBed.get(AssignedToService);
    underTest = new AssignedToStore(assignedToService);
  });

  it('should use service method, when fetchDecisionTree', () => {
    spyOn(assignedToService, 'getBatchTypesForTree')
        .and.returnValue(of(createExampleBatchTypes()));

    underTest.fetchDecisionTreeBatchTypes(1);

    expect(assignedToService.getBatchTypesForTree).toHaveBeenCalled();
  });

  describe('given one unique Batch Type per available, assigned, activated', () => {
    const activatedBatchType = createBatchType('activated_batch_type', true);
    const assignedBatchType = createBatchType('assigned_batch_type', true);
    const availableBatchType = createBatchType('available_batch_type', true);
    const decisionTreeId = 1;
    beforeEach(() => {
      spyOn(assignedToService, 'getBatchTypesForTree')
          .and.returnValue(of({
        activated: [activatedBatchType],
        assigned: [assignedBatchType],
        available: [availableBatchType]
      } as BatchTypes));

      underTest.fetchDecisionTreeBatchTypes(decisionTreeId);
    });

    it('should move assignedBatchType to activatedBatchTypes, ' +
        'when assignedBatchType is activated', () => {
      underTest.activate(assignedBatchType);

      underTest.assignedBatchTypes.subscribe(actual => {
        expect(actual.length).toBe(0);
      });
      underTest.activatedBatchTypes.subscribe(actual => {
        expect(actual).toContain(activatedBatchType);
        expect(actual).toContain(assignedBatchType);
      });
    });

    it('should move activatedBatchType to assignedBatchTypes, ' +
        'when activatedBatchType is deactivated', () => {
      underTest.deactivate(activatedBatchType);

      underTest.assignedBatchTypes.subscribe(actual => {
        expect(actual.length).toBe(2);
        expect(actual).toContain(activatedBatchType);
        expect(actual).toContain(assignedBatchType);
      });
      underTest.activatedBatchTypes.subscribe(actual => {
        expect(actual.length).toBe(0);
      });
    });

    it('should state be same as given, when assignedBatchType is unassigned and ' +
        'then assigned again, ', () => {
      underTest.unassign(assignedBatchType);
      underTest.assign(assignedBatchType);

      underTest.activatedBatchTypes.subscribe(actual => {
        expect(actual).toContain(activatedBatchType);
      });

      underTest.assignedBatchTypes.subscribe(actual => {
        expect(actual).toContain(assignedBatchType);
      });

      underTest.availableBatchTypes.subscribe(actual => {
        expect(actual).toContain(availableBatchType);
      });
    });

    it('should availableBatchTypes contain 2 batchTypes and activated should be empty, ' +
        'when activated BatchType is deactivated and then unassigned', () => {
      underTest.deactivate(activatedBatchType);
      underTest.unassign(activatedBatchType);

      underTest.availableBatchTypes.subscribe(actual => {
        expect(actual).toContain(activatedBatchType);
        expect(actual).toContain(availableBatchType);
      });
      underTest.activatedBatchTypes.subscribe(actual => {
        expect(actual.length).toBe(0);
      });
    });

    it('should saveBatchTypesAssignments not call service and return completed ' +
        'observable, when saved and no changes were made', () => {
      spyOn(assignedToService, 'updateBatchTypesForTree');

      underTest.saveBatchTypesAssignments();

      expect(assignedToService.updateBatchTypesForTree).toHaveBeenCalledTimes(0);
    });

    it('should saveChanges call service with correct update values, ' +
        'when assignedBatchType is activated', () => {
      spyOn(assignedToService, 'updateBatchTypesForTree')
          .and.returnValue(of({}));

      underTest.activate(assignedBatchType);
      underTest.saveBatchTypesAssignments();

      expect(assignedToService.updateBatchTypesForTree).toHaveBeenCalledWith(decisionTreeId, {
        toUnassign: [],
        toAssign: [],
        toDeactivate: [],
        toActivate: [assignedBatchType],
      } as BatchTypeUpdates);
    });

    it('should saveChanges call service with correct update values, ' +
        'when availableBatchType is assigned and activated', () => {
      spyOn(assignedToService, 'updateBatchTypesForTree')
          .and.returnValue(of({}));

      underTest.assign(availableBatchType);
      underTest.activate(availableBatchType);
      underTest.saveBatchTypesAssignments();

      expect(assignedToService.updateBatchTypesForTree).toHaveBeenCalledWith(decisionTreeId, {
        toUnassign: [],
        toAssign: [availableBatchType],
        toDeactivate: [],
        toActivate: [availableBatchType],
      } as BatchTypeUpdates);
    });

    it('should saveChanges call service with correct update values, ' +
        'when activatedBatchType is deactivated and assignedBatchType is activated', () => {
      spyOn(assignedToService, 'updateBatchTypesForTree')
          .and.returnValue(of({}));

      underTest.deactivate(activatedBatchType);
      underTest.activate(assignedBatchType);
      underTest.saveBatchTypesAssignments();

      expect(assignedToService.updateBatchTypesForTree).toHaveBeenCalledWith(decisionTreeId, {
        toUnassign: [],
        toAssign: [],
        toDeactivate: [activatedBatchType],
        toActivate: [assignedBatchType],
      } as BatchTypeUpdates);
    });

    it('should saveChanges call service with correct update values, ' +
        'when assignedBatchType is unassigned, activatedBatchTypes is deactivated, ' +
        'unassigned and availableBatchType are assigned', () => {
      spyOn(assignedToService, 'updateBatchTypesForTree')
          .and.returnValue(of({}));

      underTest.unassign(assignedBatchType);
      underTest.deactivate(activatedBatchType);
      underTest.unassign(activatedBatchType);
      underTest.assign(availableBatchType);
      underTest.saveBatchTypesAssignments();

      expect(assignedToService.updateBatchTypesForTree).toHaveBeenCalledWith(decisionTreeId, {
        toUnassign: [assignedBatchType, activatedBatchType],
        toAssign: [availableBatchType],
        toDeactivate: [activatedBatchType],
        toActivate: [],
      } as BatchTypeUpdates);
    });

  });

  function createBatchType(name, canActivate): BatchType {
    return {
      name: name,
      canActivate: canActivate,
    } as BatchType;
  }

  function createExampleBatchTypes(): BatchTypes {
    return {
      activated: [createBatchType('act', true)],
      assigned: [createBatchType('ass', true)],
      available: [createBatchType('act', true)]
    } as BatchTypes;
  }

});
