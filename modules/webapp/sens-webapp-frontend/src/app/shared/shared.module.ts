import { HttpClient, HttpClientModule } from '@angular/common/http';
import { ErrorHandler, NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { RouterModule } from '@angular/router';
import { FeedbackMessageComponent } from '@app/components/feedback-message/feedback-message.component';
import { IconComponent } from '@app/components/icon/icon.component';
import { LoadingLoopComponent } from '@app/components/loading-loop/loading-loop.component';
import { LoadingComponent } from '@app/components/loading/loading.component';
import { OverlayComponent } from '@app/components/overlay/overlay.component';
import { CommunicationErrorComponent } from '@app/components/popup-window/communication-error.component';
import { PreloaderComponent } from '@app/components/preloader/preloader.component';
import { ActivityMonitorModule } from '@app/shared/activity-monitor/activity-monitor.module';
import { AuthModule } from '@app/shared/security/auth.module';
import {
  BackgroundServicesManager,
  BackgroundServiceToken
} from '@app/shared/background-services-manager';
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
    LoadingComponent,
    PreloaderComponent,
    LoadingLoopComponent,
    CommunicationErrorComponent,
    OverlayComponent,
    FeedbackMessageComponent,
    NotImplementedDirective,
    IconComponent,
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
    TranslateServiceWrapper,
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
    LoadingLoopComponent,
    CommunicationErrorComponent,
    OverlayComponent,
    FeedbackMessageComponent,
    NotImplementedDirective,
    IconComponent,
  ]
})
export class SharedModule {}
