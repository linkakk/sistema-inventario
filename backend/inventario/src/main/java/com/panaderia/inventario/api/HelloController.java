package com.panaderia.inventario.api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/prueba")
    public String home() {
        return "Ruta / aaaaaaASASASaaaaasdasprueba funcionando la nueva dependencia";
    }
}
