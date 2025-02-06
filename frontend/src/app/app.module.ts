import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { CategoryPanelComponent } from './category-panel/category-panel.component';
import { LaptopComponent } from './laptop/laptop.component';
import { PhoneComponent } from './phone/phone.component';
import { TvComponent } from './tv/tv.component';
import {HttpClientModule} from "@angular/common/http";
import { NavbarComponent } from './sharable/navbar/navbar.component';
import { PhoneDetailsComponent } from './phone-details/phone-details.component';

@NgModule({
  declarations: [
    AppComponent,
    CategoryPanelComponent,
    LaptopComponent,
    PhoneComponent,
    TvComponent,
    NavbarComponent,
    PhoneDetailsComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
