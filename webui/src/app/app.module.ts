import { BrowserModule                 } from '@angular/platform-browser';
import { NgModule                      } from '@angular/core';
import { HttpClientModule              } from '@angular/common/http';
import { RouterModule                  }     from '@angular/router';
import { FormsModule, ReactiveFormsModule} from '@angular/forms';

import { AppRoutingModule              } from './app-routing.module';
import { AppComponent                  } from './app.component';

//Third Party Modules
import { NgxDatatableModule            } from '@swimlane/ngx-datatable';
import { NgxChartsModule               } from '@swimlane/ngx-charts';
import { ClarityModule                 } from '@clr/angular';
import { BrowserAnimationsModule       } from "@angular/platform-browser/animations";

//Pages 
import { HomeComponent                 } from './pages/home/home.component';
import { LoginComponent                } from './pages/login/login.component';
import { ForgotPasswordComponent       } from './pages/forgotpassword/forgotpassword.component';
import { ResetPasswordComponent        } from './pages/resetpassword/resetpassword.component';
import { LogoutComponent               } from './pages/logout/logout.component';
import { CustomersComponent            } from './pages/customers/customers.component';
import { DistributorsComponent         } from './pages/distributors/distributors.component';
import { FacilitiesComponent           } from './pages/facilities/facilities.component';
import { DevicesComponent              } from './pages/devices/devices.component';
import { DeliveriesComponent           } from './pages/deliveries/deliveries.component';


// Services
import { AppConfig                     } from './app-config';
import { AuthGuard                     } from './services/auth.guard.service';
import { UserInfoService               } from './services/user-info.service';
import { ApiRequestService             } from './services/auth/api-request.service';
import { LoginService                  } from './services/auth/login.service';
import { ResetPasswordService          } from './services/auth/resetpassword.service';
import { CustomerService               } from './services/api/customer.service';
import { DistributorService            } from './services/api/distributor.service';
import { AdminManagementService        } from './services/api/usermanagement/adminmanagement.service';
import { DistributorManagementService  } from './services/api/usermanagement/distributormanagement.service';
import { CustomerManagementService     } from './services/api/usermanagement/customermanagement.service';
import { FacilityManagementService     } from './services/api/usermanagement/facilitymanagement.service';
import { DeviceService                 } from './services/api/device.service';
import { DeliveryService               } from './services/api/delivery.service';

// Components
import { LogoComponent                 } from './components/logo/logo.component';
import { HeaderLogoComponent           } from './components/headerlogo/headerlogo.component';

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
    DistributorsComponent,
    FacilitiesComponent,
    DevicesComponent,
    DeliveriesComponent
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
    AdminManagementService,
    DistributorManagementService,
    CustomerManagementService,
    FacilityManagementService,
    DeviceService,
    DeliveryService
    ],
  bootstrap: [AppComponent]
})
export class AppModule { }