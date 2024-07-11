package br.leg.rr.al.supleg.services;

import br.leg.rr.al.supleg.enums.TipoAcao;
import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;

import br.leg.rr.al.supleg.exceptions.DAOException;
import br.leg.rr.al.supleg.models.StatusTramitacao;
import br.leg.rr.al.supleg.repositories.StatusTramitacaoRepository;
import br.leg.rr.al.supleg.utils.MensagensUtils;
import java.util.List;
import javax.inject.Inject;

@ViewScoped
public class StatusTramitacaoService implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private StatusTramitacaoRepository repository;

    @Inject
    private LogService logService;

    //__________________________________________________________________________
    public void salvar(StatusTramitacao model) {
        try {

            if (model.getId() != null) {
                repository.atualizar(model);
                MensagensUtils.addInfoMessageFlashScoped("Status Tramitação atualizada com sucesso.");
                logService.salvar(TipoAcao.ATUALIZOU, "O usuário alterou o status tramitação.", "", model.toString());
            } else {
                if (!validar(model)) {
                    MensagensUtils.addWarnMessageFlashScoped("Já um StatusTramitacao cadastrado com esses dados.");
                } else {
                    repository.salvar(model);
                    MensagensUtils.addInfoMessageFlashScoped("Status Tramitação salva com sucesso.");
                    logService.salvar(TipoAcao.SALVOU, "O usuário salvou o status tramitação.", "", model.toString());
                }
            }
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar StatusTramitacao: " + e.getLocalizedMessage());
        }
    }

    public void excluir(StatusTramitacao model) {
        try {
            repository.excluir(model);
            MensagensUtils.addInfoMessageFlashScoped("Status excluído com sucesso.");
            logService.salvar(TipoAcao.APAGOU, "O usuário excluiu o status tramitação.", model.toString(), "");
        } catch (Exception e) {
            throw new DAOException("Erro ao Excluir StatusTramitacao: " + e.getLocalizedMessage());
        }
    }

    public List<StatusTramitacao> listarPorStatus(Boolean status) {
        try {
            return repository.listarPorStatus(status);
        } catch (Exception e) {
            throw new DAOException("Erro ao listar StatusTramitacaos: " + e.getLocalizedMessage());
        }
    }

    public StatusTramitacao buscarPorNome(String nome) {
        try {
            return repository.buscarNome(nome).orElse(null);
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar StatusMaterias: " + e.getLocalizedMessage());
        }
    }

    private Boolean validar(StatusTramitacao model) {
        var sessao = repository.verificarDuplicidade(model);
        var duplicidade = !sessao.isEmpty();

        return !duplicidade;
    }
}
