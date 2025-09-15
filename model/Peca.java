package model;

abstract class Peca {
    protected Cor cor;
    protected boolean jaMoveu = false;

    public Peca(Cor cor) {
        this.cor = cor;
    }

    public Cor getCor() {
        return cor;
    }

    public boolean jaFoiMovida() {
        return jaMoveu;
    }

    public void setJaMoveu(boolean moveu) {
        this.jaMoveu = moveu;
    }

    abstract boolean movimentoValido(int origemX, int origemY, int destinoX, int destinoY, Peca[][] tabuleiro);
}


