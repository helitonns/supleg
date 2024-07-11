package br.leg.rr.al.supleg.controllers;

import br.leg.rr.al.supleg.enums.PossuiOrdem;
import br.leg.rr.al.supleg.enums.TipoAcao;
import br.leg.rr.al.supleg.models.MesaModelo;
import br.leg.rr.al.supleg.models.ModeloRoteiro;
import br.leg.rr.al.supleg.models.Ordem;
import br.leg.rr.al.supleg.models.OrdemDia;
import br.leg.rr.al.supleg.models.Roteiro;
import br.leg.rr.al.supleg.models.RoteiroSecretario;
import br.leg.rr.al.supleg.models.SessaoLegislativa;
import br.leg.rr.al.supleg.services.LogService;
import br.leg.rr.al.supleg.services.MesaModeloService;
import br.leg.rr.al.supleg.services.ModeloRoteiroService;
import br.leg.rr.al.supleg.services.OrdemDoDiaService;
import br.leg.rr.al.supleg.services.RelatorioService;
import br.leg.rr.al.supleg.services.RoteiroService;
import br.leg.rr.al.supleg.services.SessaoLegislativaService;
import br.leg.rr.al.supleg.utils.FacesUtils;
import br.leg.rr.al.supleg.utils.MensagensUtils;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.schedule.ScheduleEntryMoveEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

/**
 *
 * @author Heliton
 */
