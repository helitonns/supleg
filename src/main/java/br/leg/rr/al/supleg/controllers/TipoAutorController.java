package br.leg.rr.al.supleg.controllers;

import br.leg.rr.al.supleg.enums.TipoAcao;
import br.leg.rr.al.supleg.models.TipoAutor;
import br.leg.rr.al.supleg.services.LogService;
import br.leg.rr.al.supleg.services.TipoAutorService;
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
public class TipoAutorController implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private LogService logService;
    
    @Inject
    private TipoAutorService tipoAutorService;
    
    @Getter
    private List<TipoAutor> tipoAutores;
    
    @Getter
    @Setter
    private TipoAutor tipoAutor;

    //__________________________________________________________________________
    @PostConstruct
    public void init() {
        iniciar();
        
        logService.salvar(TipoAcao.ACESSOU, "O usuário acessou a página: " + FacesUtils.getURL(), "","");
    }

    public String salvar(){
        try {
            tipoAutorService.salvar(tipoAutor);
        } catch (Exception e) {
            MensagensUtils.addErrorMessageFlashScoped(e.getLocalizedMessage());
        }
        return "tipoAutor.xhtml" + "?faces-redirect=true";
    }

    public String excluir(){
        try {
            tipoAutorService.excluir(tipoAutor);
        } catch (Exception e) {
            MensagensUtils.addErrorMessageFlashScoped(e.getLocalizedMessage());
        }
        return "tipoAutor.xhtml" + "?faces-redirect=true";
    }
    
    public String cancelar() {
        return "tipoAutor.xhtml" + "?faces-redirect=true";
    }

    //__________________________________________________________________________
    private void iniciar() {
        tipoAutor = new TipoAutor();
        tipoAutor.setStatus(Boolean.TRUE);
        
        listarTipoAutoresAtivos();
    }

    private void listarTipoAutoresAtivos(){
        try {
            tipoAutores = tipoAutorService.listarPorStatus(Boolean.TRUE);
        } catch (Exception e) {
            MensagensUtils.addErrorMessageFlashScoped("Erro ao Listar TipoAutor");
        }
    }
}
