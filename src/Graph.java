import java.util.ArrayList;
import java.util.List;

public class Graph {
  private final int V; // Número de vértices
  private List<Integer>[] adj; // Listas de adjacência

  public Graph(int V) {
    if (V < 0)
      throw new IllegalArgumentException("O número de vértices deve ser não-negativo");
    this.V = V;
    adj = (List<Integer>[]) new List[V]; // Cria array de listas
    for (int v = 0; v < V; v++) {
      adj[v] = new ArrayList<Integer>(); // Inicializa cada lista de adjacência
    }
  }

  // Retorna o número de vértices
  public int V() {
    return V;
  }

  // Adiciona uma aresta direcionada v -> w
  public void addEdge(int v, int w) {
    // Validações podem ser adicionadas aqui se necessário
    adj[v].add(w);
  }

  // Retorna os vizinhos do vértice v
  public Iterable<Integer> adj(int v) {
    return adj[v];
  }

  // Se você tiver um construtor como `Graph(In in)` que seu
  // CaminhamentoLargura.main usa,
  // mantenha-o para compatibilidade, mas para este problema, `Graph(int V)` é
  // mais direto.
}