@Named
@ViewScoped
public class RoteiroController implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private MesaModeloService mesaModeloService;

    @Inject
    private SessaoLegislativaService sessaoLegislativaService;

    @Inject
    private RelatorioService relatorioService;

    @Inject
    private RoteiroService roteiroService;

    @Inject
    private ModeloRoteiroService modeloRoteiroService;

    @Inject
    private OrdemDoDiaService ordemDiaService;

    @Inject
    private LogService logService;

    @Inject
    private StartController startController;

    @Getter
    private List<Roteiro> roteiros;

    @Getter
    private List<ModeloRoteiro> modeloRoteiros;

    @Getter
    private List<Ordem> ordens;

    @Getter
    @Setter
    private ModeloRoteiro modeloRoteiro;

    @Getter
    @Setter
    private OrdemDia ordemDia;

    @Getter
    @Setter
    private LocalDateTime dataSessao;

    @Getter
    @Setter
    private Roteiro roteiro;

    @Getter
    @Setter
    private ScheduleModel eventModel;

    @Getter
    @Setter
    private ScheduleEvent<?> event = new DefaultScheduleEvent<>();

    @Getter
    private final String serverTimeZone = ZoneId.systemDefault().toString();

    @Getter
    @Setter
    private LocalDate dataCorrente;

    @Getter
    @Setter
    private Boolean exibirPainelRoteiro = false;

    @Getter
    private Boolean eventDetails = false;

    private OrdemDia ordemDiaParaAtualizar;

    private Boolean atualizandoRoteiro;

    @Getter
    @Setter
    private String nomeCopiaRoteiro = "";

    @Getter
    private Boolean exibirDialogoCopia = false;
    
    @Getter
    private Boolean exibirDialogoRenomeia = false;

    private MesaModelo mesa;

    private SessaoLegislativa sessaoLegislativa;
    
    //__________________________________________________________________________
    @PostConstruct
    public void init(){
        iniciar();
        logService.salvar(TipoAcao.ACESSOU, "O usuário acessou a página: " + FacesUtils.getURL(), "", "");
        
        try {
            var idRoteiro = FacesUtils.getBean("roteiroParaSerAtualizado");
            event = (ScheduleEvent<?>) FacesUtils.getBean("event");
            if (idRoteiro != null) {
                FacesUtils.removeBean("roteiroParaSerAtualizado");
                FacesUtils.removeBean("event");
                System.out.println("ID para ser atualizado: " + idRoteiro);
                atualizarRoteiro(Long.valueOf(idRoteiro.toString()));
                FacesUtils.redirect("roteiro.xhtml?faces-redirect=true");
            }
        } catch (NumberFormatException e) {
        }
        
    }

    public void onEventSelect(SelectEvent<ScheduleEvent<?>> selectEvent) throws IOException {
        event = selectEvent.getObject();
        exibirPainelRoteiro = true;
    }
    
    public void onDateSelect(SelectEvent<LocalDateTime> selectEvent) {
        eventDetails = false;
        if (!startController.isOperador()) {
            event = DefaultScheduleEvent.builder()
                    .startDate(selectEvent.getObject())
                    .endDate(selectEvent.getObject().plusHours(1))
                    .build();
            dataSessao = selectEvent.getObject();
            eventDetails = true;
        }
    }

    public void onEventMove(ScheduleEntryMoveEvent event) {
        salvarAlteracaoDataRoteiro(Long.valueOf(event.getScheduleEvent().getDescription()), event.getScheduleEvent().getStartDate().toLocalDate());
    }
    
    public String salvar() {
        salvarRoteiro();
        logService.salvar(TipoAcao.SALVOU, "O usuário salvou o roteiro", "", roteiro.toString());
        return "roteiro.xhtml" + "?faces-redirect=true";
    }

    public String excluir() {
        var idRoteiro = event.getDescription();
        excluirRoteiro();
        logService.salvar(TipoAcao.APAGOU, "O usuário apagou o roteiro", "ID Roteiro: " + idRoteiro, "");
        return "roteiro.xhtml" + "?faces-redirect=true";
    }

    public String atualizar() {
        var idRoteiro = Long.valueOf(event.getDescription());
        
        atualizarRoteiro(idRoteiro);
        
        return "roteiro.xhtml" + "?faces-redirect=true";
    }

    public void preCopiar() {
        exibirDialogoCopia = true;
        exibirPainelRoteiro = false;
        
        nomeCopiaRoteiro = event.getTitle();
    }

    public String copiar() {
        try {
            var idRoteiro = Long.valueOf(event.getDescription());
            
            var roteiroResultado = roteiroService.buscarPorId(idRoteiro);
            
            roteiroResultado.getOrdemDia().getOrdens().sort(Comparator.comparingInt(ordem -> ordem.getOrdem()));
            var ordemDiaParaCopiar = roteiroResultado.getOrdemDia();
            var ordensParaCopiar = ordemDiaParaCopiar.getOrdens();

            var roteiroParaSerSalvo = new Roteiro();
            
            roteiroParaSerSalvo.setDataSessao(roteiroResultado.getDataSessao());
            roteiroParaSerSalvo.setModeloRoteiro(roteiroResultado.getModeloRoteiro());
            roteiroParaSerSalvo.setNome(nomeCopiaRoteiro);
            roteiroParaSerSalvo.setRoteiroPresidenteRevisado(roteiroResultado.getRoteiroPresidenteRevisado());
            roteiroParaSerSalvo.setRoteiroSecretarioRevisado(roteiroResultado.getRoteiroSecretarioRevisado());

            var ordemDiaParaSerSalva = new OrdemDia();
            ordemDiaParaSerSalva.setDataSessao(ordemDiaParaCopiar.getDataSessao());

            var ordensParaSeremSalvas = new ArrayList<Ordem>();
            
            ordensParaCopiar.forEach(ordem -> {
                var ordemNova = new Ordem();

                ordemNova.setAutor(ordem.getAutor());
                if (ordem.getAutorMateriaPrincipal() != null) {
                    ordemNova.setAutorMateriaPrincipal(ordem.getAutorMateriaPrincipal());
                }
                if (ordem.getEmenta() != null) {
                    ordemNova.setEmenta(ordem.getEmenta());
                }
                ordemNova.setIdMateria(ordem.getIdMateria());
                if (ordem.getIdMateriaPrincipal() != null) {
                    ordemNova.setIdMateriaPrincipal(ordem.getIdMateriaPrincipal());
                }
                ordemNova.setMateria(ordem.getMateria());
                ordemNova.setOrdem(ordem.getOrdem());
                ordemNova.setParecerProcuradoria(ordem.getParecerProcuradoria());
                ordemNova.setProcesso(ordem.getProcesso());
                ordemNova.setProtocolo(ordem.getProtocolo());
                ordemNova.setResultado(ordem.getResultado());
                ordemNova.setRoteiroMateria(ordem.getRoteiroMateria());
                ordemNova.setStatusMateria(ordem.getStatusMateria());
                ordemNova.setStatusTramitacao(ordem.getStatusTramitacao());
                ordemNova.setTurno(ordem.getTurno());
                ordemNova.setVeto(ordem.getVeto());
                ordemNova.setTurnoVotacao(ordem.getTurnoVotacao());

                if (ordem.getMateriaMateriaPrincipal() != null) {
                    ordemNova.setMateriaMateriaPrincipal(ordem.getMateriaMateriaPrincipal());
                }
                if (ordem.getTipoAutorMateriaPrincipal() != null) {
                    ordemNova.setTipoAutorMateriaPrincipal(ordem.getTipoAutorMateriaPrincipal());
                }
                ordemNova.setTipoAutor(ordem.getTipoAutor());

                ordensParaSeremSalvas.add(ordemNova);
            });

            ordemDiaParaSerSalva.setOrdens(ordensParaSeremSalvas);
            roteiroParaSerSalvo.setOrdemDia(ordemDiaParaSerSalva);

            try {
                roteiroService.salvar(roteiroParaSerSalvo);
                logService.salvar(TipoAcao.SALVOU, "O usuário copiou o roteiro", "ID do roteiro copiado: " + idRoteiro, roteiroParaSerSalvo.toString());
                
                FacesUtils.setBean("roteiroParaSerAtualizado", roteiroParaSerSalvo.getId());
                
                event = DefaultScheduleEvent
                        .builder()
                        .description(roteiroParaSerSalvo.getId().toString())
                        .build();
                
                FacesUtils.setBean("event", event);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } catch (NumberFormatException e) {
            MensagensUtils.addErrorMessageFlashScoped("Erro ao atualizar Roteiro");
        }
        return "roteiro.xhtml" + "?faces-redirect=true";
    }

    public void preRenomear() {
        exibirDialogoRenomeia = true;
        exibirPainelRoteiro = false;
        
        nomeCopiaRoteiro = event.getTitle();
    }
    
    public String renomear() {
        try {
            var idRoteiro = Long.valueOf(event.getDescription());
            var roteiroResultado = roteiroService.buscarPorId(idRoteiro);
            
            roteiroResultado.setNome(nomeCopiaRoteiro);
            roteiroService.salvar(roteiroResultado);
            MensagensUtils.addInfoMessageFlashScoped("Roteiro renomeado com sucesso!");
        } catch (NumberFormatException e) {
        }
        return "roteiro.xhtml" + "?faces-redirect=true";
    }
    
    public String cancelar() {
        return "roteiro.xhtml" + "?faces-redirect=true";
    }

    public void baixarOrdens() {
        try {
            var roteiroSelecionado = roteiroService.buscarPorId(Long.valueOf(event.getDescription()));
            if (roteiroSelecionado.getModeloRoteiro().getPossuiOrdemDiaBoolean()) {
                relatorioService.gerarRelatorioOrdemDoDia(roteiroSelecionado);
            } else {
                MensagensUtils.addWarnMessageFlashScoped("O roteiro não possui ordem do dia.");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void baixarRoteiro() {
        try {
            var roteiroSelecionado = roteiroService.buscarPorId(Long.valueOf(event.getDescription()));
            relatorioService.gerarRelatorioRoteiro(roteiroSelecionado, mesa, sessaoLegislativa);
            exibirPainelRoteiro = false;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }finally{
            FacesUtils.redirect("roteiro.xhtml?faces-redirect=true");
        }
    }
    
    public void atualizarRevisaoRoteiro(){
        roteiroService.atualizarRevisaoRoteiro();
    }
    //__________________________________________________________________________
    private void iniciar() {
        roteiro = new Roteiro();
        dataSessao = LocalDateTime.now();
        dataCorrente = LocalDate.now();
        eventModel = new DefaultScheduleModel();
        atualizandoRoteiro = false;
        ordemDiaParaAtualizar = new OrdemDia();

        listarModelosRoteiros();
        listarRoteiros();

        buscarMesa();
        buscarSessaoLegislativa();
    }

    private void listarModelosRoteiros() {
        try {
            modeloRoteiros = modeloRoteiroService.listarPorStatus(Boolean.TRUE);
        } catch (Exception e) {
            MensagensUtils.addErrorMessageFlashScoped("Erro ao Listar TipoMateria");
        }
    }

    public void listarRoteiros() {
        try {
            roteiros = roteiroService.listarPorIntervalo(dataCorrente.atTime(9, 0));
            adicionarEventosNoCelendario();
        } catch (Exception e) {
            MensagensUtils.addErrorMessageFlashScoped("Erro ao Listar TipoMateria");
        }
    }

    private void adicionarEventosNoCelendario() {
        eventModel = new DefaultScheduleModel();
        if (!roteiros.isEmpty()) {
            roteiros.forEach(item -> {
                var e = DefaultScheduleEvent.builder()
                        .title(item.getNome())
                        .description(item.getId().toString())
                        .startDate(item.getDataSessao())
                        .endDate(item.getDataSessao().plusHours(3))
                        .overlapAllowed(true)
                        .build();

                if (item.getRoteiroPresidenteRevisado() && item.getRoteiroSecretarioRevisado()) {
                    e.setBackgroundColor("lightgreen");
                } else {
                    e.setBackgroundColor("orange");
                }

                event = e;
                eventModel.addEvent(event);
            });
        }
    }

    private void salvarRoteiro() {
        try {
            if (modeloRoteiro.getPossuiOrdemDia().equals(PossuiOrdem.ORDEM_AUTOMATICA)) {
                ordens = ordemDiaService.pesquisarSessao(dataSessao.toLocalDate());
                if (ordens == null || ordens.isEmpty()) {
                    return;
                }
                var ordemDiaDoRoteiro = new OrdemDia(dataSessao, ordens);

                if (atualizandoRoteiro) {
                    ordemDiaDoRoteiro.getOrdens().forEach(ordemAtual -> {
                        var o = ordemDiaParaAtualizar
                                .getOrdens()
                                .stream()
                                .filter(ordemAntiga -> ordemAtual.getMateria().equalsIgnoreCase(ordemAntiga.getMateria()))
                                .findFirst()
                                .orElse(null);

                        if (o != null) {
                            ordemAtual.setParecerProcuradoria(o.getParecerProcuradoria());
                            ordemAtual.setStatusMateria(o.getStatusMateria());
                            ordemAtual.setStatusTramitacao(o.getStatusTramitacao());
                            ordemAtual.setTurnoVotacao(o.getTurnoVotacao());

                            ordemAtual.setIdMateriaPrincipal(o.getIdMateriaPrincipal());
                            ordemAtual.setAutorMateriaPrincipal(o.getAutorMateriaPrincipal());
                            ordemAtual.setVeto(o.getVeto());
                        }
                    });

                    ordemDiaParaAtualizar = new OrdemDia();
                    atualizandoRoteiro = false;
                }

                roteiro.setOrdemDia(ordemDiaDoRoteiro);
            } else if (modeloRoteiro.getPossuiOrdemDia().equals(PossuiOrdem.ORDEM_MANUAL)) {
                var ordemDiaDoRoteiro = new OrdemDia(dataSessao, new ArrayList<Ordem>());
                roteiro.setOrdemDia(ordemDiaDoRoteiro);
            }

            roteiro.setModeloRoteiro(modeloRoteiro);
            roteiro.setDataSessao(dataSessao);
            roteiro.setRoteiroPresidenteRevisado(false);
            roteiro.setRoteiroSecretarioRevisado(false);
            roteiroService.salvar(roteiro);
            MensagensUtils.addInfoMessageFlashScoped("Roteiro do Presidente salvo com sucesso");

            if (modeloRoteiro.getPossuiOrdemDia().equals(PossuiOrdem.ORDEM_AUTOMATICA) || modeloRoteiro.getPossuiOrdemDia().equals(PossuiOrdem.ORDEM_MANUAL)) {
                var pareceres = ordemDiaService.pesquisarParecer(roteiro.getOrdemDia().getOrdens());
                var roteiroSecretario = new RoteiroSecretario();
                roteiroSecretario.setRoteiro(roteiro);
                roteiroSecretario.setDocumentos(pareceres);
                roteiroService.salvarSecretario(roteiroSecretario);

                MensagensUtils.addInfoMessageFlashScoped("Roteiro do 1º Secretário salvo com sucesso");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            MensagensUtils.addErrorMessageFlashScoped("Erro ao salvar Roteiro");
        }
    }

    private void salvarAlteracaoDataRoteiro(Long idRoteiro, LocalDate dataSessao) {
        try {
            roteiroService.atualizarDataRoteiro(idRoteiro, dataSessao);
            MensagensUtils.addInfoMessageFlashScoped("Roteiro do Presidente salvo com sucesso");
            logService.salvar(TipoAcao.ATUALIZOU, "O usuario alterou a data do roteiro", "ID roteiro: " + idRoteiro, "Nova data: " + dataSessao);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            MensagensUtils.addErrorMessageFlashScoped("Erro ao salvar Roteiro");
        }
    }

    private void excluirRoteiro() {
        try {
            var idRoteiro = Long.valueOf(event.getDescription());
            var roteiroSecretario = roteiroService.listarRoteiroSecretarioPorIdRoteiro(idRoteiro);

            if (roteiroSecretario != null) {
                var documentosAcessorios = roteiroService.listarDocumentoAcessorioPorRoteiroSecretario(roteiroSecretario);

                //DELETAR LINKS
                documentosAcessorios.forEach(doc -> roteiroService.excluirLinkPorDocumentoAcessorio(doc));

                //DELETAR DOCUMENTOS ACESSORIOS
                roteiroService.excluirDocumentoAcessorioPorIdRoteiroSecretario(roteiroSecretario.getId());

                //DELETAR ROTEIRO SECRETARIO
                roteiroService.excluirRoteiroSecretarioPorIdRoteiro(idRoteiro);
            }

            //DELETAR ROTEIRO
            roteiroService.excluir(new Roteiro(idRoteiro));

            MensagensUtils.addInfoMessageFlashScoped("Roteiro excluído com sucesso");
        } catch (NumberFormatException e) {
            MensagensUtils.addErrorMessageFlashScoped("Erro ao excluir Roteiro");
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

    private void atualizarRoteiro(Long idRoteiroParaSerAtualizado) {
        try {
            var roteiroResultado = roteiroService.buscarPorId(idRoteiroParaSerAtualizado);
            roteiroResultado.getOrdemDia().getOrdens().sort(Comparator.comparingInt(ordem -> ordem.getOrdem()));

            ordemDiaParaAtualizar = roteiroResultado.getOrdemDia();
            atualizandoRoteiro = true;

            dataSessao = roteiroResultado.getDataSessao();
            modeloRoteiro = roteiroResultado.getModeloRoteiro();
            roteiro = new Roteiro();
            roteiro.setNome(roteiroResultado.getNome());

            excluirRoteiro();
            salvarRoteiro();
            logService.salvar(TipoAcao.ATUALIZOU, "O usuário atualizou o roteiro", roteiroResultado.toString(), "Novo roteiro");
        } catch (NumberFormatException e) {
            MensagensUtils.addErrorMessageFlashScoped("Erro ao atualizar Roteiro");
        }
    }
}
