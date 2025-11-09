package com.panaderia.inventario.config;

import com.panaderia.inventario.model.Usuario;
import com.panaderia.inventario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class InitData implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (usuarioRepository.count() == 0) {
            Usuario user = new Usuario();
            user.setEmail("admin@correo.com");
            user.setHash(passwordEncoder.encode("1234"));
            user.setRol("ADMIN");

            usuarioRepository.save(user);

            System.out.println("✅ Usuario admin creado desde InitData");
        } else {
            System.out.println("ℹ️ Usuarios ya existen, no se crea ninguno nuevo");
        }
    }
}
