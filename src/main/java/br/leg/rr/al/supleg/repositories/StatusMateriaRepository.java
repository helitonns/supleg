package br.leg.rr.al.supleg.repositories;

import br.leg.rr.al.supleg.models.StatusMateria;
import java.util.List;
import java.util.Optional;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Heliton Nascimento
 */
@Stateless
public class StatusMateriaRepository{

    @PersistenceContext
    protected EntityManager em;

    public void salvar(StatusMateria o){
        em.persist(o);
    }

    public StatusMateria atualizar(StatusMateria o){
        return em.merge(o);
    }

    public void excluir(StatusMateria model){
        model = em.merge(model);
        em.remove(model);
    }
    
    public Optional<StatusMateria> buscarPorId(Long id){
        return Optional.ofNullable(em.createQuery("SELECT o FROM StatusMateria o WHERE o.id = :id", StatusMateria.class)
                .setParameter("id", id)
                .getSingleResult());
    }
    
    public Optional<StatusMateria> buscarNome(String nome){
        return Optional.ofNullable(em.createQuery("SELECT o FROM StatusMateria o WHERE UPPER(o.nome) LIKE :nome", StatusMateria.class)
                .setParameter("nome", "%" + nome + "%")
                .getSingleResult());
    }
    
    public List<StatusMateria> listarPorStatus(Boolean status){
        return em.createQuery("SELECT o FROM StatusMateria o WHERE o.status = :status ORDER BY o.nome", StatusMateria.class)
                .setParameter("status", status)
                .getResultList();
    }
    
    public List<StatusMateria> verificarDuplicidade(StatusMateria model){
        return em.createQuery("SELECT o FROM StatusMateria o WHERE o.nome = :nome", StatusMateria.class)
                .setParameter("nome", model.getNome())
        	.setMaxResults(1)
        	.getResultList();
    }
}
