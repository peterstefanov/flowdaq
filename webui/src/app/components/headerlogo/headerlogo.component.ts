import { Component, Input } from '@angular/core';
@Component({
	selector: 'hf-logo',
	templateUrl: './headerlogo.component.html'
})

export class HeaderLogoComponent{
  @Input() fontColor = "#63666A";
  @Input() public iconColor: string = "#E3642B";
}
