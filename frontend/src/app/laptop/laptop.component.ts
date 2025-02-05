import { Component, OnInit } from '@angular/core';

import {Observable} from "rxjs";
import {ProductDto} from "../dto/ProductDto";
import {ProductService} from "../services/product.service";

@Component({
  selector: 'app-laptop',
  templateUrl: './laptop.component.html',
  styleUrls: ['./laptop.component.scss']
})
export class LaptopComponent implements OnInit {

  laptops$!: Observable<ProductDto[]>;


  constructor(private laptopService: ProductService) {

  }

  getAll(): Observable<ProductDto[]> {
    let products = this.laptopService.getProducts().subscribe(
      data => console.log(console.log(data))
    );

   return this.laptopService.getProducts();
  }


  ngOnInit(): void {
    this.laptops$ = this.getAll();
  }

}
