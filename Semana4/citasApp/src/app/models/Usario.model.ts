export interface UsuarioRequest{
    username: string,
    password: string,
    roles: string[]
}

export interface UsuariosResponse{
    username: string,
    roles: string[]
}

