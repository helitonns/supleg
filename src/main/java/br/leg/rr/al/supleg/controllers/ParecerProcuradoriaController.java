package br.leg.rr.al.supleg.controllers;

import br.leg.rr.al.supleg.enums.TipoAcao;
import br.leg.rr.al.supleg.models.ParecerProcuradoria;
import br.leg.rr.al.supleg.services.LogService;
import br.leg.rr.al.supleg.services.ParecerProcuradoriaService;
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
public class ParecerProcuradoriaController implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Inject
    private LogService logService;
    
    @Inject
    private ParecerProcuradoriaService parecerProcuradoriaService;
    
    @Getter
    private List<ParecerProcuradoria> pareceresProcuradorias;
    
    @Getter
    @Setter
    private ParecerProcuradoria parecerProcuradoria;

    //__________________________________________________________________________
    @PostConstruct
    public void init() {
        iniciar();
        
        logService.salvar(TipoAcao.ACESSOU, "O usuário acessou a página: " + FacesUtils.getURL(), "","");
    }

    public String salvar(){
        try {
            parecerProcuradoriaService.salvar(parecerProcuradoria);
        } catch (Exception e) {
            MensagensUtils.addErrorMessageFlashScoped(e.getLocalizedMessage());
        }
        return "parecerProcuradoria.xhtml" + "?faces-redirect=true";
    }

    public String excluir(){
        try {
            parecerProcuradoriaService.excluir(parecerProcuradoria);
        } catch (Exception e) {
            MensagensUtils.addErrorMessageFlashScoped(e.getLocalizedMessage());
        }
        return "parecerProcuradoria.xhtml" + "?faces-redirect=true";
    }
    
    public String cancelar() {
        return "parecerProcuradoria.xhtml" + "?faces-redirect=true";
    }

    //__________________________________________________________________________
    private void iniciar() {
        parecerProcuradoria = new ParecerProcuradoria();
        parecerProcuradoria.setStatus(Boolean.TRUE);
        
        listarParecerProcuradoriaesAtivos();
    }

    private void listarParecerProcuradoriaesAtivos(){
        try {
            pareceresProcuradorias = parecerProcuradoriaService.listarPorStatus(Boolean.TRUE);
        } catch (Exception e) {
            MensagensUtils.addErrorMessageFlashScoped("Erro ao Listar ParecerProcuradoria");
        }
    }
}
