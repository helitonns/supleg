package br.leg.rr.al.supleg.controllers;

import br.leg.rr.al.supleg.dtos.EtiquetaDTO;
import br.leg.rr.al.supleg.enums.TipoAcao;
import br.leg.rr.al.supleg.enums.TurnoVotacao;
import br.leg.rr.al.supleg.models.DocumentoAcessorio;
import br.leg.rr.al.supleg.models.MesaModelo;
import br.leg.rr.al.supleg.models.Ordem;
import br.leg.rr.al.supleg.models.ParecerProcuradoria;
import br.leg.rr.al.supleg.models.Roteiro;
import br.leg.rr.al.supleg.models.RoteiroMateria;
import br.leg.rr.al.supleg.models.SessaoLegislativa;
import br.leg.rr.al.supleg.models.StatusMateria;
import br.leg.rr.al.supleg.models.StatusTramitacao;
import br.leg.rr.al.supleg.models.TipoAutor;
import br.leg.rr.al.supleg.services.LogService;
import br.leg.rr.al.supleg.services.MesaModeloService;
import br.leg.rr.al.supleg.services.OrdemDoDiaService;
import br.leg.rr.al.supleg.services.ParecerProcuradoriaService;
import br.leg.rr.al.supleg.services.RoteiroMateriaService;
import br.leg.rr.al.supleg.services.RoteiroService;
import br.leg.rr.al.supleg.services.SessaoLegislativaService;
import br.leg.rr.al.supleg.services.StatusMateriaService;
import br.leg.rr.al.supleg.services.StatusTramitacaoService;
import br.leg.rr.al.supleg.services.TipoAutorService;
import br.leg.rr.al.supleg.utils.FacesUtils;
import br.leg.rr.al.supleg.utils.MensagensUtils;
import br.leg.rr.al.supleg.websocket.RoteiroWebSocket;
import com.google.gwt.i18n.server.MessageUtils;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.inject.Inject;
import javax.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.beanutils.BeanUtils;
import org.omnifaces.cdi.Param;

/**
 *
 * @author Heliton
 */
