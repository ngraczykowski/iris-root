import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BranchChangesViewComponent } from './branch-changes-view.component';
import { SharedModule } from '../../../../../shared/shared.module';
import { ChangelogModule } from '../../../../../components/changelog/changelog.module';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { HttpClient } from '@angular/common/http';
import { Component, ViewChild } from '@angular/core';
import { BranchChanges } from './branch-changes';
import { RouterTestingModule } from '@angular/router/testing';

@Component({
  selector: 'app-host-component',
  template: '<app-branch-changes-view [data]="data"></app-branch-changes-view>'
})
class TestHostComponent {
  @ViewChild(BranchChangesViewComponent, {static: false})
  public branchChangesViewComponent: BranchChangesViewComponent;

  data: BranchChanges = {
    decisionTreeId: 1,
    matchGroupId: 2,
    pendingChanges: true
  };
}

function createTranslateLoader(http: HttpClient) {
  return new TranslateHttpLoader(http, '../assets/i18n/', '.json');
}

describe('BranchChangesViewComponent', () => {
  let testHostComponent: TestHostComponent;
  let testHostFixture: ComponentFixture<TestHostComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        SharedModule,
        ChangelogModule,
        RouterTestingModule,
        TranslateModule.forRoot({
          loader: {
            provide: TranslateLoader,
            useFactory: createTranslateLoader,
            deps: [HttpClient]
          }
        }),
      ],
      declarations: [
        BranchChangesViewComponent,
        TestHostComponent
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    testHostFixture = TestBed.createComponent(TestHostComponent);
    testHostComponent = testHostFixture.componentInstance;
    testHostFixture.detectChanges();
  });

  it('should create', () => {
    expect(testHostFixture).toBeTruthy();
  });

  it('should change changelogVisible to be truthy on openChangelog call', () => {
    testHostComponent.branchChangesViewComponent.openChangelog();
    expect(testHostComponent.branchChangesViewComponent.changelogVisible).toBeTruthy();
  });

  it('should change changelogVisible to be falsy on closeChangelog call', () => {
    testHostComponent.branchChangesViewComponent.closeChangelog();
    expect(testHostComponent.branchChangesViewComponent.changelogVisible).toBeFalsy();
  });
});
