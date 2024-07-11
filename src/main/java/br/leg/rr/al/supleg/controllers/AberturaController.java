package br.leg.rr.al.supleg.controllers;

import br.leg.rr.al.supleg.enums.TipoAcao;
import br.leg.rr.al.supleg.models.Abertura;
import br.leg.rr.al.supleg.models.Trecho;
import br.leg.rr.al.supleg.services.AberturaService;
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
public class AberturaController implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private LogService logService;
    
    @Inject
    private AberturaService aberturaService;
    
    @Getter
    private List<Abertura> aberturas;
    
    @Getter
    @Setter
    private List<Trecho> trechos;
    
    @Getter
    @Setter
    private Abertura abertura;
    
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
            abertura.setTrechos(trechos);
            aberturaService.salvar(abertura);
        } catch (Exception e) {
            MensagensUtils.addErrorMessageFlashScoped(e.getLocalizedMessage());
        }
        return "abertura.xhtml" + "?faces-redirect=true";
    }

    public String excluir(){
        try {
            aberturaService.excluir(abertura);
        } catch (Exception e) {
            MensagensUtils.addErrorMessageFlashScoped(e.getLocalizedMessage());
        }
        return "abertura.xhtml" + "?faces-redirect=true";
    }
    
    public String cancelar() {
        return "abertura.xhtml" + "?faces-redirect=true";
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
        abertura = new Abertura();
        abertura.setStatus(Boolean.TRUE);
        
        trecho = new Trecho();
        trechos = new ArrayList<>();
        
        listarAberturasAtivas();
    }

    private void listarAberturasAtivas(){
        try {
            aberturas = aberturaService.listarPorStatus(Boolean.TRUE);
        } catch (Exception e) {
            MensagensUtils.addErrorMessageFlashScoped("Erro ao Listar Aberturas");
        }
    }
}
