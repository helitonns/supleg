package br.leg.rr.al.supleg.services;

import br.leg.rr.al.supleg.enums.TipoAcao;
import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;

import br.leg.rr.al.supleg.exceptions.DAOException;
import br.leg.rr.al.supleg.models.Abertura;
import br.leg.rr.al.supleg.repositories.AberturaRepository;
import br.leg.rr.al.supleg.utils.MensagensUtils;
import java.util.List;
import javax.inject.Inject;

@ViewScoped
public class AberturaService implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private AberturaRepository repository;

    @Inject
    private LogService logService;

    //__________________________________________________________________________
    public void salvar(Abertura model) {
        try {
            if (model.getId() != null) {
                repository.atualizar(model);
                MensagensUtils.addInfoMessageFlashScoped("Abertura atualizada com sucesso.");
                logService.salvar(TipoAcao.ATUALIZOU, "O usuário alterou a abertura.", "", model.toString());
            } else {
                if (!validar(model)) {
                    MensagensUtils.addWarnMessageFlashScoped("Já uma Abertura cadastrada com esses dados.");
                } else {
                    repository.salvar(model);
                    MensagensUtils.addInfoMessageFlashScoped("Abertura salva com sucesso.");
                    logService.salvar(TipoAcao.SALVOU, "O usuário salvou a abertura.", "", model.toString());
                }
            }
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar Abertura: " + e.getLocalizedMessage());
        }
    }

    public void excluir(Abertura abertura) {
        try {
            repository.excluir(abertura);
            MensagensUtils.addInfoMessageFlashScoped("Abertura excluída com sucesso.");
            logService.salvar(TipoAcao.APAGOU, "O usuário excluiu a abertura.", abertura.toString(), "");
        } catch (Exception e) {
            throw new DAOException("Erro ao Excluir Abertura: " + e.getLocalizedMessage());
        }
    }

    public List<Abertura> listarPorStatus(Boolean status) {
        try {
            return repository.listarPorStatus(status);
        } catch (Exception e) {
            throw new DAOException("Erro ao listar Aberturas: " + e.getLocalizedMessage());
        }
    }

    private Boolean validar(Abertura model) {
        var sessao = repository.verificarDuplicidade(model);
        var duplicidade = !sessao.isEmpty();

        return !duplicidade;
    }
}
