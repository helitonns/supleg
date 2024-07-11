package br.leg.rr.al.supleg.controllers;

import br.leg.rr.al.supleg.enums.TipoAcao;
import br.leg.rr.al.supleg.models.Parlamentar;
import br.leg.rr.al.supleg.services.LogService;
import br.leg.rr.al.supleg.services.ParlamentarService;
import br.leg.rr.al.supleg.utils.FacesUtils;
import br.leg.rr.al.supleg.utils.MensagensUtils;
import java.io.Serializable;
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
public class ParlamentarController implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private LogService logService;
    
    @Inject
    private ParlamentarService parlamentarService;
    
    @Getter
    private List<Parlamentar> parlamentares;
    
    @Getter
    @Setter
    private Parlamentar parlamentar;

    //__________________________________________________________________________
    @PostConstruct
    public void init() {
        iniciar();
        
        logService.salvar(TipoAcao.ACESSOU, "O usuário acessou a página: " + FacesUtils.getURL(), "","");
    }

    public String salvar(){
        try {
            parlamentarService.salvar(parlamentar);
        } catch (Exception e) {
            MensagensUtils.addErrorMessageFlashScoped(e.getLocalizedMessage());
        }
        return "parlamentar.xhtml" + "?faces-redirect=true";
    }

    public String excluir(){
        try {
            parlamentarService.excluir(parlamentar);
        } catch (Exception e) {
            MensagensUtils.addErrorMessageFlashScoped(e.getLocalizedMessage());
        }
        return "parlamentar.xhtml" + "?faces-redirect=true";
    }
    
    public String cancelar() {
        return "parlamentar.xhtml" + "?faces-redirect=true";
    }

    //__________________________________________________________________________
    private void iniciar() {
        parlamentar = new Parlamentar();
        parlamentar.setStatus(Boolean.TRUE);
        
        listarParlamentaresAtivos();
    }

    private void listarParlamentaresAtivos(){
        try {
            parlamentares = parlamentarService.listarPorStatus(Boolean.TRUE);
        } catch (Exception e) {
            MensagensUtils.addErrorMessageFlashScoped("Erro ao Listar Parlamentar");
        }
    }
}
