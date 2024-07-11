package br.leg.rr.al.supleg.controllers;

import br.leg.rr.al.supleg.enums.TipoAcao;
import static br.leg.rr.al.supleg.enums.TurnoVotacao.TURNO_1;
import static br.leg.rr.al.supleg.enums.TurnoVotacao.TURNO_UNICO;
import br.leg.rr.al.supleg.models.DocumentoAcessorio;
import br.leg.rr.al.supleg.models.Link;
import br.leg.rr.al.supleg.models.Roteiro;
import br.leg.rr.al.supleg.models.RoteiroSecretario;
import br.leg.rr.al.supleg.services.LogService;
import br.leg.rr.al.supleg.services.RoteiroService;
import br.leg.rr.al.supleg.utils.FacesUtils;
import br.leg.rr.al.supleg.utils.MensagensUtils;
import java.io.Serializable;
import java.util.Comparator;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import org.omnifaces.cdi.Param;

/**
 *
 * @author Heliton
 */
@Named
@ViewScoped
public class ExibirRoteiroSecretarioController implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private RoteiroService roteiroService;

    @Getter
    private RoteiroSecretario roteiroSecretario;
    
    @Inject
    private LogService logService;

    @Getter
    @Setter
    private DocumentoAcessorio docSelecionado;

    @Getter
    @Setter
    private Link linkSelecionado;

    @Getter
    @Setter
    @Param(name = "roteiro")
    private Long idRoteiro;

    @Getter
    @Setter
    private Boolean exibirPainelDocs = false;
    
    @Getter
    private Roteiro roteiro;

    //__________________________________________________________________________
    @PostConstruct
    public void init() {
        iniciar();

        if (idRoteiro != null) {
            listarRoteiroSecretario();
            buscarRoteiroPorId();
        }
        
        logService.salvar(TipoAcao.ACESSOU, "O usuário acessou a página: " + FacesUtils.getURL(), "","");
    }

    public String salvarDocumentoAcessorio() {
        try {
            if(linkSelecionado.getId() == null){
                linkSelecionado.setParecer(false);
                docSelecionado.getLinks().add(linkSelecionado);
            }
            
            roteiroService.salvarDocumentoAcessorio(docSelecionado);
            linkSelecionado = new Link();
            MensagensUtils.addInfoMessageFlashScoped("Documento salvo com sucesso!");
            
            logService.salvar(TipoAcao.SALVOU, "O usuário salvou o documento.", "", docSelecionado.toString());
        } catch (Exception e) {
            MensagensUtils.addErrorMessageFlashScoped("Erro ao buscar Roteiro Secretário");
        }
        return "exibir-roteiro-secretario?faces-redirect=true&roteiro="+idRoteiro;
    }
    
    public String excluirLink() {
        try {
            docSelecionado.getLinks().removeIf(link -> link.getId().equals(linkSelecionado.getId()));
            roteiroService.salvarDocumentoAcessorio(docSelecionado);
            linkSelecionado = new Link();
            MensagensUtils.addInfoMessageFlashScoped("Documento exluído com sucesso!");
            logService.salvar(TipoAcao.APAGOU, "O usuário excluíu o link.", "", docSelecionado.toString());
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            MensagensUtils.addErrorMessageFlashScoped("Erro ao excluir Link");
        }
        return "exibir-roteiro-secretario?faces-redirect=true&roteiro="+idRoteiro;
    }
    
    public void resetLink(){
        linkSelecionado = new Link();
    }
    
    public void salvarRoteiro(String parametro) {
        try {
            roteiroService.salvar(roteiro);
            MensagensUtils.addInfoMessageFlashScoped("Roteiro salvo com sucesso!");

            logService.salvar(TipoAcao.ATUALIZOU, "O usuário alterou o roteiro: " + parametro, "", roteiro.toString());
            
        } catch (Exception e) {
            MensagensUtils.addErrorMessageFlashScoped("Erro ao salvar Roteiro!");
        }
    }
    
    public String revisarRoteiro(){
        roteiro.setRoteiroSecretarioRevisado(!roteiro.getRoteiroSecretarioRevisado());
        salvarRoteiro("REVISÃO DE ROTEIRO");

        return "exibir-roteiro-secretario.xhtml?faces-redirect=true&roteiro="+idRoteiro;
    }
    //__________________________________________________________________________
    private void iniciar() {
        roteiroSecretario = new RoteiroSecretario();
        docSelecionado = new DocumentoAcessorio();
        linkSelecionado = new Link();
    }

    private void listarRoteiroSecretario() {
        try {
            roteiroSecretario = roteiroService.listarRoteiroSecretarioPorIdRoteiro(idRoteiro);
            roteiroSecretario.getDocumentos().sort(Comparator.comparingInt(doc -> doc.getOrdem().getOrdem()));
        } catch (Exception e) {
            MensagensUtils.addErrorMessageFlashScoped("Erro ao buscar Roteiro Secretário");
        }
    }
    
    private void buscarRoteiroPorId() {
        try {
            roteiro = roteiroService.buscarPorId(idRoteiro);

        } catch (Exception e) {
            MensagensUtils.addErrorMessageFlashScoped("Erro ao buscar Roteiro");
        }
    }

}
