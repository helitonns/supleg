package br.leg.rr.al.supleg.exceptions;

/**
 * Classe que representa uma exceção específica. Usada em métodos que fazem comunicação
 * com o banco de dados.
 * 
 * @author Heliton Nascimento
 * @since 2019-11-26
 * @version 1.0
 */
public class NotFoundException extends DAOException  {

    public NotFoundException() {
    }
    public NotFoundException(String message) {
        super(message);
    }
}
