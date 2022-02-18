package com.example.MejoraCrud.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.Optional;

import com.example.MejoraCrud.models.UsuarioModel;
import com.example.MejoraCrud.services.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


@RestController
@RequestMapping("/usuario")
public class UsuarioController {
    @Autowired
    UsuarioService usuarioService;

    @GetMapping()
    public ArrayList<UsuarioModel> obtenerUsuarios(){
        return usuarioService.obtenerUsuarios();
    }

    @PostMapping()
    public ResponseEntity<UsuarioModel> guardarUsuario(@RequestBody UsuarioModel usuario) {
        Long usuarioid = usuario.getId();

        // se busca solucion a conflicto cuando un usuario viene con id incorporado
        if (usuarioid != null) {
            Optional<UsuarioModel> usuarioOpcional = this.usuarioService.obtenerPorId(usuarioid);

            if (usuarioOpcional.isPresent()) {
                // ya existe el usuario con ese id, devolver error 409
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }

            // si tiene id, pero no existe usuario, continuar creando
            // el sistema lo crea con un nuevo id sin importar el incorporado
        }

        UsuarioModel nuevousuario = this.usuarioService.guardarUsuario(usuario);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(nuevousuario.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping(path = "/{id}")
    public UsuarioModel modificarUsuario(@PathVariable("id") Long uid, @RequestBody UsuarioModel usuario){
        usuario.setId(uid);
        UsuarioModel usuarioModel = this.usuarioService.guardarUsuario(usuario);
        return usuarioModel;
    }

    @GetMapping( path = "/{id}")
    public Optional<UsuarioModel> obtenerUsuarioPorId(@PathVariable("id") Long id) {
        return this.usuarioService.obtenerPorId(id);
    }

    @GetMapping("/query")
    public ArrayList<UsuarioModel> obtenerUsuarioPorPrioridad(@RequestParam("prioridad") Integer prioridad){
        return this.usuarioService.obtenerPorPrioridad(prioridad);
    }

    @DeleteMapping( path = "/{id}")
    public String eliminarPorId(@PathVariable("id") Long id){
        boolean ok = this.usuarioService.eliminarUsuario(id);
        if (ok){
            return "Se eliminó el usuario con id " + id;
        }else{
            return "No pudo eliminar el usuario con id" + id;
        }
    }

}