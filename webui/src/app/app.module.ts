import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { RouterModule }     from '@angular/router';
import { FormsModule, ReactiveFormsModule} from '@angular/forms';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

//Third Party Modules
import { NgxDatatableModule      } from '@swimlane/ngx-datatable';
import { NgxChartsModule         } from '@swimlane/ngx-charts';
import { ClarityModule           } from '@clr/angular';
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";

//Pages 
import { HomeComponent           } from './pages/home/home.component';
import { LoginComponent          } from './pages/login/login.component';
import { ForgotPasswordComponent } from './pages/forgotpassword/forgotpassword.component';
import { ResetPasswordComponent  } from './pages/resetpassword/resetpassword.component';
import { LogoutComponent         } from './pages/logout/logout.component';
import { CustomersComponent      } from './pages/customers/customers.component';
import { DistributorsComponent   } from './pages/distributors/distributors.component';

// Services
import { AppConfig               } from './app-config';
import { AuthGuard               } from './services/auth.guard.service';
import { UserInfoService         } from './services/user-info.service';
import { ApiRequestService       } from './services/auth/api-request.service';
import { LoginService            } from './services/auth/login.service';
import { ResetPasswordService    } from './services/auth/resetpassword.service';
import { CustomerService         } from './services/api/customer.service';
import { DistributorService      } from './services/api/distributor.service';
import { AdminService            } from './services/api/usermanagement/admin.service';

// Components
import { LogoComponent           } from './components/logo/logo.component';
import { HeaderLogoComponent     } from './components/headerlogo/headerlogo.component';

@NgModule({
  declarations: [
    // Components
    LogoComponent,
    HeaderLogoComponent,
    
    AppComponent,
    HomeComponent,
    LoginComponent,
    ForgotPasswordComponent,
    ResetPasswordComponent,
    LogoutComponent,
    CustomersComponent,
    DistributorsComponent
  ],
  imports: [
  
    // Thirdparty Module
    NgxDatatableModule,
    NgxChartsModule,
    ClarityModule,
    
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    ClarityModule,
    BrowserAnimationsModule
  ],
  providers: [
    AuthGuard,
    UserInfoService,
    ApiRequestService,
    LoginService,
    AppConfig,
    CustomerService,
    ResetPasswordService,
    DistributorService,
    AdminService
    ],
  bootstrap: [AppComponent]
})
export class AppModule { }