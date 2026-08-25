import { Component, OnInit } from '@angular/core';
import { UsuariosResponse } from '../../../models/Usario.model';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-usuarios',
  standalone: false,
  templateUrl: './usuarios.component.html',
  styleUrl: './usuarios.component.css'
})
export class UsuariosComponent implements OnInit {

  usuarios: UsuariosResponse[] = [];

  ngOnInit(): void {
    this.usuarios = [
      {
        username: 'Usuarioxd',
        roles: ['ROLE_ADMIN']
      },
      {
        username: 'Administrador',
        roles: ['ROLE_ADMIN']
      },
      {
        username: 'Usuario',
        roles: ['ROLE_ADMIN']
      },

    ];
    console.info('Usuarios: ', this.usuarios)
  }

  eliminarUsuario(username: string): void{
    Swal.fire({
      title: '¿Estas seguro?',
      text: `El usuario $(username) sera eliminado permanentemente`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Si, eliminar',
      cancelButtonText: 'Cancelar'
    }).then(result => {
      if(result.isConfirmed){
        this.usuarios = this.usuarios.filter(u => u.username !== username)
      }
    })
  }

}
