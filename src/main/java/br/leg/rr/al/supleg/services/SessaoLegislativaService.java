package br.leg.rr.al.supleg.services;

import br.leg.rr.al.supleg.enums.TipoAcao;
import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;

import br.leg.rr.al.supleg.exceptions.DAOException;
import br.leg.rr.al.supleg.models.SessaoLegislativa;
import br.leg.rr.al.supleg.repositories.SessaoLegislativaRepository;
import br.leg.rr.al.supleg.utils.MensagensUtils;
import java.util.List;
import javax.inject.Inject;

@ViewScoped
public class SessaoLegislativaService implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private SessaoLegislativaRepository repository;

    @Inject
    private LogService logService;

    //__________________________________________________________________________
    public void salvar(SessaoLegislativa sessaoLegislativa) {
        try {
            if (sessaoLegislativa.getId() != null) {
                repository.atualizar(sessaoLegislativa);
                MensagensUtils.addInfoMessageFlashScoped("Sessão Legislativa atualizada com sucesso.");
                logService.salvar(TipoAcao.ATUALIZOU, "O usuário alterou a sessão legislativa.", "", sessaoLegislativa.toString());
            } else {
                if (!validar(sessaoLegislativa)) {
                    MensagensUtils.addWarnMessageFlashScoped("Já uma Sessão Legislativa cadastrada com esses dados.");
                } else {
                    repository.salvar(sessaoLegislativa);
                    MensagensUtils.addInfoMessageFlashScoped("Sessão Legislativa salva com sucesso.");
                    logService.salvar(TipoAcao.SALVOU, "O usuário salvou a sessão legislativa.", "", sessaoLegislativa.toString());
                }
            }
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar SessaoLegislativa: " + e.getLocalizedMessage());
        }
    }

    public void excluir(SessaoLegislativa sessaoLegislativa) {
        try {
            repository.excluir(sessaoLegislativa);
            MensagensUtils.addInfoMessageFlashScoped("Sessão Legislativa excluída com sucesso");
            logService.salvar(TipoAcao.APAGOU, "O usuário excluíu a sessão legislativa.", sessaoLegislativa.toString(), "");
        } catch (Exception e) {
            throw new DAOException("Erro ao Excluir SessaoLegislativa: " + e.getLocalizedMessage());
        }
    }

    public List<SessaoLegislativa> listarPorStatus(Boolean status) {
        try {
            return repository.listarPorStatus(status);
        } catch (Exception e) {
            throw new DAOException("Erro ao listar SessaoLegislativas: " + e.getLocalizedMessage());
        }
    }

    public SessaoLegislativa buscarUltimaPorStatus(Boolean status) {
        try {
            return repository.buscarUltimaPorStatus(status);
        } catch (Exception e) {
            throw new DAOException("Erro ao listar SessaoLegislativas: " + e.getLocalizedMessage());
        }
    }

    private Boolean validar(SessaoLegislativa sessaoLegislativa) {
        var sessao = repository.verificarDuplicidade(sessaoLegislativa);
        var duplicidade = !sessao.isEmpty();

        return !duplicidade;
    }
}
