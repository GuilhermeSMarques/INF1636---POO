package model;

class Rainha extends Peca {
    public Rainha(Cor cor) {
        super(cor);
    }

    @Override
    boolean movimentoValido(int origemX, int origemY, int destinoX, int destinoY, Peca[][] tabuleiro) {
        if (origemX == destinoX && origemY == destinoY)
            return false; // não pode ficar parada

        int dx = Math.abs(destinoX - origemX);
        int dy = Math.abs(destinoY - origemY);

        // movimento em linha reta (vertical/horizontal) ou diagonal
        return (dx == dy) || (origemX == destinoX) || (origemY == destinoY);
    }
}
