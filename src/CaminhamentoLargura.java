import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class CaminhamentoLargura {
    private boolean[] marked;
    private int[] edgeTo;
    private int[] distTo;
    private int s;

    CaminhamentoLargura(Graph g, int s){
        this.s = s;
        marked = new boolean[g.V()];
        edgeTo = new int[g.V()];
        distTo = new int[g.V()];

        //eu preciso de uma fila para inicializar
        Queue<Integer> q = new LinkedList<>();

        //vamos pensar no inicio do caminhamento
        q.add(s);
        marked[s] = true;

        while(!q.isEmpty()){ //q.peek() != null
            int v = q.remove();
            for(int w : g.adj(v)){
                if(!marked[w]){
                    q.add(w);
                    marked[w] = true;
                    edgeTo[w] = v;
                    distTo[w] = distTo[v] + 1;
                }
            }
        }
    }

    public boolean hasPathTo(int v){
        return marked[v];
    }

    public ArrayList<Integer> pathTo(int v){
        if (!hasPathTo(v)) return null;
        ArrayList<Integer> path = new ArrayList<>();

        while(v != s){
            path.add(0,v);
            v = edgeTo[v];
        }
        path.add(0,s);
        return path;

    }

    public int distTo(int v){
        return distTo[v];
    }

//     public static void main (String[] args) {
//         String filename = "tinyG.txt";
//         In in = new In(filename);
//         Graph g = new Graph(in);

//         CaminhamentoLargura cl = new CaminhamentoLargura(g, 0);
// //        if (cl.hasPathTo(3)){     
// //            for (int v : cl.pathTo(3)) {
// //                StdOut.print(v + " ");
// //            }
// //        }
//         System.out.println("Caminhos existentes:");
//         System.out.println("O: Vértice inicial");
//         //percorre todos os veritices
//         for (int v = 1 ; v < g.V() ; v++){
//             if (cl.hasPathTo(v)){
//                 System.out.print(v + ": (" + cl.distTo[v] +  ") ");
//                 for (int w : cl.pathTo(v)) {
//                     System.out.print(w + " ");
//                 }
//                 System.out.println();
//             }
//         }
//     }
}
