import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { TestModule } from '@app/test/test.module';
import { TranslateService } from '@ngx-translate/core';

import { ChangelogHistoryComponent } from './changelog-history.component';

// TODO(iwnek)
describe('ChangelogHistoryComponent', () => {
  const translation = 'translation';
  let component: ChangelogHistoryComponent;
  let fixture: ComponentFixture<ChangelogHistoryComponent>;
  let translateService: TranslateService;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ChangelogHistoryComponent ],
      imports: [ TestModule ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ChangelogHistoryComponent);
    component = fixture.componentInstance;
    translateService = TestBed.get(TranslateService);
    spyOn(translateService, 'instant').and.returnValue(translation);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
