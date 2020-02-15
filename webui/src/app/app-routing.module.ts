import { NgModule                 } from '@angular/core';
import { Routes, RouterModule     } from '@angular/router';
import {APP_BASE_HREF             } from '@angular/common';

import { HomeComponent            }       from './pages/home/home.component';

import { LoginComponent           }   from './pages/login/login.component';
import { ForgotPasswordComponent  }   from './pages/forgotpassword/forgotpassword.component';
import { ResetPasswordComponent   }   from './pages/resetpassword/resetpassword.component';
import { LogoutComponent          }   from './pages/logout/logout.component';
import { CustomersComponent       }   from './pages/customers/customers.component';
import { DistributorsComponent     }   from './pages/distributors/distributors.component';

import { AuthGuard                } from './services/auth.guard.service';
import { PageNotFoundComponent    }   from './pages/404/page-not-found.component';

const routes: Routes = [
    {path: '', redirectTo: '/home/customers', pathMatch: 'full', data:[{selectedHeaderItemIndex:0, selectedSubNavItemIndex:-1}] },
    {path: 'home', component: HomeComponent, canActivate: [AuthGuard],
        children: [ // Children paths are appended to the parent path
            {path: 'distributors',component: DistributorsComponent, data: [{selectedHeaderItemIndex: 0, selectedSubNavItemIndex: -1}]},
            {path: 'customers',component: CustomersComponent, data: [{selectedHeaderItemIndex: 1, selectedSubNavItemIndex: -1}]},
            {path: 'facilities',component: CustomersComponent, data: [{selectedHeaderItemIndex: 2, selectedSubNavItemIndex: -1}]},
            {path: 'devices',component: CustomersComponent, data: [{selectedHeaderItemIndex: 3, selectedSubNavItemIndex: -1}]},
            {path: 'deliveries',component: CustomersComponent, data: [{selectedHeaderItemIndex: 4, selectedSubNavItemIndex: -1}]},
        ]
    },

    {path: 'not-found', component: PageNotFoundComponent},
    { path: 'login', component: LoginComponent, data:[{selectedHeaderItemIndex:-1, selectedSubNavItemIndex:-1}] },
    { path: 'forgotpassword', component: ForgotPasswordComponent, data:[{selectedHeaderItemIndex:-1, selectedSubNavItemIndex:-1}] },
    { path: 'resetpassword/:code', component: ResetPasswordComponent, data:[{selectedHeaderItemIndex:-1, selectedSubNavItemIndex:-1}] },
    { path: 'logout', component: LogoutComponent, data:[{selectedHeaderItemIndex:-1, selectedSubNavItemIndex:-1}] },
    { path: '**', component: PageNotFoundComponent, data:[{selectedHeaderItemIndex:-1, selectedSubNavItemIndex:-1}] }

];

@NgModule({
    imports: [RouterModule.forRoot(routes, {useHash:true})],
    exports: [RouterModule],
    declarations: [PageNotFoundComponent]
})
export class AppRoutingModule {}