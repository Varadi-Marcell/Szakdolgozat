import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RegisterComponent } from "./register/register.component";
import { DrawComponent } from "./draw/draw.component";
import { UserGuard } from "./service/user.guard";
import { HighscoreComponent } from "./highscore/highscore.component";

const routes: Routes = [
  {
    path: 'register',
    component: RegisterComponent
  },
  {
    path: 'draw',
    component: DrawComponent,
    canActivate: [UserGuard]
  },
  {
    path: 'highscore',
    component: HighscoreComponent
  },
  {
    path: '**',
    pathMatch: 'full',
    redirectTo: 'register'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: true})],
  exports: [RouterModule]
})
export class AppRoutingModule {


}
