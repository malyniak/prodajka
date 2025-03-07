import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {LaptopComponent} from "./laptop/laptop.component";
import {CategoryPanelComponent} from "./category-panel/category-panel.component";
import {PhoneComponent} from "./phone/phone.component";
import {TvComponent} from "./tv/tv.component";
import {PhoneDetailsComponent} from "./phone-details/phone-details.component";
import {AuthComponent} from "./auth/auth.component";
import {LoginCallbackComponent} from "./auth/login-callback/login-callback.component";
import {RegisterUserComponent} from "./register-user/register-user.component";


const routes: Routes = [
  { path: '',
    component: CategoryPanelComponent },
  {
    path: 'laptops',
    component: LaptopComponent
  },
  {
    path: `phone/:id`,
    component: PhoneDetailsComponent
  },

  {
    path: 'phones',
    component: PhoneComponent,
  },

  {
    path: 'tvs',
    component: TvComponent
  },

  {
    path: 'signin',
    component: AuthComponent
  },

  {
    path: 'login-callback',
    component: LoginCallbackComponent,
    pathMatch: 'full',
  },

  {
    path: 'registration',
    component: RegisterUserComponent
  },

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
