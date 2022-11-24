import { BrowserModule } from '@angular/platform-browser';

import { APP_INITIALIZER, NgModule } from '@angular/core';

import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { CanvasComponent } from './canvas.component';
import { RegisterComponent } from './register/register.component';
import { DrawComponent } from './draw/draw.component';
import { InitService } from "./service/init.service";
import { HighscoreComponent } from './highscore/highscore.component';

export function initServiceProvideFactory(initService: InitService) {
  return async () => initService.init();
}

@NgModule({
  declarations: [
    AppComponent,
    CanvasComponent,
    RegisterComponent,
    DrawComponent,
    HighscoreComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    AppRoutingModule

  ],
  providers: [
      {
        provide: APP_INITIALIZER,
        useFactory: initServiceProvideFactory,
        deps: [InitService],
        multi: true
      }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
