package com.techmark.api_consultor.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
    // Vai ser responsavel por guardar os contadores das estatisticas
    private static Map<String, Integer> contadores = new HashMap<>();
    // Responsavel pelo Histórico
    private static List<String> historico = new ArrayList<>();

    static {
        contadores.put("cep", 0);
        contadores.put("fato-gato", 0);
        contadores.put("piada", 0);
    }

    // Método responsavel reutilizado do ConsultorApi original
    private String fazerRequisicao(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
        conexao.setRequestMethod("GET");
        conexao.addRequestProperty("User-Agente", "Mozilla/5.0");
        BufferedReader leitor = new BufferedReader(
                new InputStreamReader(conexao.getInputStream()));

        StringBuilder resposta = new StringBuilder();
        String linha;
        while ((linha = leitor.readLine()) != null) {
            resposta.append(linha);
        }
        leitor.close();

        conexao.disconnect();

        return resposta.toString();
    }

    @GetMapping("/cep/{cep}")
    public String consultarCep(@PathVariable String cep) {
        try {
            String url = "https://viacep.com.br/ws/" + cep + "/json/";
            String sJsonResposta = fazerRequisicao(url);

            String logradouro = extrairValorJSON(sJsonResposta, "logradouro");
            String bairro = extrairValorJSON(sJsonResposta, "bairro");
            String localidade = extrairValorJSON(sJsonResposta, "localidade");
            String uf = extrairValorJSON(sJsonResposta, "uf");

            return String.format("""
                    Consulta de CEP -
                    Logradouro: %s
                    Bairro: %s
                    Localidade: %s
                    UF: %s
                    """, logradouro, bairro, localidade, uf);

        } catch (Exception e) {
            return "Aconteceu algum erro: " + e.getMessage();
        }
    }

    private String extrairValorJSON(String sJson, String sChave) {
        try {
            String sBusca = "\"" + sChave + "\":\"";
            int iInicio = sJson.indexOf(sBusca);

            if (iInicio == -1) {
                sBusca = "\"" + sChave + "\":";
                iInicio = sJson.indexOf(sBusca);
                if (iInicio == -1) {
                    return "Não existe esse campo!";
                }
                iInicio += sBusca.length();
                int iFim = sJson.indexOf(",", iInicio);

                if (iFim == -1) {
                    iFim = sJson.indexOf("}", iInicio);
                }
                return sJson.substring(iInicio, iFim).trim();
            }
            iInicio += sBusca.length();
            int iFim = sJson.indexOf("\"", iInicio);
            return sJson.substring(iInicio, iFim).trim();

        } catch (Exception e) {
            return "Não encontrado!";
        }
    }

    @GetMapping("/fato")
    public Map<String, String> consultarFatosGatos() {
        try {
            String url = "https://catfact.ninja/fact";
            String sJsonResposta = fazerRequisicao(url);

            String fato = extrairValorJSON(sJsonResposta, "fact");

            Map<String, String> resposta = new HashMap<>();
            resposta.put("fato", fato);
            return resposta;

        } catch (Exception e) {
            return Map.of("erro", e.getMessage());
        }
    }

    @GetMapping("/conselho")
    public String consultarConselhoAleatorio() {
        try {
            String url = "https://api.adviceslip.com/advice";
            String sJsonResposta = fazerRequisicao(url);

            String advice = extrairValorJSON(sJsonResposta, "advice");

            return String.format("""
                Consulta Conselho Aleatório -
                Conselho: %s
                        """,advice);

        } catch (Exception e) {
            return "Aconteceu algum erro: " + e.getMessage();
        }
    }
    @GetMapping("/frontend")
    public String frontend() {
        return "index";
    }
    

}