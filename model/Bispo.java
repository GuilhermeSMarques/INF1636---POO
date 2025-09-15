package model;

class Bispo extends Peca {
    public Bispo(Cor cor) {
        super(cor);
    }

    @Override
    boolean movimentoValido(int origemX, int origemY, int destinoX, int destinoY, Peca[][] tabuleiro) {
        int dx = Math.abs(destinoX - origemX);
        int dy = Math.abs(destinoY - origemY);
        return dx == dy;
    }
}