@Named
@ViewScoped
public class ExibirRoteiroController implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private LogService logService;

    @Inject
    private RoteiroService roteiroService;

    @Inject
    private OrdemDoDiaService ordemDiaService;

    @Inject
    private MesaModeloService mesaModeloService;

    @Inject
    private SessaoLegislativaService sessaoLegislativaService;

    @Inject
    private StatusMateriaService statusMateriaService;

    @Inject
    private StatusTramitacaoService statusTramitacaoService;

    @Inject
    private ParecerProcuradoriaService parecerProcuradoriaService;

    @Inject
    private RoteiroMateriaService roteiroMateriaService;

    @Inject
    private TipoAutorService tipoAutorService;

    @Getter
    private List<StatusMateria> statusMaterias;

    @Getter
    private List<StatusTramitacao> statusTramitacoes;

    @Getter
    private List<ParecerProcuradoria> pareceres;

    @Getter
    private List<RoteiroMateria> roteirosMaterias;

    @Getter
    private List<TipoAutor> tiposAutores;

    @Getter
    @Setter
    private Roteiro roteiro;

    @Getter
    private SessaoLegislativa sessaoLegislativa;

    @Getter
    private MesaModelo mesa;

    @Getter
    @Setter
    @Param(name = "roteiro")
    private Long idRoteiro;

    @Getter
    private List<Ordem> ordensVotacao1Turno;
    @Getter
    private List<Ordem> ordensVotacao2Turno;
    @Getter
    private List<Ordem> ordensVotacaoTurnoUnico;

    @Getter
    @Setter
    private Ordem ordem;

    @Getter
    @Setter
    private Boolean exibirDialogoOrdem;
    
    @Getter
    @Setter
    private Boolean exibirDialogoOrdemEdicao;

    @Getter
    @Setter
    private Boolean exibirDialogoRemoverOrdem;
    
    @Getter
    @Setter
    private Boolean exibirDialogoAdicionarEtiqueta;

    @Getter
    @Setter
    private Integer item;

    @Getter
    @Setter
    private String etiqueta;
    
    @Getter
    @Setter
    private String corEtiqueta;
    
    @Getter
    @Setter
    private List<EtiquetaDTO> etiquetas;
    
    @Getter
    @Setter
    private String tipoMateria;

    @Getter
    @Setter
    private String numeroMateria;

    @Getter
    @Setter
    private String anoMateria;

    //__________________________________________________________________________
    @PostConstruct
    public void init() {
        iniciar();

        if (idRoteiro != null) {
            buscarRoteiroPorId();
            
            if(roteiro.getOrdemDia() != null){
                ordem.setOrdem(roteiro.getOrdemDia().getOrdens().size() + 1);
            }
        }

        logService.salvar(TipoAcao.ACESSOU, "O usuário acessou a página: " + FacesUtils.getURL(), "", "");
    }

    public void salvarRoteiro(String parametro) {
        try {
            roteiroService.salvar(roteiro);
            MensagensUtils.addInfoMessageFlashScoped("Roteiro salvo com sucesso!");

            logService.salvar(TipoAcao.ATUALIZOU, "O usuário alterou o roteiro: " + parametro, "", roteiro.toString());
            
            cancelarEdicaoOrdem();
        } catch (Exception e) {
            MensagensUtils.addErrorMessageFlashScoped("Erro ao salvar Roteiro!");
        }
    }

    @Transactional
    public String adicionarOrdem() {
        ordemDiaService.pesquisarRoteiroMateria(ordem);
        ordemDiaService.verficarSeEVeto(ordem);
        ordemDiaService.organizarSequenciaDosItens(roteiro.getOrdemDia().getOrdens(), ordem);
        
        var ordemClonada = new Ordem();
        try {
            ordemClonada = (Ordem) BeanUtils.cloneBean(ordem);
        } catch (Exception e) {
        }
        
        salvarRoteiro("ADICIONANDO ORDEM");

        //BUSCAR ORDEM POR ID
        var ordens = roteiroService.buscarOrdemPorOrdemDiaESequencia(roteiro.getOrdemDia());
        
        Ordem ordemSequencia = new Ordem();
        for(Ordem o : ordens){
            if(o.getOrdem().equals(ordemClonada.getOrdem())){
                ordemSequencia = o;
            }
        }

        if (Objects.nonNull(ordemSequencia.getId())) {
            ordem.setId(ordemSequencia.getId());
        }

        var roteiroSecretario = roteiroService.listarRoteiroSecretarioPorIdRoteiro(roteiro.getId());
        var documentoAcessorio = ordemDiaService.pesquisarParecer(ordemSequencia);
        
        if( roteiroSecretario.getDocumentos() != null){
            roteiroSecretario.getDocumentos().add(documentoAcessorio);
        }else {
            var documentos = new ArrayList<DocumentoAcessorio>();
            documentos.add(documentoAcessorio);
            roteiroSecretario.setDocumentos(documentos);
        }
        
        roteiroService.salvarSecretario(roteiroSecretario);

        ordem = new Ordem();
        exibirDialogoOrdem = false;
        
        return "exibir-roteiro.xhtml?faces-redirect=true&roteiro=" + idRoteiro;
    }

    public String removerOrdem() {
        ordemDiaService.removerItem(roteiro.getOrdemDia().getOrdens(), item);
        salvarRoteiro("REMOVENDO ORDEM");
        exibirDialogoRemoverOrdem = false;
        return "exibir-roteiro.xhtml?faces-redirect=true&roteiro=" + idRoteiro;
    }
    
    public void pesquisarMateria() {
        ordemDiaService.pesquisarMateria(ordem, tipoMateria, numeroMateria, anoMateria);
    }

    public void atualizarRoteirosWebSocket() {
        RoteiroWebSocket.enviarIdRoteiro(String.valueOf(idRoteiro));
        System.out.println("Executou o método WebSocket: atualizarRoteirosWebSocket()");
    }

    public String salvarEtiqueta() {
        formarEtiqueta();
        ordemDiaService.adicionarEtiqueta(roteiro.getOrdemDia().getOrdens(), item, etiqueta, corEtiqueta);
        salvarRoteiro("ADICIONANDO ETIQUETA");
        exibirDialogoAdicionarEtiqueta = false;
        return "exibir-roteiro.xhtml?faces-redirect=true&roteiro=" + idRoteiro;
    }
    
    public void adicionarEtiqueta() {
        etiquetas.add(new EtiquetaDTO(etiqueta, corEtiqueta));
        etiqueta = new String();
        corEtiqueta = new String();
    }
    
    public void removerEtiqueta() {
        etiquetas.removeIf(e -> e.getTexto().equals(etiqueta));
        etiqueta = new String();
        
    }
    
    private void formarEtiqueta(){
        var etiquetaCompleta = new StringBuilder();
        var corCompleta = new StringBuilder();
        
        if(etiquetas.isEmpty()){
            etiqueta = "";
            corEtiqueta = "";
        }else{
            etiquetas.forEach(e -> {
                etiquetaCompleta.append(e.getTexto()).append(";");
                corCompleta.append(e.getCor()).append(";");
            });
            
            etiqueta = etiquetaCompleta.toString();
            corEtiqueta = corCompleta.toString();
        }
        
    }
    
    public void carregarEtiquetas(){
        etiquetas = new ArrayList<>();
        
        roteiro.getOrdemDia().getOrdens().forEach(it -> {
            if(Objects.equals(it.getOrdem(), item)){
                if(Objects.nonNull(it.getEtiqueta()) && !it.getEtiqueta().isBlank()){
                    var etiquetasLigadas = List.of(it.getEtiqueta().split(";"));
                    var coresLigadas = List.of(it.getCorEtiqueta().split(";"));
                    
                    for(int i = 0; i < coresLigadas.size(); i++){
                        var etiquetaDto = new EtiquetaDTO(etiquetasLigadas.get(i), coresLigadas.get(i));
                        
                        etiquetas.add(etiquetaDto);
                    }
                }
                return;
            }
        });
    }
    
    public void selecionarOrdem(){
        
    }
    
    public void cancelarEdicaoOrdem(){
        ordem = new Ordem();
        exibirDialogoOrdemEdicao = false;
    }
    
    public String revisarRoteiro(){
        roteiro.setRoteiroPresidenteRevisado(!roteiro.getRoteiroPresidenteRevisado());
        salvarRoteiro("REVISÃO DE ROTEIRO");

        return "exibir-roteiro.xhtml?faces-redirect=true&roteiro="+idRoteiro;
    }
    
    public List<Integer> getAnos(){
        var hoje = LocalDate.now();
        var anoCorrent = hoje.getYear();
        var anos = new ArrayList<Integer>();
        anos.add(anoCorrent);
        
        for(int i = 1; i < 8; i++){
            anos.add(anoCorrent - i);
        }
        return anos;
    }
    //__________________________________________________________________________
    private void iniciar() {
        roteiro = new Roteiro();
        ordensVotacao1Turno = new ArrayList<>();
        ordensVotacao2Turno = new ArrayList<>();
        ordensVotacaoTurnoUnico = new ArrayList<>();
        exibirDialogoOrdem = false;
        exibirDialogoOrdemEdicao = false;
        ordem = new Ordem();
        ordem.setTurnoVotacao(TurnoVotacao.TURNO_UNICO);
        ordem.setTipoAutor("Parlamentar");
        ordemDiaService.inserirValoresPadroesNaOrdem(ordem);
        
        etiquetas = new ArrayList<>();
        corEtiqueta = "bg-blue";

        buscarMesa();
        buscarSessaoLegislativa();
        listarStatusMateriasAtivas();
        listarStatusTramitacoesAtivas();
        listarPareceresProcuradoriasAtivos();
        listarRoteirosMateriasAtivos();
        listarTiposAutoresAtivos();
    }

    private void buscarRoteiroPorId() {
        try {
            roteiro = roteiroService.buscarPorId(idRoteiro);

            roteiro.getOrdemDia().getOrdens().sort(Comparator.comparingInt(ordem -> ordem.getOrdem()));

            roteiro.getOrdemDia().getOrdens().forEach(ordem -> {
                switch (ordem.getTurnoVotacao()) {
                    case TURNO_UNICO -> ordensVotacaoTurnoUnico.add(ordem);
                    case TURNO_1 -> ordensVotacao1Turno.add(ordem);
                    default -> ordensVotacao2Turno.add(ordem);
                }
            });
        } catch (Exception e) {
            MensagensUtils.addErrorMessageFlashScoped("Erro ao buscar Roteiro");
        }
    }

    private void buscarMesa() {
        try {
            mesa = mesaModeloService.buscarUltimaPorStatus(Boolean.TRUE);
        } catch (Exception e) {
            MensagensUtils.addErrorMessageFlashScoped("Erro ao buscar Roteiro");
        }
    }

    private void buscarSessaoLegislativa() {
        try {
            sessaoLegislativa = sessaoLegislativaService.buscarUltimaPorStatus(Boolean.TRUE);
        } catch (Exception e) {
            MensagensUtils.addErrorMessageFlashScoped("Erro ao buscar Roteiro");
        }
    }

    private void listarStatusMateriasAtivas() {
        try {
            statusMaterias = statusMateriaService.listarPorStatus(Boolean.TRUE);
        } catch (Exception e) {
            MensagensUtils.addErrorMessageFlashScoped("Erro ao listar Status Matérias");
        }
    }

    private void listarStatusTramitacoesAtivas() {
        try {
            statusTramitacoes = statusTramitacaoService.listarPorStatus(Boolean.TRUE);
        } catch (Exception e) {
            MensagensUtils.addErrorMessageFlashScoped("Erro ao listar Status Tramitações");
        }
    }

    private void listarPareceresProcuradoriasAtivos() {
        try {
            pareceres = parecerProcuradoriaService.listarPorStatus(Boolean.TRUE);
        } catch (Exception e) {
            MensagensUtils.addErrorMessageFlashScoped("Erro ao listar Pareceres");
        }
    }

    private void listarRoteirosMateriasAtivos() {
        try {
            roteirosMaterias = roteiroMateriaService.listarPorStatus(Boolean.TRUE);
        } catch (Exception e) {
            MensagensUtils.addErrorMessageFlashScoped("Erro ao listar Roteiros Matérias");
        }
    }

    private void listarTiposAutoresAtivos() {
        try {
            tiposAutores = tipoAutorService.listarPorStatus(Boolean.TRUE);
        } catch (Exception e) {
            MensagensUtils.addErrorMessageFlashScoped("Erro ao listar Roteiros Matérias");
        }
    }
    
}
