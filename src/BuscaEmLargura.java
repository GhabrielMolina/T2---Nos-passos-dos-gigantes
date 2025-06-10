import java.util.LinkedList;
import java.util.Queue;

/**
 * Realiza uma busca em largura (BFS) para encontrar os caminhos mais curtos
 * em um grafo não ponderado a partir de um vértice de origem.
 */
public class BuscaEmLargura {
    private final boolean[] visitado;
    private final int[] antecessor;
    private final int[] distancia;
    private final int origem;

    public BuscaEmLargura(Grafo g, int s) {
        this.origem = s;
        visitado = new boolean[g.getNumVertices()];
        antecessor = new int[g.getNumVertices()];
        distancia = new int[g.getNumVertices()];

        Queue<Integer> fila = new LinkedList<>();

        fila.add(s);
        visitado[s] = true;
        distancia[s] = 0;

        while (!fila.isEmpty()) {
            int v = fila.remove();
            for (int w : g.adjacentes(v)) {
                if (!visitado[w]) {
                    fila.add(w);
                    visitado[w] = true;
                    antecessor[w] = v;
                    distancia[w] = distancia[v] + 1;
                }
            }
        }
    }

    /** Verifica se existe um caminho da origem até o vértice v. */
    public boolean temCaminhoPara(int v) {
        return visitado[v];
    }

    /** Retorna a distância (número de passos) da origem até o vértice v. */
    public int getDistanciaPara(int v) {
        return distancia[v];
    }

    public int getOrigem() {
        return origem;
    }
}