import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import {
  ErrorData,
  ErrorFeedbackComponent
} from '@app/components/error-feedback/error-feedback.component';
import { TranslateServiceWrapper } from '@app/shared/translate/translate-service-wrapper';
import { TestModule } from '@app/test/test.module';
import { TranslateService } from '@ngx-translate/core';
import { of, throwError } from 'rxjs';

function mockTranslate(translate: TranslateService, registered) {
  spyOn(translate, 'get').and.callFake((key, params) => {
    if (registered.includes(key)) {
      return of(key + ':' + params);
    }
    return throwError({});
  });
}

describe('ErrorFeedbackComponent', () => {
  let component: ErrorFeedbackComponent;
  let fixture: ComponentFixture<ErrorFeedbackComponent>;

  let translate: TranslateService;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [TestModule]
        })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ErrorFeedbackComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    translate = TestBed.get(TranslateServiceWrapper);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should errorMessages be undefined if data is null', () => {
    component.data = null;

    expect(component.errorMessages).toBeUndefined();
  });

  describe('Single error test', () => {

    it('should set valid errorMessage if provide single error data', () => {
      mockTranslate(translate, ['key']);

      component.data = <ErrorData> {key: 'key', extras: 'extra'};

      expect(component.errorMessages).toEqual(['key:extra']);
    });

    it('should set unknown error message if provide unknown error data', () => {
      mockTranslate(translate, ['unknown']);

      component.data = <ErrorData> {key: 'key1', extras: 'extra1'};

      expect(component.errorMessages).toEqual(['unknown:undefined']);
    });

    it('should add prefix if provide error data and prefix', () => {
      mockTranslate(translate, ['prefix.key1']);

      component.translatePrefix = 'prefix';
      component.data = <ErrorData> {key: 'key1', extras: 'extra1'};

      expect(component.errorMessages).toEqual(['prefix.key1:extra1']);
    });
  });

  describe('Multiple errors test', () => {

    it('should set valid errorMessages if provide multiple error data', () => {
      mockTranslate(translate, ['key1', 'key2', 'key3']);

      component.data = [
        <ErrorData> {key: 'key1', extras: 'extra1'},
        <ErrorData> {key: 'key2', extras: 'extra2'},
        <ErrorData> {key: 'key3', extras: 'extra3'},
      ];

      expect(component.errorMessages).toEqual(['key1:extra1', 'key2:extra2', 'key3:extra3']);
    });

    it('should add unknown error message if some errors is unknown', () => {
      mockTranslate(translate, ['unknown', 'key1']);

      component.data = [
        <ErrorData> {key: 'key1', extras: 'extra1'},
        <ErrorData> {key: 'key2', extras: 'extra2'}
      ];

      expect(component.errorMessages).toEqual(['key1:extra1', 'unknown:undefined']);
    });

    it('should add prefix to errors', () => {
      mockTranslate(translate, ['prefix.unknown', 'prefix.key1']);

      component.translatePrefix = 'prefix';
      component.data = [
        <ErrorData> {key: 'key1', extras: 'extra1'},
        <ErrorData> {key: 'key2', extras: 'extra2'}
      ];

      expect(component.errorMessages).toEqual(['prefix.key1:extra1', 'prefix.unknown:undefined']);
    });
  });
});
