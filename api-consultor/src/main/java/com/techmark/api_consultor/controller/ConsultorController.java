package com.techmark.api_consultor.controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@RestController
@RequestMapping("/api")
public class ConsultorController {
    //Vai ser responsavel por guardar os contadores das estatisticas
    private static Map<String, Integer> contadores = new HashMap<>();
    //Responsavel pelo Histórico
    private static List<String> historico = new ArrayList<>();

    static{
        contadores.put("cep", 0);
        contadores.put("fato-gato", 0);
        contadores.put("piada", 0);
    }

    @GetMapping("/")
    public String home() {
        return """
        <h1>Consultor APIS - Spring Boot</h1>
        <h2>Endpoints Disponiveis: </h2>
        <ul>
            <li><a href=""></a> - Buscar CEP</li>
            <li><a href=""></a> - Fatos Gatos</li>
            <li><a href=""></a> - Piada</li>
            <li><a href=""></a> - Alguma Opcao...</li>
        </ul>        
        """;
    }
    //Método responsavel reutilizado do ConsultorApi original
    private  String fazerRequisicao(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
        conexao.setRequestMethod("GET");
        conexao.addRequestProperty("User-Agente", "Mozilla/5.0");
        BufferedReader leitor = new BufferedReader(
          new InputStreamReader(conexao.getInputStream())  
        );
        
        StringBuilder resposta = new StringBuilder();
        String linha;
        while ((linha = leitor.readLine()) != null) {
            resposta.append(linha);
        }
        leitor.close();

        conexao.disconnect();
        
        return resposta.toString();
    }
}
