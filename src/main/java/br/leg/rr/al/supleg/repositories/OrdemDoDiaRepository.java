package br.leg.rr.al.supleg.repositories;

import br.leg.rr.al.supleg.models.OrdemDia;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Heliton Nascimento
 */
@Stateless
public class OrdemDoDiaRepository{

    @PersistenceContext
    protected EntityManager em;

    public void salvar(OrdemDia o){
        em.persist(o);
    }

    public OrdemDia atualizar(OrdemDia o){
        return em.merge(o);
    }

    public void excluir(OrdemDia model){
        model = em.merge(model);
        em.remove(model);
    }
    
    public Optional<OrdemDia> buscarPorId(Long id){
        return Optional.ofNullable(em.createQuery("SELECT o FROM OrdemDia o WHERE o.id = :id", OrdemDia.class)
                .setParameter("id", id)
                .getSingleResult());
    }
    
    public List<OrdemDia> listarUltimos(Integer quantidade){
        return em.createQuery("SELECT o FROM OrdemDia o ORDER BY o.dataSessao DESC", OrdemDia.class)
                .setMaxResults(quantidade)
                .getResultList();
    }
    
    public Optional<OrdemDia> buscarPorDataSessao(LocalDate dataSessao){
        return Optional.ofNullable(em.createQuery("SELECT o FROM OrdemDia o WHERE o.dataSessao = :dataSessao", OrdemDia.class)
                .setParameter("dataSessao", dataSessao)
                .getSingleResult());
    }
}
