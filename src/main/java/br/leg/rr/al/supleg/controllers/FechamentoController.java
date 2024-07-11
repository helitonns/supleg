package br.leg.rr.al.supleg.controllers;

import br.leg.rr.al.supleg.enums.TipoAcao;
import br.leg.rr.al.supleg.models.Fechamento;
import br.leg.rr.al.supleg.models.Trecho;
import br.leg.rr.al.supleg.services.FechamentoService;
import br.leg.rr.al.supleg.services.LogService;
import br.leg.rr.al.supleg.utils.FacesUtils;
import br.leg.rr.al.supleg.utils.MensagensUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Heliton
 */
@Named
@ViewScoped
public class FechamentoController implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Inject
    private LogService logService;
    
    @Inject
    private FechamentoService fechamentoService;
    
    @Getter
    private List<Fechamento> fechamentos;
    
    @Getter
    @Setter
    private List<Trecho> trechos;
    
    @Getter
    @Setter
    private Fechamento fechamento;
    
    @Getter
    @Setter
    private Trecho trecho;

    //__________________________________________________________________________
    @PostConstruct
    public void init() {
        iniciar();
        
        logService.salvar(TipoAcao.ACESSOU, "O usuário acessou a página: " + FacesUtils.getURL(), "","");
    }

    public String salvar(){
        try {
            fechamento.setTrechos(trechos);
            fechamentoService.salvar(fechamento);
        } catch (Exception e) {
            MensagensUtils.addErrorMessageFlashScoped(e.getLocalizedMessage());
        }
        return "fechamento.xhtml" + "?faces-redirect=true";
    }

    public String excluir(){
        try {
            fechamentoService.excluir(fechamento);
        } catch (Exception e) {
            MensagensUtils.addErrorMessageFlashScoped(e.getLocalizedMessage());
        }
        return "fechamento.xhtml" + "?faces-redirect=true";
    }
    
    public String cancelar() {
        return "fechamento.xhtml" + "?faces-redirect=true";
    }

    public void adicionarTrecho(){
        trechos.add(trecho);
        trecho = new Trecho();
    }
    
    public void removerTrecho(){
        trechos.removeIf(item -> item.getTexto().equals(trecho.getTexto()));
        trecho = new Trecho();
    }
    //__________________________________________________________________________
    private void iniciar() {
        fechamento = new Fechamento();
        fechamento.setStatus(Boolean.TRUE);
        
        trecho = new Trecho();
        trechos = new ArrayList<>();
        
        listarFechamentoesAtivos();
    }

    private void listarFechamentoesAtivos(){
        try {
            fechamentos = fechamentoService.listarPorStatus(Boolean.TRUE);
        } catch (Exception e) {
            MensagensUtils.addErrorMessageFlashScoped("Erro ao Listar Fechamento");
        }
    }
}
