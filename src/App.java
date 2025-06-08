import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

// Você precisará da sua classe CaminhamentoLargura e da classe Graph no mesmo pacote
// ou importá-las adequadamente.

public class App {
    public final static String ARQUIVOID = "b"; // Mude conforme o caso de teste
    public final static boolean DEBUG = false;

    // Método para converter a altura do caractere para um valor numérico
    private static int getAltura(char pedra) {
        if (pedra == 'S') {
            return 'a' - 'a'; // Altura de 'a'
        }
        // Se houver 'E' como fim e tiver altura de 'z', adicione aqui.
        // Para este problema, 'z' é o caractere do fim.
        return pedra - 'a'; // 'a' -> 0, 'b' -> 1, ..., 'z' -> 25
    }

    // Método para ler o arquivo e retornar o mapa como char[][]
    // E também para encontrar a posição inicial 'S' (em coordenadas)
    public static char[][] lerMapa(String arqId, int[] posicaoInicialCoords) throws IOException {
        FileReader fr = new FileReader("./lib/c1" + arqId + ".txt"); // Ajuste o caminho se necessário
        BufferedReader br = new BufferedReader(fr);

        String linhaDim = br.readLine();
        if (linhaDim == null) {
            br.close();
            throw new IOException("Arquivo vazio ou formato incorreto (sem dimensões).");
        }
        String[] dimensoes = linhaDim.split(" ");
        if (dimensoes.length < 2) {
            br.close();
            throw new IOException("Formato das dimensões incorreto.");
        }
        int numLinhas = Integer.parseInt(dimensoes[0]);
        int numColunas = Integer.parseInt(dimensoes[1]);

        char[][] mapa = new char[numLinhas][numColunas];
        boolean inicioEncontrado = false;

        for (int i = 0; i < numLinhas; i++) {
            String linhaMapa = br.readLine();
            if (linhaMapa == null || linhaMapa.length() < numColunas) {
                br.close();
                throw new IOException("Linha do mapa incompleta ou ausente: " + (i + 1));
            }
            for (int j = 0; j < numColunas; j++) {
                mapa[i][j] = linhaMapa.charAt(j);
                if (mapa[i][j] == 'S') {
                    posicaoInicialCoords[0] = i; // linha
                    posicaoInicialCoords[1] = j; // coluna
                    inicioEncontrado = true;
                }
            }
        }
        br.close();

        if (!inicioEncontrado) {
            throw new IOException("Ponto de início 'S' não encontrado no mapa.");
        }
        return mapa;
    }

    // Método para construir o Grafo a partir do mapa
    public static Graph construirGrafo(char[][] mapa) {
        int numLinhas = mapa.length;
        int numColunas = mapa[0].length;
        int numVertices = numLinhas * numColunas;
        Graph g = new Graph(numVertices);

        // Deltas para as 8 direções (linhas e colunas)
        int[] dr = { -1, -1, 0, 1, 1, 1, 0, -1 };
        int[] dc = { 0, 1, 1, 1, 0, -1, -1, -1 };

        for (int r = 0; r < numLinhas; r++) {
            for (int c = 0; c < numColunas; c++) {
                int vertexAtualIdx = r * numColunas + c; // Mapeia (r,c) para índice 1D
                int alturaAtual = getAltura(mapa[r][c]);

                for (int i = 0; i < dr.length; i++) {
                    int nr = r + dr[i]; // nova linha (vizinho)
                    int nc = c + dc[i]; // nova coluna (vizinho)

                    // Verifica se o vizinho está dentro dos limites do mapa
                    if (nr >= 0 && nr < numLinhas && nc >= 0 && nc < numColunas) {
                        int alturaVizinho = getAltura(mapa[nr][nc]);

                        // Aplica a regra de movimento: pode ir para vizinho se
                        // alturaVizinho <= alturaAtual + 1
                        if (alturaVizinho <= alturaAtual + 1) {
                            int vertexVizinhoIdx = nr * numColunas + nc;
                            g.addEdge(vertexAtualIdx, vertexVizinhoIdx); // Aresta direcionada
                        }
                    }
                }
            }
        }
        return g;
    }

    public static void main(String[] args) {
        try {
            System.out.println("\nLendo arquivo caso" + ARQUIVOID + ".txt...");
            int[] posicaoInicialCoords = new int[2]; // Armazenará {linha, coluna} de 'S'
            char[][] mapa = lerMapa(ARQUIVOID, posicaoInicialCoords);

            int numLinhas = mapa.length;
            int numColunas = mapa[0].length;

            Graph g = construirGrafo(mapa);

            // Converte coordenadas (linha,coluna) de 'S' para índice de vértice
            int startVertexIdx = posicaoInicialCoords[0] * numColunas + posicaoInicialCoords[1];

            // Usa sua classe CaminhamentoLargura
            CaminhamentoLargura cl = new CaminhamentoLargura(g, startVertexIdx);

            int minPassosParaZ = Integer.MAX_VALUE;
            boolean encontradoPeloMenosUmZ = false;

            // Encontra o(s) vértice(s) 'z' e a menor distância até eles
            for (int r = 0; r < numLinhas; r++) {
                for (int c = 0; c < numColunas; c++) {
                    if (mapa[r][c] == 'z') {
                        int zVertexIdx = r * numColunas + c;
                        if (cl.hasPathTo(zVertexIdx)) {
                            minPassosParaZ = Math.min(minPassosParaZ, cl.distTo(zVertexIdx));
                            encontradoPeloMenosUmZ = true;
                        }
                    }
                }
            }

            if (encontradoPeloMenosUmZ) {
                System.out.println("Menor número de passos para chegar em 'z': " + minPassosParaZ);
            } else {
                System.out.println("Não foi possível encontrar um caminho até 'z'.");
            }

        } catch (IOException e) {
            System.err.println("Erro ao processar: " + e.getMessage());
            // e.printStackTrace(); // Útil para debugging
        } catch (Exception e) {
            System.err.println("Ocorreu um erro inesperado: " + e.getMessage());
            // e.printStackTrace(); // Útil para debugging
        }
    }
}