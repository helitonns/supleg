package br.leg.rr.al.supleg.repositories;

import br.leg.rr.al.supleg.models.TipoAutor;
import java.util.List;
import java.util.Optional;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Heliton Nascimento
 */
@Stateless
public class TipoAutorRepository{

    @PersistenceContext
    protected EntityManager em;

    public void salvar(TipoAutor o){
        em.persist(o);
    }

    public TipoAutor atualizar(TipoAutor o){
        return em.merge(o);
    }

    public void excluir(TipoAutor model){
        model = em.merge(model);
        em.remove(model);
    }
    
    public Optional<TipoAutor> buscarPorId(Long id){
        return Optional.ofNullable(em.createQuery("SELECT o FROM TipoAutor o WHERE o.id = :id", TipoAutor.class)
                .setParameter("id", id)
                .getSingleResult());
    }
    
    public Optional<TipoAutor> buscarNome(String nome){
        return Optional.ofNullable(em.createQuery("SELECT o FROM TipoAutor o WHERE UPPER(o.nome) LIKE :nome", TipoAutor.class)
                .setParameter("nome", "%" + nome + "%")
                .getSingleResult());
    }
    
    public List<TipoAutor> listarPorStatus(Boolean status){
        return em.createQuery("SELECT o FROM TipoAutor o WHERE o.status = :status ORDER BY o.nome", TipoAutor.class)
                .setParameter("status", status)
                .getResultList();
    }
    
    public List<TipoAutor> verificarDuplicidade(TipoAutor model){
        return em.createQuery("SELECT o FROM TipoAutor o WHERE o.nome = :nome", TipoAutor.class)
                .setParameter("nome", model.getNome())
        	.setMaxResults(1)
        	.getResultList();
    }
}
