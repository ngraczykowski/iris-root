import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { Event, EventKey } from '@app/shared/event/event.service.model';
import { LocalEventService } from '@app/shared/event/local-event.service';
import { TestModule } from '@app/test/test.module';
import { BranchDetails } from '@model/branch.model';

import { BranchReviewButtonComponent } from './branch-review-button.component';
import { BranchReviewButtonModule } from './branch-review-button.module';

describe('BranchReviewButtonComponent', () => {
  let component: BranchReviewButtonComponent;
  let fixture: ComponentFixture<BranchReviewButtonComponent>;

  let eventService: LocalEventService;
  let httpMock: HttpTestingController;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [
            TestModule,
            HttpClientTestingModule,
            BranchReviewButtonModule
          ]
        })
        .compileComponents();

    httpMock = TestBed.get(HttpTestingController);
    eventService = TestBed.get(LocalEventService);
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BranchReviewButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should inProgress be false on init', () => {
    expect(component.inProgress).toBeFalsy();
  });

  it('should reviewedButtonVisible be true on init', () => {
    expect(component.reviewedButtonVisible).toBeTruthy();
  });

  describe('given branch details', () => {

    beforeEach(() => {
      component.branch = <BranchDetails> {
        decisionTreeInfo: {id: 1},
        matchGroupId: 2
      };
    });

    it('should call review branch post method onReviewBranch()', () => {
      component.onBranchReview();
      const req = httpMock.expectOne('/rest/webapp/api/decision-tree/1/branch/2/review');

      expect(req.request.method).toEqual('POST');
      expect(req.request.body).toEqual({});
    });

    it('should emit review event after review success', () => {
      spyOn(component.review, 'emit');
      component.onBranchReview();
      const req = httpMock.expectOne('/rest/webapp/api/decision-tree/1/branch/2/review');

      req.flush({});

      expect(component.review.emit).toHaveBeenCalled();
    });

    it('should inProgress be true while request is in progress', () => {
      component.onBranchReview();
      httpMock.expectOne('/rest/webapp/api/decision-tree/1/branch/2/review');

      expect(component.inProgress).toBeTruthy();
    });

    it('should set inProgress to false after review success', () => {
      component.onBranchReview();
      const req = httpMock.expectOne('/rest/webapp/api/decision-tree/1/branch/2/review');

      req.flush({});

      expect(component.inProgress).toBeFalsy();
    });

    it('should set inProgress to false after review error', () => {
      component.onBranchReview();
      const req = httpMock.expectOne('/rest/webapp/api/decision-tree/1/branch/2/review');

      req.error(<ErrorEvent> {});

      expect(component.inProgress).toBeFalsy();
    });

    it('should send error message when failed to review branch', () => {
      spyOn(eventService, 'sendEvent');
      component.onBranchReview();
      const req = httpMock.expectOne('/rest/webapp/api/decision-tree/1/branch/2/review');

      req.error(<ErrorEvent> {});

      expect(eventService.sendEvent).toHaveBeenCalledWith(<Event> {
        key: EventKey.NOTIFICATION,
        data: {
          message: 'reasoningBranch.review.message.error.UNKNOWN'
        }
      });
    });
  });
});
