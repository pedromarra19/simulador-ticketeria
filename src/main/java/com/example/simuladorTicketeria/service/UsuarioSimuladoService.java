package com.example.simuladorTicketeria.service;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UsuarioSimuladoService {
    
    private final Map<String, String> usuarios = new ConcurrentHashMap<>();
    private final Map<String, String> tokensAtivos = new ConcurrentHashMap<>();
    
    public UsuarioSimuladoService() {
        usuarios.put("joao@email.com", "123456");
        usuarios.put("maria@email.com", "654321");
        usuarios.put("admin@ticketeria.com", "admin123");
        usuarios.put("usuario_teste", "senha123");
    }
    
    public boolean validarCredenciais(String login, String senha) {
        String senhaArmazenada = usuarios.get(login);
        return senhaArmazenada != null && senhaArmazenada.equals(senha);
    }
    
    public void salvarToken(String login, String token) {
        tokensAtivos.put(token, login);
    }
    
    public boolean isTokenAtivo(String token) {
        return tokensAtivos.containsKey(token);
    }
    
    public String getLoginPorToken(String token) {
        return tokensAtivos.get(token);
    }
    
    public void removerToken(String token) {
        tokensAtivos.remove(token);
    }
    
    public Map<String, String> getUsuariosCadastrados() {
        return new HashMap<>(usuarios);
    }
}