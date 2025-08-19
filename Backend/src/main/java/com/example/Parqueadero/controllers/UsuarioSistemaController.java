//
//package com.example.Parqueadero.controllers;
//
//import com.example.Parqueadero.entities.UsuarioSistema;
//import com.example.Parqueadero.service.UsuarioSistemaService;
//import java.util.List;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/usuarios")
//public class UsuarioSistemaController {
//
//    private final UsuarioSistemaService service;
//
//    public UsuarioSistemaController(UsuarioSistemaService service) {
//        this.service = service;
//    }
//
//    @PostMapping("/crear")
//
//    public ResponseEntity<UsuarioSistema> crear(@RequestBody UsuarioSistema usuario) {
//        return ResponseEntity.ok(service.crearUsuario(usuario));
//    }
//
//    @GetMapping 
//    public ResponseEntity<List<UsuarioSistema>> listar() {
//        return ResponseEntity.ok(service.obtenerTodos());
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<UsuarioSistema> obtenerPorId(@PathVariable Long id) {
//        return service.obtenerPorId(id)
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<UsuarioSistema> actualizar(@PathVariable Long id, @RequestBody UsuarioSistema usuario) {
//        return ResponseEntity.ok(service.actualizarUsuario(id, usuario));
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
//        service.eliminarUsuario(id);
//        return ResponseEntity.noContent().build();
//    }
//}