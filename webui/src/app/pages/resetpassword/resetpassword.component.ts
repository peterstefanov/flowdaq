import { Component, OnInit } from '@angular/core';
import { LoginService } from '../../services/auth/login.service';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from "@angular/router";
import { ResetPasswordService     } from '../../services/auth/resetpassword.service';

@Component({
	selector   : 'f-login-pg',
	templateUrl: './resetpassword.component.html',
    styleUrls  : [ '../login/login.scss'],
})

export class ResetPasswordComponent implements OnInit {
    userId: string = '';
    errMsg: string = '';
    successMsg: string = '';
    validationMsg: string = '';
    resetPasswordForm: FormGroup;
    loading = false;
    submitted = false;
    code: string;

    constructor(
        private formBuilder: FormBuilder,
        private router: Router,
        private route: ActivatedRoute,
        private loginService: LoginService,
        private resetService: ResetPasswordService) {}

    ngOnInit() {
        this.resetPasswordForm = this.formBuilder.group({
            password: ['', Validators.required],
            confirmPassword: ['', Validators.required]
        });
        this.code = this.route.snapshot.paramMap.get('code');
        // reset login status
        this.loginService.logout(false);
        this.validateCode();
    }

    // convenience getter for easy access to form fields
    get f() {
        if (this.resetPasswordForm.controls.password.errors || this.resetPasswordForm.controls.confirmPassword.errors) {
            this.errMsg = '';
            this.successMsg = '';
            this.validationMsg = '';
        }
        return this.resetPasswordForm.controls;
    }

    validateCode() {
        this.errMsg = '';
        this.successMsg = '';
        this.validationMsg = '';
        /**Error is caught in the service, here just set the message to be displayed*/
        this.resetService.validateCode(this.code)
            .subscribe(resp => {
                if (resp.success === false) {
                    this.successMsg = resp.message;
                    return;
                } else {
                    this.userId = resp.userId;
                    return;
                }
            });
    }

    resetPassword() {
        this.errMsg = '';
        this.successMsg = '';
        this.validationMsg = '';
        this.submitted = true;
        // stop here if form is invalid
        if (this.resetPasswordForm.invalid) {
            return;
        }

        this.loading = true;
        /**Error is caught in the service, here just set the message to be displayed*/
        this.resetService.validatePassword(this.userId, this.f.password.value, this.code)
            .subscribe(resp => {
                if (resp.success === false) {
                    this.loading = false;
                    this.validationMsg = resp.message;
                    return;
                } else if (resp.success === true) {
                    this.loginService.getToken(this.userId, this.f.password.value)
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
                } else {
                    return;
                }
            });
    }
}