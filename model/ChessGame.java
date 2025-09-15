package model;

import java.util.List;
import java.awt.Point;
import controller.Observado;
import controller.Observador;
import java.io.*;

public class ChessGame implements Observado {
    private static ChessGame instancia;
    private Tabuleiro tabuleiro;
    private Cor jogadorAtual;
    private final List<Observador> observadores = new java.util.ArrayList<>();
    private boolean fimDeJogo = false;
    public boolean isFimDeJogo() {
        return fimDeJogo;
    }

    private ChessGame() {
        tabuleiro = new Tabuleiro();
        jogadorAtual = Cor.BRANCO;
    }

    public static ChessGame getInstance() {
        if (instancia == null) {
            instancia = new ChessGame();
        }
        return instancia;
    }
    
    @Override
    public void adicionarObservador(Observador o) {
        observadores.add(o);
    }

    @Override
    public void removerObservador(Observador o) {
        observadores.remove(o);
    }

    @Override
    public void notificarObservadores() {
        for (Observador o : observadores) {
            o.atualizar();
        }
    }

    public boolean moverPeca(int origemX, int origemY, int destinoX, int destinoY) {
        Cor adversario = (jogadorAtual == Cor.BRANCO) ? Cor.PRETO : Cor.BRANCO;
        boolean sucesso = tabuleiro.moverPeca(origemX, origemY, destinoX, destinoY, jogadorAtual);

        if (sucesso) {
            System.out.println("Verificando xeque-mate para: " + adversario);
            System.out.println("Resultado: " + tabuleiro.xequeMate(adversario));
            
            if (tabuleiro.xequeMate(adversario)) {
            	fimDeJogo = true;
            }

            jogadorAtual = adversario;
            notificarObservadores();
        }

        return sucesso;
    }
    
    public boolean isPromocaoPendente() {
        return tabuleiro.isPromocaoPendente();
    }

    public Cor getCorPromocao() {
        return tabuleiro.getCorPromocao();
    }

    public void aplicarPromocao(String tipo) {
        tabuleiro.aplicarPromocao(tipo);
        notificarObservadores();
    }
    
    public String getUltimaMensagemErro() {
        return tabuleiro.getUltimaMensagemErro();
    }

    public boolean emXeque() {
        return tabuleiro.emXeque(jogadorAtual);
    }

    public boolean xequeMate(Cor cor) {
        return tabuleiro.xequeMate(cor);
    }

    public boolean emXeque(Cor cor) {
        return tabuleiro.emXeque(cor);
    }

    public Cor getJogadorAtual() {
        return jogadorAtual;
    }
    
    public String getNomePeca(int x, int y) {
        return tabuleiro.getNomePeca(x, y);
    }
    
    public List<Point> getMovimentosPossiveis(int linha, int coluna) {
        return tabuleiro.getMovimentosPossiveis(linha, coluna);
    }

    public boolean verificarXequeMate() {
        Cor adversario = (jogadorAtual == Cor.BRANCO) ? Cor.PRETO : Cor.BRANCO;
        return tabuleiro.xequeMate(adversario);
    }

    public void setFimDeJogo(boolean fim) {
        this.fimDeJogo = fim;
    }


    public void reiniciar() {
        tabuleiro = new Tabuleiro();
        jogadorAtual = Cor.BRANCO;
        notificarObservadores();
    }
    
    public void salvarEstado(File arquivo) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo))) {
            String estado = tabuleiro.exportarEstado();
            writer.write(estado);
        }
    }

    public void carregarEstado(File arquivo) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            StringBuilder sb = new StringBuilder();
            String linha;
            while ((linha = reader.readLine()) != null) {
                sb.append(linha).append("\n");
            }
            tabuleiro.importarEstado(sb.toString());
        }
    }
}
