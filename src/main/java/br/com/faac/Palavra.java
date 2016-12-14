package br.com.faac;

public final class Palavra {
    private final String palavra;
    private int quantidade;
    
    public Palavra(String palavra){
        this.palavra = palavra;
        this.quantidade = 0;
    }
    
    public void setQuantidade(){
        this.quantidade++;
    }
    
    public String getPalavra(){
        return this.palavra;
    }
    
    public int getQuantidade(){
        return this.quantidade;
    }
    
    public boolean igual(Palavra palavra){
        return this.palavra.equals(palavra.getPalavra());
    }
}
