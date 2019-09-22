import { Component, OnInit } from '@angular/core';
import { LoginService } from '../../services/auth/login.service';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
	selector   : 'f-login-pg',
	templateUrl: './login.component.html',
    styleUrls  : [ './login.scss'],
})

export class LoginComponent implements OnInit {
    errMsg: string = '';
    loginForm: FormGroup;
    loading = false;
    submitted = false;
    constructor(
        private formBuilder: FormBuilder,
        private router: Router,
        private loginService: LoginService) {}

    ngOnInit() {
        this.loginForm = this.formBuilder.group({
            username: ['', Validators.required],
            password: ['', Validators.required]
        });
        this.loginService.logout(false);
    }


    // convenience getter for easy access to form fields
    get f() {
        if (this.loginForm.controls.password.errors || this.loginForm.controls.username.errors) {
            this.errMsg = '';
        }
        return this.loginForm.controls;
    }

    login() {
        this.errMsg = '';
        this.submitted = true;
        // stop here if form is invalid
        if (this.loginForm.invalid) {
            return;
        }

        this.loading = true;
        this.loginService.getToken(this.f.username.value, this.f.password.value)
            .subscribe(resp => {
                if (resp.success === false) {
                    this.loading = false;
                    this.errMsg = resp.message;
                    return;
                }
                if (resp.user === undefined || resp.user.token === undefined || resp.user.token === "INVALID") {
                    this.errMsg = '';
                    this.loading = false;
                    return;
                }

                this.router.navigate([resp.landingPage]);
            });
    }
}