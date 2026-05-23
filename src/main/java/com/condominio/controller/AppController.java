package com.condominio.controller;

import com.condominio.entity.*;
import com.condominio.repository.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AppController {

    private final PessoaRepository pessoaRepository;
    private final UsuarioRepository usuarioRepository;
    private final AcessoRepository acessoRepository;
    private final BCryptPasswordEncoder encoder;

    public AppController(
            PessoaRepository pessoaRepository,
            UsuarioRepository usuarioRepository,
            AcessoRepository acessoRepository,
            BCryptPasswordEncoder encoder
    ) {
        this.pessoaRepository = pessoaRepository;
        this.usuarioRepository = usuarioRepository;
        this.acessoRepository = acessoRepository;
        this.encoder = encoder;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String root() {
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping("/usuarios")
    public String usuarios(Model model) {
        model.addAttribute("usuarios", usuarioRepository.findAll());
        return "usuarios";
    }

    @GetMapping("/usuarios/novo")
    public String novoUsuario(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "novo_usuario";
    }

    @PostMapping("/usuarios/salvar")
    public String salvarUsuario(
            @RequestParam(required = false) Long id,
            @RequestParam String username,
            @RequestParam(required = false) String password
    ) {

        Usuario usuario;

        if (id != null) {
            usuario = usuarioRepository.findById(id)
                    .orElse(new Usuario());
        } else {
            usuario = new Usuario();
        }

        usuario.setUsername(username);

        if (password != null && !password.isBlank()) {
            usuario.setPassword(encoder.encode(password));
        }

        usuarioRepository.save(usuario);

        return "redirect:/usuarios";
    }

    @GetMapping("/pessoas")
    public String pessoas(Model model) {
        model.addAttribute("pessoas", pessoaRepository.findAll());
        return "pessoas";
    }

    @GetMapping("/cadastrar")
    public String cadastrar(Model model) {
        model.addAttribute("pessoa", new Pessoa());
        return "cadastrar";
    }

    @PostMapping("/cadastrar")
    public String salvarPessoa(@ModelAttribute Pessoa pessoa) {
        pessoaRepository.save(pessoa);
        return "redirect:/pessoas";
    }

    @GetMapping("/controle")
    public String controle(Model model) {

        List<Pessoa> pessoas = pessoaRepository.findAll();

        Map<Long, String> statusMap = new HashMap<>();

        for (Pessoa p : pessoas) {

            boolean dentro = acessoRepository
                    .findTopByPessoaIdAndSaidaIsNullOrderByEntradaDesc(p.getId())
                    .isPresent();

            statusMap.put(p.getId(), dentro ? "DENTRO" : "FORA");
        }

        model.addAttribute("pessoas", pessoas);
        model.addAttribute("statusMap", statusMap);

        return "controle";
    }

    @PostMapping("/controle/entrada/{id}")
    public String entrada(@PathVariable Long id) {

        Pessoa pessoa = pessoaRepository
                .findById(id)
                .orElseThrow();

        Acesso acesso = new Acesso();

        acesso.setPessoa(pessoa);
        acesso.setEntrada(LocalDateTime.now());

        acessoRepository.save(acesso);

        return "redirect:/controle";
    }

    @PostMapping("/controle/saida/{id}")
    public String saida(@PathVariable Long id) {

        Acesso acesso = acessoRepository
                .findTopByPessoaIdAndSaidaIsNullOrderByEntradaDesc(id)
                .orElseThrow();

        acesso.setSaida(LocalDateTime.now());

        acessoRepository.save(acesso);

        return "redirect:/controle";
    }

    @GetMapping("/controle/log/{id}")
    public String log(@PathVariable Long id, Model model) {

        Pessoa pessoa = pessoaRepository
                .findById(id)
                .orElseThrow();

        model.addAttribute("pessoa", pessoa);

        model.addAttribute(
                "logs",
                acessoRepository.findByPessoaIdOrderByEntradaDesc(id)
        );

        return "controle_log";
    }

    // =========================
    // EDITAR PESSOA
    // =========================

    @GetMapping("/pessoas/editar/{id}")
    public String editarPessoa(@PathVariable Long id, Model model) {

        Pessoa pessoa = pessoaRepository
                .findById(id)
                .orElseThrow();

        model.addAttribute("pessoa", pessoa);

        return "cadastrar";
    }

    // =========================
    // EXCLUIR PESSOA
    // =========================

    @PostMapping("/pessoas/excluir/{id}")
    public String excluirPessoa(@PathVariable Long id) {

        acessoRepository.deleteByPessoaId(id);

        pessoaRepository.deleteById(id);

        return "redirect:/pessoas";
    }

    // =========================
    // EDITAR USUÁRIO
    // =========================

    @GetMapping("/usuarios/editar/{id}")
    public String editarUsuario(@PathVariable Long id, Model model) {

        Usuario usuario = usuarioRepository
                .findById(id)
                .orElseThrow();

        model.addAttribute("usuario", usuario);

        return "novo_usuario";
    }

    // =========================
    // EXCLUIR USUÁRIO
    // =========================

    @PostMapping("/usuarios/excluir/{id}")
    public String excluirUsuario(@PathVariable Long id) {

        usuarioRepository.deleteById(id);

        return "redirect:/usuarios";
    }

    @GetMapping("/gerenciar")
    public String gerenciar() {
        return "gerenciar";
    }

    @GetMapping("/backup")
    public String backup() {
        return "backup";
    }
}
