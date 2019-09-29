import { Component, ViewEncapsulation, ViewChild, OnInit } from '@angular/core';
import { Router,ActivatedRoute, NavigationEnd } from '@angular/router';

import { LogoComponent  } from '../../components/logo/logo.component';
import { HeaderLogoComponent  } from '../../components/headerlogo/headerlogo.component';
import { LoginService   } from '../../services/auth/login.service';
import { UserInfoService} from '../../services/user-info.service';
import { AdminService} from '../../services/api/usermanagement/admin.service';

import { Distributor} from '../../models/distributor';
import { Admin} from '../../models/admin';

import { Subscription, Observable, Observer, Subject, asapScheduler, pipe, of, from, interval, merge, fromEvent } from 'rxjs';
import { map, filter, scan, catchError, switchMap } from 'rxjs/operators';

@Component({
  selector   : 'home-comp',
  templateUrl: './home.component.html',
  styleUrls  : ['./home.component.scss'],
  encapsulation: ViewEncapsulation.None
})

export class HomeComponent {

    public adminErrorMsg: string = '';
    
    public showAppAlert: boolean = false;
    public appHeaderItems = [];

    public selectedHeaderItemIndex: number = 0;
    public userDisplayeName: string = "";
    public userRole: string = "";
    public distributorName: string = "";
    
    createDistributorObject: Distributor = {userName: '', email: '', firstName: '', lastName: '', companyName: '', role: 'distributor' } as Distributor;
    createAdminObject: Admin = {userName: '', email: '', firstName: '', lastName: '', role: 'admin' } as Admin;
    distributorCreateModal = false;
    adminCreateModal = false;
    
    constructor(
        private router: Router,
        private activeRoute: ActivatedRoute,
        private loginService: LoginService,
        private userInfoService: UserInfoService,
        private adminService: AdminService
    ) {

        this.router.events.pipe(
                filter(event => event instanceof NavigationEnd),
                map(() => this.activeRoute),
                map(route => route.firstChild),
                switchMap(route => route.data),
                map(data => data['asdf']))
            .subscribe(data => {
                this.selectedHeaderItemIndex = (data && data[0]) ? data[0].selectedHeaderItemIndex : -1;
            });

        this.userDisplayeName = this.userInfoService.getUserName();
        this.userRole = this.userInfoService.getUserRole();
        this.distributorName = this.userInfoService.getDistributorName();
        
        if (this.userRole === 'admin') {
            this.appHeaderItems = [
                {label: 'Distributors', href: '/home/distributors', subNav: []},
                {label: 'Customers', href: '/home/customers', subNav: []},
                {label: 'Locations', href: '/home/locations', subNav: []},
                {label: 'Devices', href: '/home/devices', subNav: []},
                {label: 'Deliveries', href: '/home/deliveries', subNav: []}
            ];
        } else {
             this.appHeaderItems = [
                {label: 'Customers', href: '/home/customers', subNav: []},
                {label: 'Locations', href: '/home/locations', subNav: []},
                {label: 'Devices', href: '/home/devices', subNav: []},
                {label: 'Deliveries', href: '/home/deliveries', subNav: []}
            ];
        }
    }

    navbarSelectionChange(val) {
        console.log('navbarSelectionChange');
        console.log(val);
    }

    closeAppAlert() {
        this.showAppAlert = false;
    }

    /* Distributor dialog */
    createDistributor(
        distributor: Distributor = {userName: '', email: '', firstName: '', lastName: '', companyName: '', role: 'distributor'}
    ): void {
        this.createDistributorObject = distributor;
        this.distributorCreateModal = true;
    }

    public saveDistributor(): void {
        console.log('Save dialog distributor');
        console.log(this.createDistributorObject);
        this.cancelDistributor();
    }

    public cancelDistributor(): void {
        console.log('Cancel dialog distributor');
        this.distributorCreateModal = false;
    }
    /* END Distributor dialog */
    
    /* Admin dialog */
    createAdmin(
        admin: Admin = {userName: '', email: '', firstName: '', lastName: '', role: 'admin'}
    ): void {
        this.createAdminObject = admin;
        this.adminCreateModal = true;
    }

    public saveAdmin(): void {
        this.adminService.createAdmin(this.createAdminObject)
             .subscribe(resp => {
                    if (resp.success === false) {
                        this.adminErrorMsg = resp.message;
                        return;
                    } else if (resp.success === true) {
                        this.cancelAdmin();
                        return;
                    }              
                }
            );
        
    }

    public cancelAdmin(): void {
        this.adminCreateModal = false;
    }
    /* END Admin dialog */
}