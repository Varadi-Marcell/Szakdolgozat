import { Injectable } from '@angular/core';
import { Router } from "@angular/router";

@Injectable({
  providedIn: 'root'
})
export class InitService {

  constructor(private readonly router: Router) {
  }
  init() {
    sessionStorage.removeItem('user');
    this.router.navigateByUrl('/register');
  }
}
