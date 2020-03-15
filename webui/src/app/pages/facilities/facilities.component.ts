import { Component, OnInit, OnDestroy, TemplateRef, ViewChild, HostListener } from '@angular/core';
import { ColumnMode, SelectionType   } from '@swimlane/ngx-datatable';
import { Router, NavigationEnd       } from '@angular/router';
import { CustomerService             } from '../../services/api/customer.service';
import { UserInfoService             } from '../../services/user-info.service';

@Component({
	selector: 'f-facilities-pg',
	templateUrl: './facilities.component.html',
    styleUrls: [ './facilities.scss'],
})

export class FacilitiesComponent implements OnDestroy {

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

    constructor(private router: Router, private customerService: CustomerService, private userInfoService: UserInfoService) {
        this.navigationSubscription = this.router.events.subscribe((e: any) => {
            // If it is a NavigationEnd event re-initalise the component
            if (e instanceof NavigationEnd) {
                this.getPageData();
            }
        });
        this.customerName = this.userInfoService.getCustomerCompany();
    }

    getPageData() {

        if (!this.isToggled) {
            let me = this;
            me.isLoading = true;
            this.customerService.getFacilities(this.userInfoService.getCustomerId()).subscribe((data) => {
                me.rows = data.items;
                me.isLoading = false;
            });
        }
    }

    onPage(event) {
        clearTimeout(this.timeout);
        this.timeout = setTimeout(() => {
            console.log('paged!', event);
        }, 100);
    }

    /**Customer action*/
    editCustomer(row) {
       console.log('edit customer');     
       console.log(row);             
    }     
    
    deleteCustomer(row) {
       console.log('delete customer');     
       console.log(row);  
    }  
       
     enableCustomer(row) {
       console.log('enableCustomer ');     
       console.log(row);           
    } 
    
    disableCustomer(row) {
       console.log('disableCustomer');     
       console.log(row);           
    } 
    
    createDelivery(row) {
       console.log('create delivery');     
       console.log(row);  
    }  
   /** END Customer action*/
            
    toggleExpandRow(row) {
        console.log('Toggled Expand Row!', row);
        this.table.rowDetail.toggleExpandRow(row);
    }

    onDetailToggle(event) {
        this.isToggled = true;
        console.log('Detail Toggled', event);
    }

    onSelect({ selected }) {
        console.log('Select Event', selected, this.selected);
        this.selected.splice(0, this.selected.length);
        this.selected.push(...selected);
    }
    
    add() {
        this.selected.push(this.rows[1], this.rows[3]);
    }

    update() {
        this.selected = [this.rows[1], this.rows[3]];
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