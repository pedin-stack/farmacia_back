package br.com.personal.chatia.controller;

import br.com.personal.chatia.DTO.ChatRequestDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/chat")
@CrossOrigin(origins = "*") // Permite o React acessar
public class ChatController {

    @Autowired
    private GeminiService geminiService;

    @PostMapping
    public String conversar(@RequestBody ChatRequestDTO dto) {

        if (dto.getDadosJson() == null || dto.getPergunta() == null) {
            return "Erro: Dados do estoque ou pergunta não fornecidos.";
        }


        return geminiService.perguntarAoGemini(dto.getPergunta(), dto.getDadosJson());
    }
}

  /*  @GetMapping("/modelos")
    public String listarModelos() {

        String apiKey = "api_key";

        String url = "https://generativelanguage.googleapis.com/v1beta/models?key=" + apiKey;
        org.springframework.web.client.RestTemplate restTemplate = new org.springframework.web.client.RestTemplate();

        try {
            return restTemplate.getForObject(url, String.class);
        } catch (Exception e) {
            return "Erro ao listar modelos: " + e.getMessage();
        }
    }*/

