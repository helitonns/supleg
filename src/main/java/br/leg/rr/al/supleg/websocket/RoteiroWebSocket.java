package br.leg.rr.al.supleg.websocket;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author Heliton
 */
@ServerEndpoint("/roteiroWebSocket")
public class RoteiroWebSocket {

    // Mapa para armazenar as sessões associadas aos IDs do roteiro
    private static Map<String, SessaoRoteiro> mapaSessoes = new HashMap<>();
    
    @OnOpen
    public void onOpen(Session session, @PathParam("idRoteiro") String idRoteiro) {
        // Adiciona a sessão recém-conectada ao mapa de sessões
        var idRoteiroParametro = session.getQueryString().replace("idRoteiro=", "");
        mapaSessoes.put(session.getId(), new SessaoRoteiro(idRoteiroParametro, session));
        
        // Imprime o ID da sessão e o ID do roteiro para identificação
        //System.out.println("Nova sessão aberta - ID: " + session.getId() + ", ID do Roteiro: " + idRoteiroParametro);
    }

    @OnClose
    public void onClose(Session session) {
        // Remove a sessão que foi desconectada do mapa de sessões
        mapaSessoes.remove(session.getId());
        // Imprime o ID da sessão para identificação
        //System.out.println("Sessão fechada - ID: " + session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        // Lógica para lidar com mensagens recebidas do cliente, se necessário
    }

    // Método para enviar o ID do roteiro para todas as sessões associadas a esse roteiro
    public static void enviarIdRoteiro(String idRoteiro) {
        for (SessaoRoteiro sessaoRoteiro : mapaSessoes.values()) {
            if (sessaoRoteiro.getIdRoteiro().equals(idRoteiro)) {
                try {
                    sessaoRoteiro.getSession().getBasicRemote().sendText(idRoteiro);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
    
    // Classe privada para armazenar o ID do roteiro e a respectiva sessão
    private static class SessaoRoteiro {
        private String idRoteiro;
        private Session session;

        public SessaoRoteiro(String idRoteiro, Session session) {
            this.idRoteiro = idRoteiro;
            this.session = session;
        }

        public String getIdRoteiro() {
            return idRoteiro;
        }

        public Session getSession() {
            return session;
        }
    }

}
