package TrabalhoPraticoII;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.text.html.HTMLDocument.Iterator;

import org.graphstream.algorithm.Dijkstra;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Path;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.algorithm.ConnectedComponents;

public class Index {
	
	public static void main(String[] args){

		
		//Lê o arquivo com os dados da Aresta e armazena no Array (aresta)
		
		ArrayList<String> aresta = new ArrayList<String>();
		File file = new File("\\Users\\stell\\Desktop\\Trab\\ArestasFacebook1.txt");
		
	       try {
	            FileReader reader = new FileReader(file);
	            BufferedReader input = new BufferedReader(reader);
	            String linha;
	            while ((linha = input.readLine()) != null) {
	                //adiciona os valores da em um arrayList
	                aresta.add(linha);
	            } input.close();
	        } catch (IOException ioe) {
	            System.out.println(ioe);
	        }
		
	    //Lê o arquivo com os dados do Vertice e armazena no Array (vertice)
       ArrayList<String> vertice = new ArrayList<String>();
       File file2 = new File("\\Users\\stell\\Desktop\\Trab\\VerticesFacebook1.txt");
        try {
            FileReader reader2 = new FileReader(file2);
            BufferedReader input = new BufferedReader(reader2);
            String linha2;
	            while ((linha2 = input.readLine()) != null) {
	                //adiciona os valores em um arrayList
	                vertice.add(linha2);
	
	            } input.close();
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
        
        //Verifica o tamanho do Arraylist do vértce e cria um novo grafo
        int tamanhoVertice = vertice.size();
        Graph g = new SingleGraph("Grafo1");
	        for (int i = 0; i < tamanhoVertice; i++) {
	            //Percorre o array do vértice e adiciona ao grafo
	            g.addNode(vertice.get(i));
	        }
	        
	        int tamanhoAresta = aresta.size();
	        
	        for (int j = 0; j < tamanhoAresta; j++) {
	            //Percorre o array da aresta e separa o arquivo em duas colunas
		            String[] textoSeparado = aresta.get(j).split("-");
		            String vert1 = textoSeparado[0];
		            String vert2 = textoSeparado[1];
		            g.addEdge(vert1 + "" + vert2 + "", vert1, vert2);
	        }
	        
	        //Atribui nome para os vertices
	        for (Node n : g) {
	            n.addAttribute("label", n.getId());
	        }
       
	        ConnectedComponents cc = new ConnectedComponents();
			cc.init(g);
			

			/*Cria um novo grafo com uma lista de nós 
			 * pertencentes ao maior componente conectado do grafo*/
	        Graph g2 = new SingleGraph("Grafo2");
	        for(Node node : g){
	        	if(cc.getGiantComponent().contains(node)){
	        		g2.addNode(node.getId());
	        	}
	        }
	        
	        for (Edge edge: g.getEachEdge()){
	        	if(cc.getGiantComponent().contains(edge.getNode0())){
	        		// Nó Origem, Nó Destino - ID da aresta em questão
	        		g2.addEdge(edge.getId(), edge.getNode0().getId(), edge.getNode1().getId());
	        	}
	        }
	        
			int tamanhoVert = g2.getNodeCount();
			System.out.printf("Total vertices: %d ", tamanhoVert);
			
	        //Retorna o numero total de vértices
	        int tamanhoArest = g2.getEdgeCount();
	        System.out.printf("\nTotal arestas: %d ", tamanhoArest);
	        

	        g2.display();
	        
	       
	        //O comprimento do menor caminho será o número de vértices no caminho
	        Dijkstra dijkstra = new Dijkstra(Dijkstra.Element.NODE, null, null);
	        dijkstra.init(g2);
	        
	        for (int i = 0; i < tamanhoVertice; i++) {
	            //Percorre o array do vértice e adiciona ao grafo
	        	dijkstra.setSource(g2.getNode(vertice.get(i)));
	        }
	        //Imprime o comprimento de cada menor caminho
	        dijkstra.compute();

	        int distanciaParesVert = 0;
	        //Calcula os caminhos mais curtos do nó de origem para todos os nós no gráfico.
	        for (Node node : g2) {
	        	/*getPathLength(node) - Retorna o comprimento do caminho mais curto do 
	        	nó de origem para um determinado nó de destino.*/
	        	distanciaParesVert += dijkstra.getPathLength(node);
	        }

	        double distanciaRede = (1 / ( (0.5 * tamanhoVert) * (tamanhoVert + 1) )) * distanciaParesVert; 
	        //distanciaRede = ( 1/ ((0.5) * (tamanhoVert + 1))) * distanciaParesVert;;
	        System.out.printf("\nMedia: " + distanciaRede);
	    
	        
	}
}