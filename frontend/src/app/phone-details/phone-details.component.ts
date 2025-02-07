import { Component, OnInit } from '@angular/core';
import {PhoneService} from "../phone/services/phone.service";
import {PhoneDto} from "../dto/PhoneDto";
import {ActivatedRoute} from "@angular/router";
import {forkJoin, Observable, of} from "rxjs";
import {map, switchMap} from "rxjs/operators";

@Component({
  selector: 'app-phone-details',
  templateUrl: './phone-details.component.html',
  styleUrls: ['./phone-details.component.scss']
})
export class PhoneDetailsComponent implements OnInit {
  phone!: Observable<PhoneDto>;
  phones: PhoneDto[] = [];
  stars = new Array(5);
  isDetails: boolean = false;

  constructor(
    protected phoneService: PhoneService,
    private route: ActivatedRoute,
  ) {}

    ngOnInit(): void {
        let phoneId = this.route.snapshot.paramMap.get('id');
        if (phoneId != null) {
            this.phone = this.phoneService.getPhone(phoneId).pipe(
                map((phone: PhoneDto) => {
                    if (phone.imageUrl) {
                        return this.phoneService.getPhoneImage(phone.imageUrl).pipe(
                            map(safeUrl => ({ ...phone, safeImageUrl: safeUrl }))
                        );
                    }
                    return of(phone); // Якщо немає картинки, повертаємо Observable із самим об'єктом телефону
                }),
                switchMap(obs => obs) // "Розгортає" вкладений Observable (flatMap/concatMap)
            );

            this.phone.subscribe((phone: PhoneDto) => {
                console.log(phone);
            });
        }
    }

    changeIsDetails() {
      this.isDetails = !this.isDetails;
    }

}
