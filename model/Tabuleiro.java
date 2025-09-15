package model;

import java.util.List;

import java.util.ArrayList;
import java.awt.Point;

class Tabuleiro {
    private Peca[][] pecas = new Peca[8][8];

    public Tabuleiro() {
        inicializar();
    }

    private void inicializar() {
        pecas[0][0] = new Torre(Cor.PRETO);
        pecas[0][1] = new Cavalo(Cor.PRETO);
        pecas[0][2] = new Bispo(Cor.PRETO);
        pecas[0][3] = new Rainha(Cor.PRETO);
        pecas[0][4] = new Rei(Cor.PRETO);
        pecas[0][5] = new Bispo(Cor.PRETO);
        pecas[0][6] = new Cavalo(Cor.PRETO);
        pecas[0][7] = new Torre(Cor.PRETO);
        for (int i = 0; i < 8; i++) {
            pecas[1][i] = new Peao(Cor.PRETO);
        }

        pecas[7][0] = new Torre(Cor.BRANCO);
        pecas[7][1] = new Cavalo(Cor.BRANCO);
        pecas[7][2] = new Bispo(Cor.BRANCO);
        pecas[7][3] = new Rainha(Cor.BRANCO);
        pecas[7][4] = new Rei(Cor.BRANCO);
        pecas[7][5] = new Bispo(Cor.BRANCO);
        pecas[7][6] = new Cavalo(Cor.BRANCO);
        pecas[7][7] = new Torre(Cor.BRANCO);
        for (int i = 0; i < 8; i++) {
            pecas[6][i] = new Peao(Cor.BRANCO);
        }
    }
    
    private String ultimaMensagemErro = "";
    private boolean promocaoPendente = false;
    private int xPromocao;
    private int yPromocao;
    private Cor corPromocao;

    boolean isPromocaoPendente() {
        return promocaoPendente;
    }

    Cor getCorPromocao() {
        return corPromocao;
    }

    void aplicarPromocao(String tipo) {
        if (!promocaoPendente) return;

        Peca nova;
        switch (tipo) {
        case "Torre":
            nova = new Torre(corPromocao);
            break;
        case "Bispo":
            nova = new Bispo(corPromocao);
            break;
        case "Cavalo":
            nova = new Cavalo(corPromocao);
            break;
        default:
            nova = new Rainha(corPromocao);
            break;
    }

        pecas[xPromocao][yPromocao] = nova;
        promocaoPendente = false;
    }
    
    String getUltimaMensagemErro() {
        return ultimaMensagemErro;
    }

    boolean moverPeca(int origemX, int origemY, int destinoX, int destinoY, Cor jogador) {
        if (!posicaoValida(origemX, origemY) || !posicaoValida(destinoX, destinoY)) {
            ultimaMensagemErro = "Posição fora do tabuleiro.";
            return false;
        }

        Peca origem = pecas[origemX][origemY];
        if (origem == null || origem.getCor() != jogador) {
            ultimaMensagemErro = "Não há peça sua nessa posição.";
            return false;
        }

        if (!origem.movimentoValido(origemX, origemY, destinoX, destinoY, pecas)) {
            ultimaMensagemErro = "Movimento não permitido para essa peça.";
            return false;
        }

        if (!caminhoLivre(origemX, origemY, destinoX, destinoY, origem)) {
            ultimaMensagemErro = "Há peças no caminho.";
            return false;
        }

        Peca destino = pecas[destinoX][destinoY];
        if (destino != null && destino.getCor() == jogador) {
            ultimaMensagemErro = "Você não pode capturar sua própria peça.";
            return false;
        }

        Peca[][] backup = copiarTabuleiro();
        boolean roque = origem instanceof Rei && Math.abs(destinoY - origemY) == 2;

        pecas[destinoX][destinoY] = origem;
        pecas[origemX][origemY] = null;

        if (roque) {
            if (destinoY > origemY) {
                Peca torre = pecas[origemX][7];
                pecas[origemX][5] = torre;
                pecas[origemX][7] = null;
            } else {
                Peca torre = pecas[origemX][0];
                pecas[origemX][3] = torre;
                pecas[origemX][0] = null;
            }
        }

        if (emXeque(jogador)) {
            pecas = backup;
            ultimaMensagemErro = "Movimento deixaria o rei em xeque!";
            return false;
        }

        origem.setJaMoveu(true);

        if (roque) {
            if (destinoY > origemY) {
                Peca torre = pecas[origemX][5];
                torre.setJaMoveu(true);
            } else {
                Peca torre = pecas[origemX][3];
                torre.setJaMoveu(true);
            }
        }

        if (origem instanceof Peao) {
            int fimLinha = (jogador == Cor.BRANCO) ? 0 : 7;
            if (destinoX == fimLinha) {
                promocaoPendente = true;
                xPromocao = destinoX;
                yPromocao = destinoY;
                corPromocao = jogador;
            }
        }
        return true;
    }

