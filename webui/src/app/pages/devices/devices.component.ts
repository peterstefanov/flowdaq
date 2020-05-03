import { Component, OnInit, OnDestroy, TemplateRef, ViewChild, HostListener } from '@angular/core';
import { ColumnMode, SelectionType   } from '@swimlane/ngx-datatable';
import { Router, NavigationEnd       } from '@angular/router';
import { DeviceService             } from '../../services/api/device.service';
import { UserInfoService             } from '../../services/user-info.service';

import { Device                    } from '../../models/device';



@Component({
	selector: 'f-devices-pg',
	templateUrl: './devices.component.html',
    styleUrls: [ './devices.scss'],
})

export class DevicesComponent implements OnDestroy {

   @ViewChild('myTable', {static: false}) table: any;

    rows: any[];
    isLoading: boolean = false;
    navigationSubscription;
    expanded: any = {};
    timeout: any;
    isToggled: boolean = false;
    selected = [];;
    public customerName: string = "";
    
    ColumnMode = ColumnMode;
    SelectionType = SelectionType;
    
    constructor(private router: Router, private deviceService: DeviceService, private userInfoService: UserInfoService) {
        this.navigationSubscription = this.router.events.subscribe((e: any) => {
            // If it is a NavigationEnd event re-initalise the component
            if (e instanceof NavigationEnd) {
                this.getPageData();
            }
        });
        this.customerName = this.userInfoService.getCustomerCompany();
    }
    
    getPageData() {

        let me = this;
        me.isLoading = true;
        this.deviceService.getDevices(this.userInfoService.getCustomerId()).subscribe((data) => {
            me.rows = data.items;
            me.isLoading = false;
        });
    }

    onPage(event) {
        clearTimeout(this.timeout);
        this.timeout = setTimeout(() => {
            console.log('paged!', event);
        }, 100);
    }
        
    /**Device action*/
    editDevice(row) {
       console.log('Edit device');     
       console.log(row);       
    }     

    createDelivery(row) {
       console.log('create delivery');     
       console.log(row);  
    } 
    /**END Device action*/
    
    toggleExpandRow(row) {
        console.log('Toggled Expand Row!', row);
        this.table.rowDetail.toggleExpandRow(row);
    }

    onDetailToggle(event) {
        this.isToggled = true;
        console.log('Detail Toggled', event);
    }

    onSelect({ selected }) {
        this.selected.splice(0, this.selected.length);
        this.selected.push(...selected);
    }

    remove() {
        this.selected = [];
    }
    
    onActivate(event) {
       console.log('Activate Event', event);
       if(event.type === 'click') {
           this.table.rowDetail.toggleExpandRow(event.row);
       }    
    }
    
    ngOnDestroy() {
        if (this.navigationSubscription) {
            this.navigationSubscription.unsubscribe();
        }
    }
}