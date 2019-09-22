import { Component, OnInit } from '@angular/core';
import { LoginService } from '../../services/auth/login.service';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
	selector   : 'f-login-pg',
	templateUrl: './forgotpassword.component.html',
    styleUrls  : [ '../login/login.scss'],
})

export class ForgotPasswordComponent implements OnInit {
    errMsg:string = '';
    successMsg:string = '';
    forgotPasswordForm: FormGroup;
    loading = false;
    submitted = false;
    constructor(
        private formBuilder: FormBuilder,
        private router: Router,
        private loginService: LoginService) { }
    
    ngOnInit() {
        this.forgotPasswordForm = this.formBuilder.group({
            email: ['', Validators.required]
        });
        // reset login status
        this.loginService.logout(false);
    }
      
    // convenience getter for easy access to form fields
    get f() { 
        if(this.forgotPasswordForm.controls.email.errors) {
            this.errMsg = '';
            this.successMsg = '';
        }
        return this.forgotPasswordForm.controls; 
    }
    
    forgotPassword() {
        this.errMsg = '';
        this.successMsg = '';
        this.submitted = true;
        // stop here if form is invalid
        if (this.forgotPasswordForm.invalid) {
            return;
        }
        
        this.loading = true;
        /**Error is caught in the service, here just set the message to be displayed*/
        this.loginService.requestResetPassword(this.f.email.value)
            .subscribe(resp => {
                    if (resp.success === false) {
                        this.loading = false;
                        this.errMsg = resp.message;
                        return;
                    } else {
                        this.successMsg = resp.message;
                        this.loading = false;
                        return;
                    }              
                }
            );
    }
}
