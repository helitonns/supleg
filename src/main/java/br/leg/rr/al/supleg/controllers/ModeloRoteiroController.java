package br.leg.rr.al.supleg.controllers;

import br.leg.rr.al.supleg.enums.PossuiOrdem;
import br.leg.rr.al.supleg.enums.TipoAcao;
import br.leg.rr.al.supleg.models.Abertura;
import br.leg.rr.al.supleg.models.Fechamento;
import br.leg.rr.al.supleg.models.ModeloRoteiro;
import br.leg.rr.al.supleg.services.AberturaService;
import br.leg.rr.al.supleg.services.FechamentoService;
import br.leg.rr.al.supleg.services.LogService;
import br.leg.rr.al.supleg.services.ModeloRoteiroService;
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
public class ModeloRoteiroController implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Inject
    private LogService logService;
    
    @Inject
    private ModeloRoteiroService modeloRoteiroService;
    
    @Inject
    private AberturaService aberturaService;
    
    @Inject
    private FechamentoService fechamentoService;
    
    @Getter
    private List<ModeloRoteiro> modeloRoteiros;
    @Getter
    private List<Abertura> aberturas;
    @Getter
    private List<Fechamento> fechamentos;
    
    @Getter
    @Setter
    private ModeloRoteiro modeloRoteiro;
    
    @Getter
    @Setter
    private Abertura abertura;
    
    @Getter
    @Setter
    private Fechamento fechamento;

    //__________________________________________________________________________
    @PostConstruct
    public void init() {
        iniciar();
        logService.salvar(TipoAcao.ACESSOU, "O usuário acessou a página: " + FacesUtils.getURL(), "","");
    }

    public String salvar(){
        try {
            modeloRoteiro.setAbertura(abertura);
            modeloRoteiro.setFechamento(fechamento);
            modeloRoteiroService.salvar(modeloRoteiro);
        } catch (Exception e) {
            MensagensUtils.addErrorMessageFlashScoped(e.getLocalizedMessage());
        }
        return "modelo-roteiro.xhtml" + "?faces-redirect=true";
    }

    public String excluir(){
        try {
            modeloRoteiroService.excluir(modeloRoteiro);
        } catch (Exception e) {
            MensagensUtils.addErrorMessageFlashScoped(e.getLocalizedMessage());
        }
        return "modelo-roteiro.xhtml" + "?faces-redirect=true";
    }
    
    public String cancelar() {
        return "modelo-roteiro.xhtml" + "?faces-redirect=true";
    }

    //__________________________________________________________________________
    private void iniciar() {
        modeloRoteiro = new ModeloRoteiro();
        modeloRoteiro.setStatus(Boolean.TRUE);
        modeloRoteiro.setPossuiOrdemDia(PossuiOrdem.ORDEM_AUTOMATICA);
        
        listarRoteirosAtivos();
        listarAberturaAtivas();
        listarFechamentoAtivos();
    }

    private void listarRoteirosAtivos(){
        try {
            modeloRoteiros = modeloRoteiroService.listarPorStatus(Boolean.TRUE);
        } catch (Exception e) {
            MensagensUtils.addErrorMessageFlashScoped("Erro ao Listar Roteiros");
        }
    }
    
    private void listarAberturaAtivas(){
        try {
            aberturas = aberturaService.listarPorStatus(Boolean.TRUE);
        } catch (Exception e) {
            MensagensUtils.addErrorMessageFlashScoped("Erro ao Listar Roteiros");
        }
    }
    
    private void listarFechamentoAtivos(){
        try {
            fechamentos = fechamentoService.listarPorStatus(Boolean.TRUE);
        } catch (Exception e) {
            MensagensUtils.addErrorMessageFlashScoped("Erro ao Listar Roteiros");
        }
    }
}
