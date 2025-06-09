import java.util.ArrayList;
import java.util.List;

/**
 * Representa um grafo direcionado usando listas de adjacência.
 */
public class Grafo {
    private final int numVertices;
    private final List<Integer>[] adjacencias;

    public Grafo(int V) {
        if (V < 0) {
            throw new IllegalArgumentException("O número de vértices deve ser não-negativo.");
        }
        this.numVertices = V;
        this.adjacencias = (List<Integer>[]) new List[V];
        for (int v = 0; v < V; v++) {
            adjacencias[v] = new ArrayList<>();
        }
    }

    /** Retorna o número de vértices do grafo. */
    public int getNumVertices() {
        return numVertices;
    }

    /** Adiciona uma aresta direcionada de v para w. */
    public void adicionarAresta(int v, int w) {
        adjacencias[v].add(w);
    }

    /** Retorna os vértices adjacentes a um dado vértice v. */
    public Iterable<Integer> adjacentes(int v) {
        return adjacencias[v];
    }
}