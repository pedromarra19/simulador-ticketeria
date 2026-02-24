package com.example.simuladorTicketeria.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.simuladorTicketeria.model.LoginRequest;
import com.example.simuladorTicketeria.model.LoginResponse;
import com.example.simuladorTicketeria.model.ValidateRequest;
import com.example.simuladorTicketeria.model.ValidateResponse;
import com.example.simuladorTicketeria.service.JwtService;
import com.example.simuladorTicketeria.service.UsuarioSimuladoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final JwtService jwtService;
    private final UsuarioSimuladoService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        log.info("📝 Tentativa de login: {}", request.getLogin());
        
        if (!usuarioService.validarCredenciais(request.getLogin(), request.getSenha())) {
            log.warn("❌ Credenciais inválidas para: {}", request.getLogin());
            
            Map<String, String> error = new HashMap<>();
            error.put("error", "Credenciais inválidas");
            error.put("message", "Login ou senha incorretos");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }

        String token = jwtService.generateToken(request.getLogin());
        String refreshToken = jwtService.generateRefreshToken(request.getLogin());
        
        usuarioService.salvarToken(request.getLogin(), token);
        
        log.info("✅ Login bem-sucedido para: {}", request.getLogin());

        LoginResponse response = new LoginResponse(
            token,
            refreshToken,
            3600,
            "Bearer"
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestBody ValidateRequest request) {
        log.info("🔍 Validando token");
        
        boolean isValid = jwtService.validateToken(request.getToken());
        String login = jwtService.getLoginFromToken(request.getToken());
        
        boolean isAtivo = login != null && usuarioService.isTokenAtivo(request.getToken());
        
        ValidateResponse response = new ValidateResponse();
        response.setValid(isValid && isAtivo);
        
        if (response.isValid()) {
            response.setMessage("Token válido");
            response.setUsuario(login);
            log.info("✅ Token válido para usuário: {}", login);
        } else {
            response.setMessage("Token inválido ou expirado");
            log.warn("❌ Token inválido");
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/usuarios")
    public ResponseEntity<?> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.getUsuariosCadastrados());
    }
}