import { AfterViewInit, Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  standalone: false,
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'citasApp';
  /*otra: string = 'Otro valor';
  contador: number = 0;
  limite: boolean = false;
  
  ngOnInit(): void {
    alert('Se esta iniciando el componente');
  }
  
  ngAfterViewInit(): void {
    alert('Se ha renderizado la vista del componente');
  }

  aumentarContador(): void{
    if(!this.limite){
      this.contador++;
    }
    this.limite = this.contador >  10? true : false;
  }*/

}
