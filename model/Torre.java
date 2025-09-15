package model;

class Torre extends Peca {
    public Torre(Cor cor) {
        super(cor);
    }

    @Override
    boolean movimentoValido(int origemX, int origemY, int destinoX, int destinoY, Peca[][] tabuleiro) {
        return origemX == destinoX || origemY == destinoY;
    }
}
