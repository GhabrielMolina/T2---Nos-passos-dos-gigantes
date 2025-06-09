import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class App {

    // Representa uma coordenada no mapa para maior clareza
    private record Ponto(int linha, int coluna) {}

    // Contém o mapa e a posição inicial após a leitura do arquivo
    private record DadosMapa(char[][] mapa, Ponto inicio) {}

    public static void main(String[] args) {
        System.out.println("--- Nos Passos dos Gigantes: Solução ---");
        
        // Automatiza a execução para todos os casos de teste de 'a' a 'h'
        String[] casosDeTeste = {"a", "b", "c", "d", "e", "f", "h"};

        for (String casoId : casosDeTeste) {
            try {
                System.out.println("\nProcessando caso de teste: c1" + casoId + ".txt");
                
                // 1. Carregar o mapa do arquivo
                DadosMapa dados = carregarMapa(casoId);
                char[][] mapa = dados.mapa();
                Ponto pontoInicial = dados.inicio();

                int numLinhas = mapa.length;
                int numColunas = mapa[0].length;
                
                // 2. Construir o grafo a partir do mapa
                Grafo grafo = construirGrafo(mapa, numLinhas, numColunas);
                
                // 3. Executar a busca em largura (BFS) a partir do ponto 'S'
                int verticeInicialIdx = pontoInicial.linha() * numColunas + pontoInicial.coluna();
                BuscaEmLargura bfs = new BuscaEmLargura(grafo, verticeInicialIdx);
                
                // 4. Encontrar o menor caminho até uma pedra 'z'
                int menorCaminho = encontrarMenorCaminhoParaZ(mapa, bfs, numLinhas, numColunas);

                if (menorCaminho != Integer.MAX_VALUE) {
                    System.out.println("Resultado: O menor número de passos para chegar em 'z' é " + menorCaminho);
                } else {
                    System.out.println("Resultado: Não foi encontrado um caminho de 'S' até 'z'.");
                }

            } catch (IOException e) {
                System.err.println("Erro de E/S ao processar o arquivo do caso " + casoId + ": " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Ocorreu um erro inesperado no caso " + casoId + ": " + e.getMessage());
            }
        }
        System.out.println("\n--- Fim da execução ---");
    }

    /**
     * Lê o arquivo de mapa, cria a matriz de caracteres e encontra a posição inicial 'S'.
     */
    private static DadosMapa carregarMapa(String arqId) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader("./lib/c1" + arqId + ".txt"))) {
            String linhaDim = br.readLine();
            if (linhaDim == null) throw new IOException("Arquivo vazio.");
            
            String[] dimensoes = linhaDim.split(" ");
            int numLinhas = Integer.parseInt(dimensoes[0]);
            int numColunas = Integer.parseInt(dimensoes[1]);

            char[][] mapa = new char[numLinhas][numColunas];
            Ponto pontoInicial = null;

            for (int i = 0; i < numLinhas; i++) {
                String linhaMapa = br.readLine();
                if (linhaMapa == null) throw new IOException("Dados do mapa incompletos.");
                for (int j = 0; j < numColunas; j++) {
                    mapa[i][j] = linhaMapa.charAt(j);
                    if (mapa[i][j] == 'S') {
                        pontoInicial = new Ponto(i, j);
                    }
                }
            }
            
            if (pontoInicial == null) throw new IOException("Ponto inicial 'S' não encontrado.");
            return new DadosMapa(mapa, pontoInicial);
        }
    }

    /**
     * Constrói o grafo a partir da matriz do mapa, adicionando arestas direcionadas
     * com base nas regras de movimento.
     */
    private static Grafo construirGrafo(char[][] mapa, int numLinhas, int numColunas) {
        int numVertices = numLinhas * numColunas;
        Grafo grafo = new Grafo(numVertices);

        int[] dr = {-1, -1, 0, 1, 1, 1, 0, -1}; // Deltas de linha (8 direções)
        int[] dc = {0, 1, 1, 1, 0, -1, -1, -1};  // Deltas de coluna (8 direções)

        for (int l = 0; l < numLinhas; l++) {
            for (int c = 0; c < numColunas; c++) {
                int verticeAtualIdx = l * numColunas + c;
                int alturaAtual = getAltura(mapa[l][c]);

                for (int i = 0; i < 8; i++) {
                    int vizinhoL = l + dr[i];
                    int vizinhoC = c + dc[i];

                    if (vizinhoL >= 0 && vizinhoL < numLinhas && vizinhoC >= 0 && vizinhoC < numColunas) {
                        int alturaVizinho = getAltura(mapa[vizinhoL][vizinhoC]);
                        
                        if (alturaVizinho <= alturaAtual + 1) {
                            int verticeVizinhoIdx = vizinhoL * numColunas + vizinhoC;
                            grafo.adicionarAresta(verticeAtualIdx, verticeVizinhoIdx);
                        }
                    }
                }
            }
        }
        return grafo;
    }

    /**
     * Itera por todas as pedras 'z' do mapa e retorna a menor distância encontrada pela BFS.
     */
    private static int encontrarMenorCaminhoParaZ(char[][] mapa, BuscaEmLargura bfs, int numLinhas, int numColunas) {
        int minPassos = Integer.MAX_VALUE;
        for (int l = 0; l < numLinhas; l++) {
            for (int c = 0; c < numColunas; c++) {
                if (mapa[l][c] == 'z') {
                    int zVertexIdx = l * numColunas + c;
                    if (bfs.temCaminhoPara(zVertexIdx)) {
                        minPassos = Math.min(minPassos, bfs.getDistanciaPara(zVertexIdx));
                    }
                }
            }
        }
        return minPassos;
    }
    
    /** Converte o caractere de uma pedra para sua altura numérica. */
    private static int getAltura(char pedra) {
        if (pedra == 'S') return 0; // 'S' tem a altura de 'a'
        return pedra - 'a';         // 'a' -> 0, 'b' -> 1, ..., 'z' -> 25
    }
}