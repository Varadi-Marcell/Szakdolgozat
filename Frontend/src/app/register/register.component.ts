import { Component, OnInit } from '@angular/core';
import { Router } from "@angular/router";
import { FormBuilder, FormControl, FormGroup, Validators } from "@angular/forms";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  form: FormGroup;

  constructor(private readonly fb: FormBuilder,
              private readonly router: Router) {
  }

  ngOnInit(): void {
    this.form = this.fb.group({
      name: new FormControl('',
        [Validators.required,
        Validators.minLength(1),
        Validators.maxLength(10)])
    });
  }

  connect() {
    sessionStorage.setItem('user',
      JSON.stringify({name: this.form.value.name}));
    this.router.navigateByUrl('/draw');
  }
}
