# ♟️ Projeto de Xadrez em Java (POO)

## 📖 Descrição
Este projeto implementa um jogo de **xadrez** em Java, utilizando os princípios de **Programação Orientada a Objetos (POO)**.  
O objetivo é exercitar conceitos como **herança, polimorfismo, encapsulamento** e **abstração**, além de aplicar boas práticas de design de software.

---

## 🚀 Tecnologias
- **Linguagem:** Java 8+
- **Paradigma:** Programação Orientada a Objetos
- **IDE recomendada:** IntelliJ / Eclipse / VS Code

---

## ⚙️ Funcionalidades
- Movimentação de todas as peças conforme as regras oficiais.
- Verificação de xeque e xeque-mate.
- Validação de movimentos inválidos.
- Sistema de turnos.
- Interface simples em console (texto).

---

## 📸 Demonstração
_Aqui você pode colocar prints do console rodando o jogo ou até um GIF mostrando uma partida._

---

## 📂 Estrutura do Projeto
- `model/` → classes das peças e do tabuleiro  
- `controller/` → regras do jogo e lógica de movimentos  
- `view/` → interface de usuário (console)  
- `Main.java` → ponto de entrada do programa  

---

## 📝 Próximos Passos
- [ ] Adicionar interface gráfica (Java Swing/JavaFX)  
- [ ] Criar testes unitários para validar movimentos  
- [ ] Melhorar mensagens de feedback ao usuário  

---

## 📌 Como rodar
```bash
# Clonar o repositório
git clone https://github.com/seu-usuario/xadrez-poo.git

# Compilar os arquivos (Linux/Mac)
javac src/**/*.java

# Executar o jogo
java src/Main
