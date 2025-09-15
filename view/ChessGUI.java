package view;

import model.ChessGame;
import model.Cor;
import controller.ChessController;
import controller.Observador;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class ChessGUI extends JFrame implements Observador {
    private static final int TAM_CASA = 100;
    private final ChessGame jogo = ChessGame.getInstance();
    private final Map<String, BufferedImage> imagens = new HashMap<>();
    private int origemX = -1, origemY = -1;
    private java.util.List<Point> movimentosPossiveis = new ArrayList<>();
    private ChessController controller;

    public ChessGUI() {
        super("Xadrez Visual");
        setSize(8 * TAM_CASA + 16, 8 * TAM_CASA + 39);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        carregarImagens();
        controller = new ChessController(jogo, this);
        jogo.adicionarObservador(this);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX() / TAM_CASA;
                int y = e.getY() / TAM_CASA;

                if (SwingUtilities.isRightMouseButton(e)) {
                    mostrarMenuDireito(e);
                } else {
                    controller.processarClique(x, y);
                }
            }
        });

        setVisible(true);
    }

    private void carregarImagens() {
        try {
            imagens.put("Rei_BRANCO", ImageIO.read(new File("CyanK.png")));
            imagens.put("Rainha_BRANCO", ImageIO.read(new File("CyanQ.png")));
            imagens.put("Torre_BRANCO", ImageIO.read(new File("CyanR.png")));
            imagens.put("Bispo_BRANCO", ImageIO.read(new File("CyanB.png")));
            imagens.put("Cavalo_BRANCO", ImageIO.read(new File("CyanN.png")));
            imagens.put("Peao_BRANCO", ImageIO.read(new File("CyanP.png")));

            imagens.put("Rei_PRETO", ImageIO.read(new File("PurpleK.png")));
            imagens.put("Rainha_PRETO", ImageIO.read(new File("PurpleQ.png")));
            imagens.put("Torre_PRETO", ImageIO.read(new File("PurpleR.png")));
            imagens.put("Bispo_PRETO", ImageIO.read(new File("PurpleB.png")));
            imagens.put("Cavalo_PRETO", ImageIO.read(new File("PurpleN.png")));
            imagens.put("Peao_PRETO", ImageIO.read(new File("PurpleP.png")));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar imagens: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;

        Insets insets = getInsets();
        int offsetX = insets.left;
        int offsetY = insets.top;

        desenharTabuleiro(g2, offsetX, offsetY);
        destacarMovimentosPossiveis(g2, offsetX, offsetY);
        desenharPecas(g2, offsetX, offsetY);
    }

    private void desenharTabuleiro(Graphics2D g2, int offsetX, int offsetY) {
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                boolean clara = (x + y) % 2 == 0;
                g2.setColor(clara ? Color.WHITE : Color.GRAY);
                g2.fillRect(offsetX + x * TAM_CASA, offsetY + y * TAM_CASA, TAM_CASA, TAM_CASA);
                g2.setColor(Color.BLACK);
                g2.drawRect(offsetX + x * TAM_CASA, offsetY + y * TAM_CASA, TAM_CASA, TAM_CASA);
            }
        }
    }

    private void destacarMovimentosPossiveis(Graphics2D g2, int offsetX, int offsetY) {
        if (origemX != -1 && origemY != -1) {
            String nomeOrigem = jogo.getNomePeca(origemY, origemX);
            Cor corOrigem = (nomeOrigem != null && nomeOrigem.endsWith("BRANCO")) ? Cor.BRANCO : Cor.PRETO;

            for (Point p : movimentosPossiveis) {
                String ocupante = jogo.getNomePeca(p.y, p.x);
                boolean mesmaCor = ocupante != null && ocupante.endsWith(corOrigem.name());
                if (!mesmaCor) {
                    g2.setColor(corOrigem == Cor.BRANCO ? new Color(0, 255, 0, 128) : new Color(255, 105, 180, 128));
                    g2.fillRect(offsetX + p.x * TAM_CASA, offsetY + p.y * TAM_CASA, TAM_CASA, TAM_CASA);
                }
            }
        }
    }

    private void desenharPecas(Graphics2D g2, int offsetX, int offsetY) {
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                String nome = jogo.getNomePeca(y, x);
                if (nome != null) {
                    BufferedImage img = imagens.get(nome);
                    if (img != null) {
                        g2.drawImage(img, offsetX + x * TAM_CASA, offsetY + y * TAM_CASA, TAM_CASA, TAM_CASA, null);
                    }
                }
            }
        }
    }

    private void mostrarMenuDireito(MouseEvent e) {
        JPopupMenu menu = new JPopupMenu();

        JMenuItem salvar = new JMenuItem("Salvar e Sair");
        salvar.addActionListener(evt -> salvarPartida());

        JMenuItem carregar = new JMenuItem("Carregar Partida");
        carregar.addActionListener(evt -> carregarPartida());

        JMenuItem sair = new JMenuItem("Sair sem Salvar");
        sair.addActionListener(evt -> confirmarSaida());

        menu.add(salvar);
        menu.add(carregar);
        menu.add(sair);

        menu.show(this, e.getX(), e.getY());
    }

    private void confirmarSaida() {
        int opcao = JOptionPane.showConfirmDialog(
            this,
            "Tem certeza que deseja encerrar a partida sem salvar?",
            "Confirmar saída",
            JOptionPane.YES_NO_OPTION
        );

        if (opcao == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private void salvarPartida() {
        JFileChooser fileChooser = new JFileChooser();
        int escolha = fileChooser.showSaveDialog(this);
        if (escolha == JFileChooser.APPROVE_OPTION) {
            try {
                jogo.salvarEstado(fileChooser.getSelectedFile());
                JOptionPane.showMessageDialog(this, "Jogo salvo com sucesso!");
                System.exit(0);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar o jogo: " + ex.getMessage());
            }
        }
    }

    private void carregarPartida() {
        JFileChooser fileChooser = new JFileChooser();
        int escolha = fileChooser.showOpenDialog(this);
        if (escolha == JFileChooser.APPROVE_OPTION) {
            try {
                jogo.carregarEstado(fileChooser.getSelectedFile());
                repaint();
                JOptionPane.showMessageDialog(this, "Jogo carregado com sucesso!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar o jogo: " + ex.getMessage());
            }
        }
    }

    private void exibirMenuPromocao() {
        String[] opcoes = {"Rainha", "Torre", "Bispo", "Cavalo"};
        Cor cor = jogo.getCorPromocao();

        String escolha = (String) JOptionPane.showInputDialog(
            this,
            "Escolha a peça para promoção:",
            "Promoção de Peão - " + cor.name(),
            JOptionPane.PLAIN_MESSAGE,
            null,
            opcoes,
            "Rainha"
        );

        if (escolha != null) {
            jogo.aplicarPromocao(escolha);
            jogo.notificarObservadores();
        }
    }

    @Override
    public void atualizar() {
        repaint();

        if (jogo.isPromocaoPendente()) {
            exibirMenuPromocao();
        }

        if (jogo.isFimDeJogo()) {
            JOptionPane.showMessageDialog(
                this,
                "Xeque-mate! O jogador " + jogo.getJogadorAtual().name() + " venceu a partida.",
                "Fim de Jogo",
                JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    public void setOrigem(int x, int y, java.util.List<Point> movimentos) {
        this.origemX = x;
        this.origemY = y;
        this.movimentosPossiveis = movimentos;
        atualizar();
    }

    public void resetarOrigem() {
        origemX = -1;
        origemY = -1;
        movimentosPossiveis.clear();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String[] opcoes = {"Nova Partida", "Carregar Partida"};
            int escolha = JOptionPane.showOptionDialog(
                null,
                "Escolha uma opção:",
                "Xadrez",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcoes,
                opcoes[0]
            );

            if (escolha == 1) {
                JFileChooser fileChooser = new JFileChooser();
                int resultado = fileChooser.showOpenDialog(null);

                if (resultado == JFileChooser.APPROVE_OPTION) {
                    try {
                        ChessGame.getInstance().carregarEstado(fileChooser.getSelectedFile());
                        new ChessGUI();
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Erro ao carregar o jogo: " + ex.getMessage());
                    }
                }
            } else if (escolha == 0) {
                new ChessGUI();
            }
        });
    }
}
