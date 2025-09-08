import { ApplicationConfig, provideBrowserGlobalErrorListeners, provideZoneChangeDetection } from '@angular/core';
import {provideRouter, withComponentInputBinding, withHashLocation} from '@angular/router';

import { provideStore } from '@ngrx/store';

import {
  AutoRefreshTokenService, provideKeycloak, UserActivityService, withAutoRefreshToken,
  INCLUDE_BEARER_TOKEN_INTERCEPTOR_CONFIG, IncludeBearerTokenCondition, includeBearerTokenInterceptor,
  createInterceptorCondition
} from 'keycloak-angular';

import { routes } from './app.routes';
import {environment} from '../environments/environment';
import {provideHttpClient, withInterceptors} from '@angular/common/http';

const devUrlCondition = createInterceptorCondition<IncludeBearerTokenCondition>({
  urlPattern: /^(http:\/\/localhost:8000)(\/.*)?$/i,
  bearerPrefix: 'Bearer'
});

const prodUrlCondition = createInterceptorCondition<IncludeBearerTokenCondition>({
  urlPattern: /^(\/.*)?$/i,
  bearerPrefix: 'Bearer'
});

export const appConfig: ApplicationConfig = {
  providers: [
    provideKeycloak({
      config: {
        clientId: environment.keycloak.clientId,
        realm: environment.keycloak.realm,
        url: environment.keycloak.server
      },
      initOptions: {
        onLoad: 'login-required'
      },
      features: [
        withAutoRefreshToken({
          onInactivityTimeout: 'login',
          sessionTimeout: 60000
        })
      ],
      providers: [AutoRefreshTokenService, UserActivityService]
    }),
    {
      provide: INCLUDE_BEARER_TOKEN_INTERCEPTOR_CONFIG,
      useValue: [devUrlCondition, prodUrlCondition]
    },
    provideBrowserGlobalErrorListeners(),
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes, withComponentInputBinding(), withHashLocation()),
    provideHttpClient(withInterceptors([includeBearerTokenInterceptor])),
    provideStore()
]
};
