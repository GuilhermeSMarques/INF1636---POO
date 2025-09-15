package model;

class Peao extends Peca {
    public Peao(Cor cor) {
        super(cor);
    }

    @Override
    boolean movimentoValido(int origemX, int origemY, int destinoX, int destinoY, Peca[][] tabuleiro) {
        int direcao = (cor == Cor.BRANCO) ? -1 : 1;
        int dx = destinoX - origemX;
        int dy = Math.abs(destinoY - origemY);

        boolean movimentoFrente = dx == direcao && dy == 0 && tabuleiro[destinoX][destinoY] == null;

        boolean primeiraJogada = (cor == Cor.BRANCO && origemX == 6) || (cor == Cor.PRETO && origemX == 1);
        boolean movimentoDuplo = dx == 2 * direcao && dy == 0 && primeiraJogada &&
                tabuleiro[origemX + direcao][origemY] == null && tabuleiro[destinoX][destinoY] == null;

        boolean capturaDiagonal = dx == direcao && dy == 1 && tabuleiro[destinoX][destinoY] != null &&
                tabuleiro[destinoX][destinoY].getCor() != this.cor;

        return movimentoFrente || movimentoDuplo || capturaDiagonal;
    }
}