    boolean emXeque(Cor cor) {
        int[] reiPos = encontrarRei(cor);
        if (reiPos == null) return false;

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Peca p = pecas[x][y];
                if (p != null && p.getCor() != cor) {
                    if (p.movimentoValido(x, y, reiPos[0], reiPos[1], pecas) &&
                        caminhoLivre(x, y, reiPos[0], reiPos[1], p)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    private boolean existeLanceQueRemoveXeque(Cor cor) {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Peca p = pecas[x][y];
                if (p != null && p.getCor() == cor) {
                    List<Point> movimentos = getMovimentosPossiveis(x, y);
                    for (Point destino : movimentos) {
                        Peca[][] backup = copiarTabuleiro();

                        pecas[destino.x][destino.y] = p;
                        pecas[x][y] = null;

                        if (!emXeque(cor)) {
                            // Reporta a peça e o movimento encontrado que remove o xeque
                            System.out.println("Defesa possível: " +
                                p.getClass().getSimpleName() + " em (" + x + "," + y + 
                                ") pode mover para (" + destino.x + "," + destino.y + ")");

                            pecas = backup; // restaura tabuleiro
                            return true; // achou jogada que tira o xeque
                        }

                        pecas = backup; // restaura tabuleiro
                    }
                }
            }
        }
        return false;
    }



    public boolean xequeMate(Cor cor) {
        if (!emXeque(cor)) return false; // não em xeque -> não é mate

        int[] reiPos = encontrarRei(cor);
        int reiX = reiPos[0], reiY = reiPos[1];

        // Direções que o rei pode tentar fugir: 8 direções em volta
        int[][] direcoes = {
            {1, 0}, {-1, 0}, {0, 1}, {0, -1},
            {1, 1}, {1, -1}, {-1, 1}, {-1, -1}
        };

        for (int[] dir : direcoes) {
            int novoX = reiX + dir[0];
            int novoY = reiY + dir[1];

            if (!posicaoValida(novoX, novoY)) continue; // fora do tabuleiro

            Peca destino = pecas[novoX][novoY];
            if (destino != null && destino.getCor() == cor) continue; // casa ocupada por peça da mesma cor

            // Simular movimento do rei
            Peca[][] backup = copiarTabuleiro();
            pecas[novoX][novoY] = pecas[reiX][reiY]; // move o rei
            pecas[reiX][reiY] = null;

            if (!emXeque(cor)) {
                // encontrou movimento que tira o rei do xeque
                pecas = backup; // restaura tabuleiro
                return false;
            }

            pecas = backup; // restaura tabuleiro
        }

        // Não há fuga para o rei, então verifica se alguma outra peça pode bloquear ou capturar
        if (existeLanceQueRemoveXeque(cor)) {
        	System.out.println("há defesa possível");
            return false; // há defesa possível
        }

        return true;
    }


    
    String getNomePeca(int x, int y) {
        if (pecas[x][y] == null) return null;
        return pecas[x][y].getClass().getSimpleName() + "_" + pecas[x][y].getCor().name();
    }

    private boolean caminhoLivre(int x1, int y1, int x2, int y2, Peca peca) {
        if (peca instanceof Cavalo) return true;

        int dx = Integer.compare(x2, x1);
        int dy = Integer.compare(y2, y1);
        int x = x1 + dx;
        int y = y1 + dy;

        while (x != x2 || y != y2) {
            if (pecas[x][y] != null) return false;
            x += dx;
            y += dy;
        }
        return true;
    }

    private int[] encontrarRei(Cor cor) {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Peca p = pecas[x][y];
                if (p instanceof Rei && p.getCor() == cor) {
                    return new int[]{x, y};
                }
            }
        }
        return null;
    }

    private Peca[][] copiarTabuleiro() {
        Peca[][] copia = new Peca[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Peca p = pecas[i][j];
                if (p == null) {
                    copia[i][j] = null;
                } else if (p instanceof Rei) {
                    copia[i][j] = new Rei(p.getCor());
                    copia[i][j].setJaMoveu(p.jaFoiMovida());
                } else if (p instanceof Torre) {
                    copia[i][j] = new Torre(p.getCor());
                    copia[i][j].setJaMoveu(p.jaFoiMovida());
                } else if (p instanceof Rainha) {
                    copia[i][j] = new Rainha(p.getCor());
                } else if (p instanceof Bispo) {
                    copia[i][j] = new Bispo(p.getCor());
                } else if (p instanceof Cavalo) {
                    copia[i][j] = new Cavalo(p.getCor());
                } else if (p instanceof Peao) {
                    copia[i][j] = new Peao(p.getCor());
                    copia[i][j].setJaMoveu(p.jaFoiMovida());
                }
            }
        }
        return copia;
    }
    
    public void setPecas(Peca[][] pecas) {
        this.pecas = pecas;
    }

    private boolean simulaMovimentoEChecaXeque(int x1, int y1, int x2, int y2, Cor cor) {

        Peca[][] backup = copiarTabuleiro();

        Peca pecaMovida = backup[x1][y1];
        backup[x2][y2] = pecaMovida;
        backup[x1][y1] = null;

        Tabuleiro simulado = new Tabuleiro();
        simulado.setPecas(backup);

        return simulado.emXeque(cor);
    }


    public List<Point> getMovimentosPossiveis(int linha, int coluna) {
        List<Point> movimentos = new ArrayList<>();
        Peca peca = pecas[linha][coluna];
        if (peca == null) return movimentos;

        Cor cor = peca.getCor();

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
            	if (linha == y && coluna == x) continue;
                if (!peca.movimentoValido(linha, coluna, y, x, pecas)) continue;
                if (!caminhoLivre(linha, coluna, y, x, peca)) continue;
                if (!posicaoValida(x, y)) continue;
                if (!simulaMovimentoEChecaXeque(linha, coluna, y, x, cor)) {
                    movimentos.add(new Point(x, y));
                }
            }
        }

        return movimentos;
    }

    private boolean posicaoValida(int x, int y) {
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }
    
    String exportarEstado() {
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                Peca p = pecas[y][x];
                if (p == null) {
                    sb.append("-");
                } else {
                    char cor = (p.getCor() == Cor.BRANCO) ? 'B' : 'P';
                    char tipo;

                    if (p instanceof Rainha) {
                        tipo = 'Q'; // Rainha deve ser exportada como Q
                    } else {
                        tipo = p.getClass().getSimpleName().charAt(0);
                    }

                    sb.append(tipo).append(cor);
                }
                sb.append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }


    void importarEstado(String estado) {
        String[] linhas = estado.split("\n");
        for (int y = 0; y < 8; y++) {
            String[] tokens = linhas[y].split(" ");
            for (int x = 0; x < 8; x++) {
                String token = tokens[x];
                if (token.equals("-")) {
                    pecas[y][x] = null;
                } else {
                    char tipo = token.charAt(0);
                    char corChar = token.charAt(1);
                    Cor cor = (corChar == 'B') ? Cor.BRANCO : Cor.PRETO;

                    switch (tipo) {
                    case 'T':
                        pecas[y][x] = new Torre(cor);
                        break;
                    case 'C':
                        pecas[y][x] = new Cavalo(cor);
                        break;
                    case 'B':
                        pecas[y][x] = new Bispo(cor);
                        break;
                    case 'R':
                        pecas[y][x] = new Rei(cor);
                        break;
                    case 'Q':
                        pecas[y][x] = new Rainha(cor);
                        break;
                    case 'P':
                        pecas[y][x] = new Peao(cor);
                        break;
                    default:
                        throw new IllegalArgumentException("Tipo de peça desconhecido: " + tipo);
                    }
                }
            }
        }
    }
}
