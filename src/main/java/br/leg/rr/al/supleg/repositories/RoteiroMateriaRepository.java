package br.leg.rr.al.supleg.repositories;

import br.leg.rr.al.supleg.models.RoteiroMateria;
import java.util.List;
import java.util.Optional;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Heliton Nascimento
 */
@Stateless
public class RoteiroMateriaRepository{

    @PersistenceContext
    protected EntityManager em;

    public void salvar(RoteiroMateria o){
        em.persist(o);
    }

    public RoteiroMateria atualizar(RoteiroMateria o){
        return em.merge(o);
    }

    public void excluir(RoteiroMateria model){
        model = em.merge(model);
        em.remove(model);
    }
    
    public Optional<RoteiroMateria> buscarPorId(Long id){
        return Optional.ofNullable(em.createQuery("SELECT o FROM RoteiroMateria o WHERE o.id = :id", RoteiroMateria.class)
                .setParameter("id", id)
                .getSingleResult());
    }
    
    public List<RoteiroMateria> listarPorStatus(Boolean status){
        return em.createQuery("SELECT o FROM RoteiroMateria o WHERE o.status = :status ORDER BY o.tipoMateria.nome", RoteiroMateria.class)
                .setParameter("status", status)
                .getResultList();
    }
    
    public Optional<RoteiroMateria> buscarPorTipoMateriaNome(String nome){
        return Optional.ofNullable(em.createQuery("SELECT o FROM RoteiroMateria o WHERE UPPER(o.tipoMateria.nome) = :nome  ", RoteiroMateria.class)
                .setParameter("nome",  nome.toUpperCase() )
                .getSingleResult());
    }
    
    public List<RoteiroMateria> verificarDuplicidade(RoteiroMateria model){
    return em.createQuery("SELECT o FROM RoteiroMateria o WHERE o.tipoMateria.nome = :nome", RoteiroMateria.class)
            .setParameter("nome", model.getTipoMateria().getNome())
            .setMaxResults(1)
            .getResultList();
    }
}
