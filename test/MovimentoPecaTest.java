package test;

import model.ChessGame;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MovimentoPecaTest {

    private ChessGame jogo;

    // Inicializa um novo jogo antes de cada teste
    @Before
    public void setUp() {
        jogo = ChessGame.getInstance(); // Singleton
        jogo.reiniciar(); // Reinicia o tabuleiro para estado inicial
    }

    @Test
    public void testReiMovimentoValido() {
        jogo.moverPeca(6, 4, 5, 4);
        jogo.moverPeca(1, 0, 2, 0);
        assertTrue(jogo.moverPeca(7, 4, 6, 4));
    }

    @Test
    public void testReiMovimentoInvalido() {
        jogo.moverPeca(6, 4, 4, 4);
        jogo.moverPeca(1, 0, 2, 0);
        assertFalse(jogo.moverPeca(7, 4, 5, 4));
    }

    @Test
    public void testRainhaMovimentoValido() {
        jogo.moverPeca(6, 3, 4, 3);
        jogo.moverPeca(1, 0, 2, 0);
        assertTrue(jogo.moverPeca(7, 3, 5, 3));
    }

    @Test
    public void testRainhaMovimentoInvalido() {
        jogo.moverPeca(6, 0, 5, 0);
        jogo.moverPeca(1, 0, 2, 0);
        assertFalse(jogo.moverPeca(7, 3, 5, 5));
    }

    @Test
    public void testTorreMovimentoValido() {
        jogo.moverPeca(6, 0, 4, 0);
        jogo.moverPeca(1, 0, 2, 0);
        assertTrue(jogo.moverPeca(7, 0, 6, 0));
    }

    @Test
    public void testTorreMovimentoInvalido() {
        jogo.moverPeca(6, 1, 5, 1);
        jogo.moverPeca(1, 0, 2, 0);
        assertFalse(jogo.moverPeca(7, 0, 5, 2));
    }

    @Test
    public void testBispoMovimentoValido() {
        jogo.moverPeca(6, 3, 4, 3);
        jogo.moverPeca(1, 0, 2, 0);
        assertTrue(jogo.moverPeca(7, 2, 5, 4));
    }

    @Test
    public void testBispoMovimentoInvalido() {
        jogo.moverPeca(6, 2, 5, 2);
        jogo.moverPeca(1, 0, 2, 0);
        assertFalse(jogo.moverPeca(7, 2, 6, 2));
    }

    @Test
    public void testCavaloMovimentoValido() {
        assertTrue(jogo.moverPeca(7, 1, 5, 2));
    }

    @Test
    public void testCavaloMovimentoInvalido() {
        assertFalse(jogo.moverPeca(7, 1, 5, 1));
    }

    @Test
    public void testPeaoMovimentoValido() {
        assertTrue(jogo.moverPeca(6, 4, 4, 4));
    }

    @Test
    public void testPeaoMovimentoInvalido() {
        jogo.moverPeca(6, 4, 5, 4);
        jogo.moverPeca(1, 0, 2, 0);
        assertFalse(jogo.moverPeca(5, 4, 3, 4));
    }
}
