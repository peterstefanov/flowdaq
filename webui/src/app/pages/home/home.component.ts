import { Component, ViewEncapsulation, ViewChild, OnInit } from '@angular/core';
import { Router,ActivatedRoute, NavigationEnd } from '@angular/router';

import { LogoComponent  } from '../../components/logo/logo.component';
import { HeaderLogoComponent  } from '../../components/headerlogo/headerlogo.component';
import { LoginService   } from '../../services/auth/login.service';
import { UserInfoService} from '../../services/user-info.service';

import { Distributor} from '../../models/distributor';

import { Subscription, Observable, Observer, Subject, asapScheduler, pipe, of, from, interval, merge, fromEvent } from 'rxjs';
import { map, filter, scan, catchError, switchMap } from 'rxjs/operators';

@Component({
  selector   : 'home-comp',
  templateUrl: './home.component.html',
  styleUrls  : ['./home.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class HomeComponent   {

    public showAppAlert:boolean = false;
    public appHeaderItems=[
        { label: 'Customers'   , href: '/home/customers'    , subNav: []},
        { label: 'Locations' , href: '/home/locations'  , subNav: []},
        { label: 'Devices', href: '/home/devices' , subNav: []},
        { label: 'Deliveries', href: '/home/deliveries' , subNav: []}
    ];

    public selectedHeaderItemIndex:number=0;
    public userName: string="";
    public userRole: string="";
    
    create: Distributor = { userName: '', email: '', firstName: '', lastName: '', companyName: '', role: 'user' } as Distributor;
    distributorCreateModal = false;
    
    constructor(
        private router:Router,
        private activeRoute:ActivatedRoute,
        private loginService:LoginService,
        private userInfoService:UserInfoService
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
        
        this.userName = this.userInfoService.getUserName();
        this.userRole = this.userInfoService.getUserRole();

    }

    navbarSelectionChange(val){
         console.log('navbarSelectionChange');
         console.log(val);
    }

    closeAppAlert(){
        this.showAppAlert=false;
    }

    createDistributor(
        distributor: Distributor = { userName: '', email: '', firstName: '', lastName: '', companyName: '', role: 'user' }
    ): void {
      this.create = distributor;
      this.distributorCreateModal = true;
    }
    
   /**
   * Save the currently edited category
   */
   public save(): void {
       console.log('Save dialog');
       console.log(this.create);
       this.cancel();     
   }

   /**
   * Close the modal and reset error states
   */
  public cancel(): void {
    console.log('Cancel dialog');
    this.distributorCreateModal = false;
  }
}
