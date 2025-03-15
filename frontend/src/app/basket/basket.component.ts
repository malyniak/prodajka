import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { BasketService } from '../services/basket.service';
import { BehaviorSubject, Observable } from 'rxjs';
import { UserBasketDto } from '../dto/UserBasketDto';
import { CommonModule } from '@angular/common';
import { ProductService } from '../services/product.service';
import { SafeUrl } from '@angular/platform-browser';
import { count } from 'console';

@Component({
  selector: 'app-basket',
  templateUrl: './basket.component.html',
  styleUrls: ['./basket.component.scss']

})
export class BasketComponent implements OnInit {

  basket$ = new BehaviorSubject<UserBasketDto | null>(null);
  productImagesMap$ = new Map<string, Observable<SafeUrl>>()

  constructor(private basketService: BasketService, private cd: ChangeDetectorRef, private productService: ProductService) { }
  ngOnInit(): void {
    console.log('INIT');
    this.basketService.getBasket().subscribe(data => {
      console.log('Basket:', data);
      this.basket$.next(data);

      data.basketItems.forEach(product => {
        this.productImagesMap$.set(product.id, this.productService.getImage(product.imageUrl));
      });
    });
  }
  increase(productId: string) {
    const basket = this.basket$.getValue();
    if (!basket) return; 
  
    const updatedBasketItems = basket.basketItems.map(product => 
      product.id === productId ? { ...product, count: Number(product.count) + 1 } : product
    );
    this.basketService.increaseCount(productId, count)
    this.basket$.next({ ...basket, basketItems: updatedBasketItems });
    console.log(basket.id)
  }

}
