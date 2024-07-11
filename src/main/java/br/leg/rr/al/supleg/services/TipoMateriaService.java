package br.leg.rr.al.supleg.services;

import br.leg.rr.al.supleg.enums.TipoAcao;
import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;

import br.leg.rr.al.supleg.exceptions.DAOException;
import br.leg.rr.al.supleg.models.TipoMateria;
import br.leg.rr.al.supleg.repositories.TipoMateriaRepository;
import br.leg.rr.al.supleg.utils.MensagensUtils;
import java.util.List;
import javax.inject.Inject;

@ViewScoped
public class TipoMateriaService implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private TipoMateriaRepository repository;

    @Inject
    private LogService logService;

    //__________________________________________________________________________
    public void salvar(TipoMateria tipoMateria) {
        try {

            if (tipoMateria.getId() != null) {
                repository.atualizar(tipoMateria);
                MensagensUtils.addInfoMessageFlashScoped("Tipo Matéria atualizada com sucesso.");
                logService.salvar(TipoAcao.ATUALIZOU, "O usuário alterou o tipo matéria.", "", tipoMateria.toString());
            } else {
                if (!validar(tipoMateria)) {
                    MensagensUtils.addWarnMessageFlashScoped("Já um TipoMateria cadastrado com esses dados.");
                } else {
                    repository.salvar(tipoMateria);
                    MensagensUtils.addInfoMessageFlashScoped("Tipo Matéria salva com sucesso.");
                    logService.salvar(TipoAcao.SALVOU, "O usuário salvou o tipo matéria.", "", tipoMateria.toString());
                }
            }
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar TipoMateria: " + e.getLocalizedMessage());
        }
    }

    public void excluir(TipoMateria tipoMateria) {
        try {
            repository.excluir(tipoMateria);
            MensagensUtils.addInfoMessageFlashScoped("Tipo Matéria excluído com sucesso.");
            logService.salvar(TipoAcao.APAGOU, "O usuário excluiu o tipo matéria.", tipoMateria.toString(), "");
        } catch (Exception e) {
            throw new DAOException("Erro ao Excluir TipoMateria: " + e.getLocalizedMessage());
        }
    }

    public List<TipoMateria> listarPorStatus(Boolean status) {
        try {
            return repository.listarPorStatus(status);
        } catch (Exception e) {
            throw new DAOException("Erro ao listar TipoMateriaes: " + e.getLocalizedMessage());
        }
    }

    private Boolean validar(TipoMateria tipoMateria) {
        var sessao = repository.verificarDuplicidade(tipoMateria);
        var duplicidade = !sessao.isEmpty();

        return !duplicidade;
    }
}
