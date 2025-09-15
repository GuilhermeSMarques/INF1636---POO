package model;

class Rei extends Peca {
    public Rei(Cor cor) {
        super(cor);
    }

    @Override
    boolean movimentoValido(int origemX, int origemY, int destinoX, int destinoY, Peca[][] tabuleiro) {
        int dx = Math.abs(destinoX - origemX);
        int dy = Math.abs(destinoY - origemY);

        // movimento padrão do rei
        if (dx <= 1 && dy <= 1) return true;

        // tentativa de roque
        if (!jaMoveu && dx == 0 && dy == 2) {
            int linha = origemX;
            int colunaRei = origemY;
            int colunaDestino = destinoY;

            // Roque pequeno (torre na direita)
            if (colunaDestino > colunaRei) {
                Peca torre = tabuleiro[linha][7];
                return torre instanceof Torre && !torre.jaFoiMovida() &&
                       tabuleiro[linha][5] == null &&
                       tabuleiro[linha][6] == null;
            }

            // Roque grande (torre na esquerda)
            if (colunaDestino < colunaRei) {
                Peca torre = tabuleiro[linha][0];
                return torre instanceof Torre && !torre.jaFoiMovida() &&
                       tabuleiro[linha][1] == null &&
                       tabuleiro[linha][2] == null &&
                       tabuleiro[linha][3] == null;
            }
        }

        return false;
    }
}

