package br.leg.rr.al.supleg.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe que manipula strings.
 * 
 * @author Heliton Nascimento
 * @since 2019-11-26
 * @version 1.0
 * @see String
 */
public class StringUtils {

    /**
     * Método que remove os acentos das palavras e substitui "ç" por "c" e "ñ" por "n".
     * @param s representa a string a ser manipulada.
     * @return retorna a string transformada.
     */
    public static String removerAcentos(String s) {
        String out = s;
        
        out = out.replaceAll("[àáâäã]", "a");
        out = out.replaceAll("[èéêëẽ]", "e");
        out = out.replaceAll("[ìíîïĩ]", "i");
        out = out.replaceAll("[òóôöõ]", "o");
        out = out.replaceAll("[ùúûüũ]", "u");
        out = out.replaceAll("ç", "c");
        out = out.replaceAll("ñ", "n");

        out = out.replaceAll("[ÀÁÂÄÃ]", "A");
        out = out.replaceAll("[ÈÉÊËẼ]", "E");
        out = out.replaceAll("[ÌÍÎÏĨ]", "I");
        out = out.replaceAll("[ÒÓÔÖÕ]", "O");
        out = out.replaceAll("[ÙÚÛÜŨ]", "U");
        out = out.replaceAll("Ç", "C");
        out = out.replaceAll("Ñ", "N");

        return out;
    }
    
    /**
     * Método que retorna a primeira palavra de uma string composta por mais de uma palavra.
     * @param s representa a string a ser manipulada.
     * @return retorna a primeira palavra.
     */
    public static String retornarPrimeiroNome(String s) {
        try {
            String[] resultado = s.split(" ");
            return resultado[0];
        } catch (Exception e) {
        }
        return null;
    }
    
    /**
     * Método que retorna maiuscula a primeira palavra de uma string composta por uma frase.
     *
     */
    public static String capitalizer(String word){

        String[] words = word.split(" ");
        StringBuilder sb = new StringBuilder();
        if (words[0].length() > 0) {
            sb.append(Character.toUpperCase(words[0].charAt(0)) + words[0].subSequence(1, words[0].length()).toString().toLowerCase());
            for (int i = 1; i < words.length; i++) {
                sb.append(" ");
                sb.append(Character.toUpperCase(words[i].charAt(0)) + words[i].subSequence(1, words[i].length()).toString().toLowerCase());
            }
        }
        return  sb.toString();
    }
    
    public static String removerTagsHtml(String html){
        if (html == null || html.isEmpty()) {
            return "";
        }

        // Padrão regex para identificar tags HTML
        Pattern pattern = Pattern.compile("<[^>]*>");
        Matcher matcher = pattern.matcher(html);

        // Substitui todas as tags HTML por uma string vazia
        return matcher.replaceAll("");
    }
    
    public static String removerTagsHtml(String html, String stringSubstituta){
        if (html == null || html.isEmpty()) {
            return "";
        }

        // Padrão regex para identificar tags HTML
        Pattern pattern = Pattern.compile("<[^>]*>");
        Matcher matcher = pattern.matcher(html);

        // Substitui todas as tags HTML por uma string vazia
        return matcher.replaceAll(stringSubstituta);
    }

}
