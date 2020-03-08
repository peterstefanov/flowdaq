import { Component, ViewEncapsulation, ViewChild, OnInit } from '@angular/core';
import { Router,ActivatedRoute, NavigationEnd            } from '@angular/router';

import { LogoComponent                                   } from '../../components/logo/logo.component';
import { HeaderLogoComponent                             } from '../../components/headerlogo/headerlogo.component';
import { LoginService                                    } from '../../services/auth/login.service';
import { UserInfoService                                 } from '../../services/user-info.service';
import { AdminManagementService                          } from '../../services/api/usermanagement/adminmanagement.service';
import { DistributorManagementService                    } from '../../services/api/usermanagement/distributormanagement.service';
import { CustomerManagementService                       } from '../../services/api/usermanagement/customermanagement.service';

import { Distributor                                     } from '../../models/distributor';
import { Admin                                           } from '../../models/admin';
import { Customer                                        } from '../../models/customer';

import { Subscription, Observable, Observer, Subject, asapScheduler, pipe, of, from, interval, merge, fromEvent } from 'rxjs';
import { map, filter, scan, catchError, switchMap        } from 'rxjs/operators';

@Component({
  selector   : 'home-comp',
  templateUrl: './home.component.html',
  styleUrls  : ['./home.component.scss'],
  encapsulation: ViewEncapsulation.None
})

export class HomeComponent {

    public adminErrorMsg: string = '';
    public adminSuccessMsg: string = '';

    public distributorErrorMsg: string = '';
    public distributorSuccessMsg: string = '';
    
    public customerErrorMsg: string = '';
    public customerSuccessMsg: string = '';
    
    public appHeaderItems = [];

    public selectedHeaderItemIndex: number = 0;
    public userDisplayeName: string = "";
    public userRole: string = "";
    public distributorName: string = "";
    
    createCustomerObject: Customer = {distributorId: 0,id: 0, userName: '', email: '', firstName: '', lastName: '', contact: '', altContact: '', enabled: true, phoneNumber: '', companyName: '', addressId: 0, addressLine1: '', addressLine2: '',addressLine3: '', city: '', state: '', country: '', postalCode: '', role: 'customer' } as Customer;
    createDistributorObject: Distributor = {id: 0, userName: '', email: '', firstName: '', lastName: '', enabled: true, phoneNumber: '', companyName: '', addressId: 0, addressLine1: '', addressLine2: '',addressLine3: '', city: '', state: '', country: '', postalCode: '', role: 'distributor' } as Distributor;
    createAdminObject: Admin = {userName: '', email: '', firstName: '', lastName: '', phoneNumber: '', role: 'admin' } as Admin;
    
    customerCreateModal = false;
    distributorCreateModal = false;
    adminCreateModal = false;
    
    constructor(
        private router: Router,
        private activeRoute: ActivatedRoute,
        private loginService: LoginService,
        private userInfoService: UserInfoService,
        private adminService: AdminManagementService,
        private distributorService: DistributorManagementService,
        private customerService: CustomerManagementService
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
                {label: 'Facilities', href: '/home/facilities', subNav: []},
                {label: 'Devices', href: '/home/devices', subNav: []},
                {label: 'Deliveries', href: '/home/deliveries', subNav: []}
            ];
        } else if (this.userRole === 'distributor') {
            this.appHeaderItems = [  
                {label: 'Customers', href: '/home/customers', subNav: []},
                {label: 'Deliveries', href: '/home/deliveries', subNav: []}
            ];
        } else if (this.userRole === 'customer') {
            this.appHeaderItems = [  
                {label: 'Facilities', href: '/home/facilities', subNav: []},
                {label: 'Deliveries', href: '/home/deliveries', subNav: []}
            ];
        }else {
             this.appHeaderItems = [
                {label: 'Customers', href: '/home/customers', subNav: []},
                {label: 'Facilities', href: '/home/locations', subNav: []},
                {label: 'Devices', href: '/home/devices', subNav: []},
                {label: 'Deliveries', href: '/home/deliveries', subNav: []}
            ];
        }
    }

    navbarSelectionChange(val) {
        console.log('navbarSelectionChange');
        console.log(val);
    }
    
        
    /* Facility dialog */
    createFacility(): void {
        console.log("FACILITY create");
    }

    /* END Facility dialog */
    
    /* Customer dialog */
    createCustomer(
        customer: Customer = {distributorId: this.userInfoService.getDistributorId(), id: 0, userName: '', email: '', firstName: '', lastName: '', contact: '', altContact: '', enabled: true, phoneNumber: '', companyName: '', addressId: 0,  addressLine1: '', addressLine2: '',addressLine3: '', city: '', state: '', country: '', postalCode: '', role: 'customer'}
    ): void {
        this.createCustomerObject = customer;
        this.customerCreateModal = true;
    }

    public saveCustomer(): void {      
        this.customerService.createCustomer(this.createCustomerObject)
             .subscribe(resp => {
                    if (resp.success === false) {
                        this.customerErrorMsg = resp.message;
                        return;
                    } else if (resp.success === true) {
                        this.cancelCustomer();
                        this.customerSuccessMsg = resp.message;
                        return;
                    }              
                }
            );
    }

    public cancelCustomer(): void {
        this.customerCreateModal = false;
    }
    
    public closeCustomerSuccess(): void {
        this.customerSuccessMsg = '';
    }
    /* END Customer dialog */
    

    /* Distributor dialog */
    createDistributor(
        distributor: Distributor = {id: 0, userName: '', email: '', firstName: '', lastName: '', enabled: true, phoneNumber: '', companyName: '', addressId: 0,  addressLine1: '', addressLine2: '',addressLine3: '', city: '', state: '', country: '', postalCode: '', role: 'distributor'}
    ): void {
        this.createDistributorObject = distributor;
        this.distributorCreateModal = true;
    }

    public saveDistributor(): void {      
        this.distributorService.createDistributor(this.createDistributorObject)
             .subscribe(resp => {
                    if (resp.success === false) {
                        this.distributorErrorMsg = resp.message;
                        return;
                    } else if (resp.success === true) {
                        this.cancelDistributor();
                        this.distributorSuccessMsg = resp.message;
                        return;
                    }              
                }
            );
    }

    public cancelDistributor(): void {
        this.distributorCreateModal = false;
    }
    
    public closeDistributorSuccess(): void {
        this.distributorSuccessMsg = '';
    }
    /* END Distributor dialog */
    
    /* Admin dialog */
    createAdmin(
        admin: Admin = {userName: '', email: '', firstName: '', lastName: '', phoneNumber: '', role: 'admin'}
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
                        this.adminSuccessMsg = resp.message;
                        return;
                    }              
                }
            );
        
    }

    public cancelAdmin(): void {
        this.adminCreateModal = false;
    }
    
    public closeAdminSuccess(): void {
        this.adminSuccessMsg = '';
    }
    /* END Admin dialog */
}