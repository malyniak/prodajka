import { SafeUrl } from "@angular/platform-browser"


export interface UserBasketDto {
    id: string,
    basketItems: BasketProductDto[]
}

export interface BasketProductDto {
    id: string,
    productId: string,
    fullName: string,
    price: number,
    imageUrl: string,
    count: number
}