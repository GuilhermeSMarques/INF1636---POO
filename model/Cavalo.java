package model;

class Cavalo extends Peca {
    public Cavalo(Cor cor) {
        super(cor);
    }

    @Override
    boolean movimentoValido(int origemX, int origemY, int destinoX, int destinoY, Peca[][] tabuleiro) {
        int dx = Math.abs(destinoX - origemX);
        int dy = Math.abs(destinoY - origemY);
        return (dx == 2 && dy == 1) || (dx == 1 && dy == 2);
    }
}
