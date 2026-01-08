package br.com.personal.chatia.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    public String perguntarAoGemini(String dadosJson, String pergunta) {

        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-flash-latest:generateContent?key=" + apiKey;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();
        LocalDateTime horarioAtual = LocalDateTime.now();

        String regraDeFormatacao =
                "REGRA ESTRITA DE RESPOSTA: " +
                        "Responda APENAS com uma lista numerada simples. " +
                        "não use texto extra. " +
                        "Siga  este padrão para as perguntas pertinentes : " +
                        "'{Numero}. {Nome do Remédio} - {Data de Término} - {Nome da Pessoa}'. " +
                        "Se a data não estiver explícita, calcule baseada no estoque e uso diário a partir de hoje (" + horarioAtual + ").";

        String promptCombinado = "Contexto (Dados do Estoque):\n" + dadosJson +
                "\n\n" + regraDeFormatacao +
                "\n\nPergunta do usuário: " + pergunta;


        String promptEscapado = promptCombinado
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "");

        String jsonGoogle = "{\"contents\": [{\"parts\": [{\"text\": \"" + promptEscapado + "\"}]}]}";

        HttpEntity<String> request = new HttpEntity<>(jsonGoogle, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

            JsonNode root = mapper.readTree(response.getBody());
            JsonNode candidates = root.path("candidates");

            if (candidates.isArray() && !candidates.isEmpty()) {
                return candidates.get(0).path("content").path("parts").get(0).path("text").asText();
            } else {
                return "O Gemini respondeu sem texto.";
            }

        } catch (Exception e) {
            return "Erro na comunicação com a IA: " + e.getMessage();
        }
    }
}