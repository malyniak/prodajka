import {Component, OnInit} from '@angular/core';
import {forkJoin, of} from "rxjs";
import {PhoneService} from "./services/phone.service";
import {PhoneDto} from "../dto/PhoneDto";

import {map, switchMap} from "rxjs/operators";

@Component({
  selector: 'app-phone',
  templateUrl: './phone.component.html',
  styleUrls: ['./phone.component.scss']
})
export class PhoneComponent implements OnInit {
  phones: PhoneDto[] = [];


  constructor(
    protected phoneService: PhoneService
  ) {}

  ngOnInit(): void {
    this.phoneService.getPhones().pipe(
      switchMap((phones: PhoneDto[]) => {
       const req = phones.map(phone => {
          if(phone.imageUrl) {
           return  this.phoneService.getPhoneImage(phone.imageUrl).pipe(
              map(safeUrl => ({...phone, safeImageUrl: safeUrl}))
            );
          } else {
            return of(phone)
          }
        })
       return forkJoin(req)
      })
    ).subscribe((phones: PhoneDto[]) => {
      this.phones = phones;
    });
  }
}
