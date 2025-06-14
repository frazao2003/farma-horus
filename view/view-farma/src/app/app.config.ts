import { ApplicationConfig, importProvidersFrom, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { provideClientHydration } from '@angular/platform-browser';
import { provideAnimations } from '@angular/platform-browser/animations';
import { provideToastr, ToastrModule } from 'ngx-toastr';
import { provideHttpClient } from '@angular/common/http';


export const appConfig: ApplicationConfig = {
  providers: [provideZoneChangeDetection({ eventCoalescing: true }), 
    provideRouter(routes), 
    provideClientHydration(), 
    provideAnimations(),
    provideToastr(),
    provideHttpClient(),
    importProvidersFrom(ToastrModule.forRoot({
      positionClass: 'toast-top-right',
      timeOut: 4000,
      progressBar: true,
      closeButton: true,
    })), // ✅ fornece HttpClient para o SSR
  ]
};
