package br.leg.rr.al.supleg.controllers;

import br.leg.rr.al.supleg.models.OrdemDia;
import br.leg.rr.al.supleg.services.OrdemDoDiaService;
import br.leg.rr.al.supleg.utils.MensagensUtils;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Heliton
 */
@Named
@ViewScoped
public class OrdemDiaController implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Inject
    private OrdemDoDiaService ordemDoDiaService;
    
    @Getter
    private List<OrdemDia> ordens;

    @Getter
    @Setter
    private LocalDateTime dataSessao;
    
    @Getter
    @Setter
    private OrdemDia ordemDia;
    
    //__________________________________________________________________________
    @PostConstruct
    public void init() {
        iniciar();
    }

    public String salvarRoteiro(){
        try {
            
            ordemDoDiaService.salvar(dataSessao);
            MensagensUtils.addInfoMessageFlashScoped("Ordem do dia salva com sucesso");
        } catch (Exception e) {
            MensagensUtils.addErrorMessageFlashScoped("Erro ao salvar Ordem do dia");
        }
        return "ordem-dia.xhtml" + "?faces-redirect=true";
    }
    
    public String excluir(){
        try {
            ordemDoDiaService.excluir(ordemDia);
            MensagensUtils.addInfoMessageFlashScoped("Roteiro Matéria excluído com sucesso");
        } catch (Exception e) {
            MensagensUtils.addErrorMessageFlashScoped("Erro ao excluir Roteiro Matéria");
        }
        return "ordem-dia.xhtml" + "?faces-redirect=true";
    }


    public String cancelar() {
        return "ordem-dia.xhtml" + "?faces-redirect=true";
    }

    //__________________________________________________________________________
    private void iniciar() {
        dataSessao = LocalDateTime.now();

        listarOrdemDia();
    }
    
    private void listarOrdemDia(){
        try {
            ordens = ordemDoDiaService.listarUltimos(10);
        } catch (Exception e) {
            MensagensUtils.addErrorMessageFlashScoped("Erro ao Listar TipoMateria");
        }
    }

}
