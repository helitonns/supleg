package br.leg.rr.al.supleg.services;

import br.leg.rr.al.supleg.dtos.DataDTO;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

/**
 *
 * @author heliton.nascimento
 */
public class SessaoPlenariaService {

    static String BASE_URL = "https://sapl.al.rr.leg.br/api/";
    static int codigoSucesso = 200;

    public static DataDTO buscaSessaoPorData(LocalDate dataSessao) throws Exception {
        var dataDaSessao = dataSessao.getYear()+"-"+dataSessao.getMonthValue()+"-"+dataSessao.getDayOfMonth();
        //String urlParaChamada = BASE_URL + "sessao-plenaria/?data_inicio="+ dataDaSessao + "&data_fim=" + dataDaSessao;
        String urlParaChamada = BASE_URL + "sessao-plenaria/?data_inicio="+ dataDaSessao;

        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpResponse<String> response = client.send(HttpRequest.newBuilder()
                .uri(URI.create(urlParaChamada))
                .build(), HttpResponse.BodyHandlers.ofString());

            Gson gson = new Gson();
            DataDTO data = gson.fromJson(response.body(), DataDTO.class);
            return data;
        } catch (IOException | InterruptedException e) {
            throw new Exception("ERRO: " + e.getMessage());
        }       
    }
    
    private static String converteJsonEmString(BufferedReader buffereReader) throws IOException {
        var resposta = "";
        var jsonEmString = "";
        while ((resposta = buffereReader.readLine()) != null) {
            jsonEmString += resposta;
        }
        return new String(jsonEmString.getBytes(), StandardCharsets.UTF_8);
    }
    
}
