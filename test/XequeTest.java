package test;

import model.ChessGame;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class XequeTest {

    private ChessGame jogo;

    // Reinicia o jogo antes de cada teste
    @Before
    public void setUp() {
        jogo = ChessGame.getInstance();
        jogo.reiniciar();
    }

    // Testa se o rei fica em xeque após movimento direto da rainha
    @Test
    public void testXequeDireto() {
        jogo.moverPeca(6, 4, 4, 4); // Peão branco libera a rainha
        jogo.moverPeca(1, 5, 2, 5); // Lance preto qualquer
        assertTrue(jogo.moverPeca(7, 3, 3, 7)); // Rainha branca move para posição de ataque
        assertTrue(jogo.emXeque()); // O rei preto está em xeque
    }

    // Testa xeque descoberto (a peça que bloqueava o ataque é movida)
    @Test
    public void testXequeDescoberto() {
        jogo.moverPeca(6, 4, 4, 4); // Peão branco libera a rainha
        jogo.moverPeca(1, 0, 2, 0); // Lance preto qualquer
        jogo.moverPeca(7, 3, 3, 7); // Rainha vai para posição de ataque
        assertFalse(jogo.moverPeca(1, 5, 2, 5)); // Jogada inválida pois deixaria o rei em xeque descoberto
    }
}
