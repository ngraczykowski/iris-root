import { HttpClient, HttpClientModule } from '@angular/common/http';
import { ErrorHandler, NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { RouterModule } from '@angular/router';
import { BreadcrumbsComponent } from '@app/components/breadcrumbs/breadcrumbs.component';
import { CodeBlockComponent } from '@app/components/code-block/code-block.component';
import { DropdownSelectorComponent } from '@app/components/dropdown-selector/dropdown-selector.component';
import { DynamicTableComponent } from '@app/components/dynamic-table/dynamic-table.component';
import { DynamicViewTableComponent } from '@app/components/dynamic-view-table/dynamic-view-table.component';
import { DynamicViewComponent } from '@app/components/dynamic-view/dynamic-view.component';
import { ViewDirective } from '@app/components/dynamic-view/view.directive';
import { ElementStatusComponent } from '@app/components/element-status/element-status.component';
import { EmptyStateComponent } from '@app/components/empty-state/empty-state.component';
import { ErrorFeedbackComponent } from '@app/components/error-feedback/error-feedback.component';
import { FeedbackMessageComponent } from '@app/components/feedback-message/feedback-message.component';
import { IconComponent } from '@app/components/icon/icon.component';
import { InputControlFeedbackComponent } from '@app/components/input-control-feedback/input-control-feedback.component';
import { InputDescriptionComponent } from '@app/components/input-description/input-description.component';
import { InputFeedbackComponent } from '@app/components/input-feedback/input-feedback.component';
import { KeyValueVerticalTableComponent } from '@app/components/key-value-vertical-table/key-value-vertical-table.component';
import { KeyboardShortcutsComponent } from '@app/components/keyboard-shortcuts/keyboard-shortcuts.component';
import { LoadingLoopComponent } from '@app/components/loading-loop/loading-loop.component';
import { LoadingComponent } from '@app/components/loading/loading.component';
import { OverlayComponent } from '@app/components/overlay/overlay.component';
import { CommunicationErrorComponent } from '@app/components/popup-window/communication-error.component';
import { PreloaderComponent } from '@app/components/preloader/preloader.component';
import { PreviewElementComponent } from '@app/components/preview-element/preview-element.component';
import { SolutionChangeComponent } from '@app/components/solution-change/solution-change.component';
import { SolutionTagComponent } from '@app/components/solution-tag/solution-tag.component';
import { StatusChangeComponent } from '@app/components/status-change/status-change.component';
import { SwitchViewComponent } from '@app/components/switch-view/switch-view.component';
import { TableHintsComponent } from '@app/components/table-hints/table-hints.component';
import { TagComponent } from '@app/components/tag/tag.component';
import { ActivityMonitorModule } from '@app/shared/activity-monitor/activity-monitor.module';
import { AuthModule } from '@app/shared/auth/auth.module';
import {
  BackgroundServicesManager,
  BackgroundServiceToken
} from '@app/shared/background-services-manager';
import { ClipboardService } from '@app/shared/clipboard/clipboard.service';
import { DefaultErrorHandler } from '@app/shared/default-error-handler';
import { NotImplementedDirective } from '@app/shared/directives/not-implemented/not-implemented.directive';
import { EventModule } from '@app/shared/event/event.module';
import { FrontendSettingsService } from '@app/shared/frontend-settings/frontend-settings.service';
import { httpInterceptorProviders } from '@app/shared/interceptors';
import { PipeModule } from '@app/shared/pipes/pipe.module';
import { RedirectorService } from '@app/shared/redirector.service';
import { TranslateExceptionHandler } from '@app/shared/translate/translate-exception-handler';
import { TranslateServiceWrapper } from '@app/shared/translate/translate-service-wrapper';
import { environment } from '@env/environment';
import { MissingTranslationHandler, TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { InlineSVGModule } from 'ng-inline-svg';
import { PERFECT_SCROLLBAR_CONFIG, PerfectScrollbarModule } from 'ngx-perfect-scrollbar';
import { HighlightSearchPipe } from '@app/templates/user-management/highlight-search.pipe';
import { SolutionSettingsService } from './solution-settings.service';

export function createTranslateLoader(http: HttpClient) {
  return new TranslateHttpLoader(http, '../assets/i18n/', '.json');
}

@NgModule({
  imports: [
    RouterModule,
    HttpClientModule,
    BrowserModule,
    ReactiveFormsModule,
    FormsModule,
    TranslateModule.forRoot({
      missingTranslationHandler: {
        provide: MissingTranslationHandler,
        useClass: TranslateExceptionHandler
      },
      loader: {
        provide: TranslateLoader,
        useFactory: createTranslateLoader,
        deps: [HttpClient]
      }
    }),
    InlineSVGModule.forRoot(),
    ActivityMonitorModule,
    EventModule,
    AuthModule,
    PerfectScrollbarModule,
    PipeModule
  ],
  declarations: [
    HighlightSearchPipe,
    LoadingComponent,
    PreloaderComponent,
    EmptyStateComponent,
    DynamicViewTableComponent,
    DynamicViewComponent,
    ViewDirective,
    KeyboardShortcutsComponent,
    DynamicTableComponent,
    KeyValueVerticalTableComponent,
    LoadingLoopComponent,
    CommunicationErrorComponent,
    OverlayComponent,
    SwitchViewComponent,
    FeedbackMessageComponent,
    InputFeedbackComponent,
    InputControlFeedbackComponent,
    InputDescriptionComponent,
    BreadcrumbsComponent,
    NotImplementedDirective,
    DropdownSelectorComponent,
    ElementStatusComponent,
    IconComponent,
    TagComponent,
    StatusChangeComponent,
    ErrorFeedbackComponent,
    SolutionTagComponent,
    SolutionChangeComponent,
    CodeBlockComponent,
    TableHintsComponent,
    PreviewElementComponent
  ],
  providers: [
    {
      provide: ErrorHandler,
      useClass: DefaultErrorHandler
    },
    httpInterceptorProviders,
    {
      provide: BackgroundServicesManager,
      useClass: BackgroundServicesManager,
      deps: [BackgroundServiceToken]
    },
    {provide: PERFECT_SCROLLBAR_CONFIG, useValue: environment.perfectScrollbarConfig},
    RedirectorService,
    FrontendSettingsService,
    ClipboardService,
    TranslateServiceWrapper,
    HighlightSearchPipe,
    SolutionSettingsService
  ],
  exports: [
    RouterModule,
    TranslateModule,
    BrowserModule,
    ReactiveFormsModule,
    FormsModule,
    InlineSVGModule,
    PerfectScrollbarModule,
    PipeModule,
    PreloaderComponent,
    LoadingComponent,
    EmptyStateComponent,
    DynamicViewTableComponent,
    DynamicViewComponent,
    ViewDirective,
    KeyboardShortcutsComponent,
    DynamicTableComponent,
    KeyValueVerticalTableComponent,
    LoadingLoopComponent,
    CommunicationErrorComponent,
    OverlayComponent,
    SwitchViewComponent,
    FeedbackMessageComponent,
    InputFeedbackComponent,
    InputControlFeedbackComponent,
    InputDescriptionComponent,
    BreadcrumbsComponent,
    NotImplementedDirective,
    DropdownSelectorComponent,
    ElementStatusComponent,
    IconComponent,
    TagComponent,
    StatusChangeComponent,
    ErrorFeedbackComponent,
    SolutionTagComponent,
    SolutionChangeComponent,
    CodeBlockComponent,
    TableHintsComponent,
    PreviewElementComponent,
    HighlightSearchPipe
  ]
})
export class SharedModule {}
