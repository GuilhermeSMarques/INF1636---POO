package controller;

import model.ChessGame;
import view.ChessGUI;
import java.awt.*;
import javax.swing.JOptionPane;
import java.util.List;

public class ChessController {
    private final ChessGame jogo;
    private final ChessGUI view;

    private int origemX = -1, origemY = -1;
    private List<Point> movimentosPossiveis;

    public ChessController(ChessGame jogo, ChessGUI view) {
        this.jogo = jogo;
        this.view = view;
    }

    public void processarClique(int x, int y) {
        if (origemX == -1) {
            String nome = jogo.getNomePeca(y, x);
            if (nome != null && nome.endsWith(jogo.getJogadorAtual().name())) {
                origemX = x;
                origemY = y;
                movimentosPossiveis = jogo.getMovimentosPossiveis(origemY, origemX);
                view.setOrigem(origemX, origemY, movimentosPossiveis);
            }
        } else {
            boolean sucesso = jogo.moverPeca(origemY, origemX, y, x);
            if (!sucesso) {
                String erro = jogo.getUltimaMensagemErro();
                JOptionPane.showMessageDialog(null, erro);
            } else {
                // Checa xeque-mate após movimento bem-sucedido
                if (jogo.verificarXequeMate()) {
                    jogo.setFimDeJogo(true); // atualiza flag do modelo
                }
            }

            origemX = origemY = -1;
            movimentosPossiveis = null;
            view.resetarOrigem();

            jogo.notificarObservadores(); // notifica a GUI sempre, sucesso ou não
        }
    }
}
