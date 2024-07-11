package br.leg.rr.al.supleg.utils;

import at.favre.lib.crypto.bcrypt.BCrypt;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * <p>
 * Classe que serve para criptogravar uma string com o algoritmo MD5. Geralmente
 * usada para criptografar senha de usuários do sistema.
 * </p>
 * 
 * @author Heliton Nascimento
 * @since 2019-11-26
 * @version 1.0
 */
public class Criptografia {

    /**
     * Método que criptografa uma String.
     * @param senha string que deve ser criptgrafada.
     * @return retorna a string passada criptografada.
     */
    public static String criptografarEmMD5(String senha) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            BigInteger hash = new BigInteger(1, md.digest(senha.getBytes()));
            return String.format("%32x", hash);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Erro ao tentar criptografar a string");
        }
        return null;
    }
    
    public static String criptografarEmBcrypt(String senha) {
        var bcryptHashString = BCrypt.withDefaults().hashToString(12, senha.toCharArray());
        return bcryptHashString;
    }
    
    public static boolean verificarBcrypt(String senha, String bcryptHash) {
        var result = BCrypt.verifyer().verify(senha.toCharArray(), bcryptHash);
        return result.verified;
    }
    
    

}
