package com.panaderia.inventario.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import com.panaderia.inventario.model.Usuario;
import com.panaderia.inventario.repository.UsuarioRepository;
import com.panaderia.inventario.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;


/**
 * AuthController (contrato listo, lógica se agrega en el siguiente paso).
 * Endpoints:
 *  - POST /api/v1/auth/login   : recibe email+password y devolverá (access, refresh).
 *  - POST /api/v1/auth/refresh : recibe refresh y devolverá un nuevo access.
 *  - GET  /api/v1/auth/me      : devolverá datos del usuario (a partir del JWT).
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

private final UsuarioRepository usuarioRepository;
private final PasswordEncoder passwordEncoder;
private final JwtService jwtService;

// Constructor para inyectar dependencias
public AuthController(UsuarioRepository usuarioRepository,
                      PasswordEncoder passwordEncoder,
                      JwtService jwtService) {
    this.usuarioRepository = usuarioRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
}


    

      // ====== DTOs pequeños y claros (en el mismo archivo para verlo todo fácil) ======
    //Creamos el record para que sea inmutable
    //ponemos las anotaciones @NotBlank para que los campos no permitan estar vacios
    //@Email esta permite que este campo se de tipo correo
    public record LoginRequest(@NotBlank @Email String email,@NotBlank String password){}
    /*
     * lo que la API devolvera cuadno el login o el refresh  sale un 
     * accessToken:Este es un acceso de tiempo corto de 15 - 30 minutos
     * no se acostumbra a almacenarse
     * refreshToken:este se almacena y tiene una duracion de 7 - 30 dias
     * 
     */
    public record TokenPair(String accessToken, String refreshToken) {}

    /*
     * lo que el cliente envia al pedir un token un nuevo acceso, contiene el refresh token actual
     * 
     */
    public record RefreshRequest(@NotBlank String refreshToken) {}

    /*
     * devolvera datos en get estos ser informacion simple del usuario
     */
    public record MeResponse(Long id,String email, String rol) {}


    
@PostMapping("/login")
public ResponseEntity<TokenPair> login(@RequestBody @Valid LoginRequest req) {
    // Paso 1: Buscar al usuario por email
    Usuario usuario = usuarioRepository.findByEmail(req.email())
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    // Logs para depurar
    System.out.println("🔎 Email recibido: " + req.email());
    System.out.println("🔎 Password recibido: " + req.password());
    System.out.println("🔐 Hash almacenado: " + usuario.getHash());

    boolean passwordValida = passwordEncoder.matches(req.password(), usuario.getHash());
    System.out.println("✅ Contraseña válida: " + passwordValida);

    // Paso 2: Verificar que la contraseña coincida
    if (!passwordValida) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    // Paso 3: Generar access y refresh token usando JwtService
    String accessToken = jwtService.generateToken(usuario.getEmail());
    String refreshToken = jwtService.generateRefreshToken(usuario.getEmail());

    // Paso 4: Devolver los tokens al frontend
    return ResponseEntity.ok(new TokenPair(accessToken, refreshToken));
}

      /** Contrato de refresh: luego validaremos el refresh y emitiremos un access nuevo. */
    @PostMapping("/refresh")
    public ResponseEntity<TokenPair> refresh(@RequestBody @Valid RefreshRequest req) {
    // Siguiente paso: validar refresh y emitir nuevo access.        
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
    
    /** Contrato de /me: luego leeremos el JWT del header y devolveremos el usuario. */
    @GetMapping("/me")
    public ResponseEntity<MeResponse> me(){
    // Siguiente paso: extraer usuario del JWT y retornarlo.
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();

    }
}

    

