import java.util.ArrayList;

/**
 * A representation of a mathematical relationship model using Java. It supports<br>
 * the ability to add and remove items and relationships between the items from the graph<br>
 * as well as finding various items in the graph using several different algorithms.
 * @author Andrew Coleman
 */
public class Graph	{
	//size integer variable to keep track of number of nodes in the graph
	private int size;
	//Double two-dimensional array for the adjacency matrix
	private double[][] adjacent;
	//Array for holding the movie edges
	private String[][] movielist;
	//Array for holding the dates of movies
	private int[][] datelist;
	//boolean to determine whether a graph is directed
	private boolean directed;
	//Vertex storage list
	private ArrayList vertexList;
	//int to determin generic unweighted value
	private final int UNWEIGHTED_VALUE = 1;

	/**
	 *  Default Constructor, creates an undirected graph.
	 */
	public Graph ()	{
		size = 0;
		adjacent = new double[size][size];
		movielist = new String[size][size];
		datelist = new int[size][size];
		vertexList = new ArrayList();
		directed = false;
	}
  
	/**
	 *  Constructor, takes a boolean to determine directed or undirected.
	 */
	public Graph ( boolean param )	{
		size = 0;
		adjacent = new double[size][size];
		movielist = new String[size][size];
		datelist = new int[size][size];
		vertexList = new ArrayList();
		directed = param;
	}

	/**
	 * Removes all vertecies in the graph and sets the size to zero.
	 */
	public void makeEmpty()	{
		size = 0;
		adjacent = new double[size][size];
		movielist = new String[size][size];
		datelist = new int[size][size];
		vertexList.clear();
	}

	/**
	 * Convienence method for determining if the graph is empty.
	 * @return boolean if the graph is empty.
	 */
	public boolean isEmpty()	{
		return ( size == 0 );
	}
	
	/**
	 * Determines if this graph is a connected graph or not.
	 * @return boolean determining if this is connected.
	 */
	public boolean isConnected()	{
		for ( int x = 0; x < size; x++ )	{
			ArrayList t = bft ( getVertex ( x ).getKey() );
			if ( t.size() != size )	return false;
		}
		return true;
	}
	
	/**
	 * Convienence method for determining the number of vertecies in the graph.
	 * @return int of the size of the graph.pat
	 */
	public int numVertices()	{
		return size;
	}

	/**
	 * Determines the number of edges between all points in the graph.
	 * @return int of the number of edges.
	 */
	public int numEdges()	{
		int c = 0;
		for ( int x = 0; x < size; x++ )	{
			for ( int y = 0; y < size; y++ )	{
				if ( adjacent[x][y] != Double.POSITIVE_INFINITY )	{
					c++;	//But wait, it's Java!
				}
			}
		}
		return c;
	}

	/**
	 *  Method to resize the adjacency matrix.
	 *  @param Size integer value of the size of the graph.
	 */
	private void resizeAdjacent ( int size )	{
		double[][] temp = new double[size][size];
		String[][] tmovie = new String[size][size];
		int[][] tdate = new int[size][size];
		for( int x = 0; x < size - 1; x++)
			for ( int y = 0; y < size - 1; y++ )	{
				temp[x][y] = adjacent[x][y];
				tmovie[x][y] = movielist[x][y];
				tdate[x][y] = datelist[x][y];
			}
		for ( int x = 0; x < size; x++ )	{
			temp[x][size - 1] = Double.POSITIVE_INFINITY;
            temp[size - 1][x] = Double.POSITIVE_INFINITY;
            tmovie[x][size - 1] = "";
            tmovie[size - 1][x] = "";
            tdate[x][size - 1] = 0;
            tdate[size - 1][x] = 0;
		}
		adjacent = temp;
		movielist = tmovie;
		datelist = tdate;
	}

	/**
	 * Adds a new GraphNode into the graph.
	 * @param GraphNode the node to be added.
	 * @throws GraphException if the node is already in the graph.
	 */
	public void addVertex ( GraphNode myItem ) throws GraphException	{
		if ( findIndex ( myItem.getKey() ) >= 0 )
			throw new GraphException ( "Vertex already exists!" );
		size++;
		vertexList.add ( myItem );
		resizeAdjacent ( vertexList.size() );
	}
	
	/**
	 * Adds an edge with a weight.
	 * @param searchKey1 first vertex to use in the edge.
	 * @param searchKey2 second vertex to use in the edge.
	 * @param weight double value of the edge between searchKey1 and searchKey2.
	 * @throws GraphException if an edge already exists.
	 */
	public void addEdge ( Comparable searchKey1, Comparable searchKey2, double weight ) throws GraphException	{
		int x = findIndex ( searchKey1 );
		int y = findIndex ( searchKey2 );
		if ( x < 0 || y < 0 )
			throw new GraphException ( "No matching vertecies were found!" );
		if ( adjacent[x][y] > 0 )
			throw new GraphException ( "Edge already exists!" );
		adjacent[x][y] = weight;
	}
	
	/**
	 * Adds an edge between two actors and also records the name and date of the film connecting the actors.
	 * @param searchKey1 first actor to find.
	 * @param searchKey2 second actor to find.
	 * @param movie movie that both actors starred in.
	 * @param date date of the movie's release.
	 * @throws GraphException if a duplicate was found or if no vertecies were found.
	 */
	public void addEdge ( Comparable searchKey1, Comparable searchKey2, String movie, String date ) throws GraphException	{
		int x = findIndex ( searchKey1 );
		int y = findIndex ( searchKey2 );
		if ( x < 0 || y < 0 )
			throw new GraphException ( "No matching vertecies were found!" );
		int datenum = Integer.parseInt ( date );
		if ( datenum == 0 || datelist[x][y] < datenum || (datelist[x][y] == datenum && movielist[x][y].compareTo ( movie ) > 0) )	{
			adjacent[x][y] = UNWEIGHTED_VALUE;
			adjacent[y][x] = UNWEIGHTED_VALUE;
			movielist[x][y] = movie;
			movielist[y][x] = movie;
			datelist[x][y] = datenum;
			datelist[y][x] = datenum;
		}
		else	{
			throw new GraphException ( "Duplicate edge exists!" );
		}
	}

	/**
	 * Adds an unweighted edge between two keys in the graph.
	 * @param searchKey1 first vertex to find.
	 * @param searchKey2 second vertex to find.
	 * @throws GraphException if a duplicate was found or no matching vertecies found.
	 */
	public void addEdge ( Comparable searchKey1, Comparable searchKey2 ) throws GraphException	{  
		int x = findIndex ( searchKey1 );
		int y = findIndex ( searchKey2 );
		if ( x < 0 || y < 0 )
			throw new GraphException ( "Vertecies not found!" );
		if ( adjacent[x][y] > 0 )
			throw new GraphException ( "Edge already exists!" );
		adjacent[x][y] = UNWEIGHTED_VALUE;
		if(!directed)	{
			adjacent[y][x] = UNWEIGHTED_VALUE;
		}
	}
	
	/**
	 * Returns the value of the edge between two keys in the graph. 
	 * @param searchKey1 first vertex to find.
	 * @param searchKey2 second vertex to find.
	 * @throws GraphException if a duplicate was found.
	 */
	public double getWeight ( Comparable searchKey1, Comparable searchKey2 ) throws GraphException	{
		int x = findIndex ( searchKey1 );
		int y = findIndex ( searchKey2 );
		if ( x < 0 || y < 0 )
			throw new GraphException ( "Edge does not exist!" );
		return adjacent[x][y];
	}
	
	/**
	 * Returns the movie connection between two actors.
	 * @param searchKey1 first actor to find.
	 * @param searchKey2 second actor to find.
	 * @return String representing the oldest and first movie alphabetically found to be linking the two actors.
	 * @throws GraphException if no edge was found.
	 */
	public String getMovie ( Comparable searchKey1, Comparable searchKey2 ) throws GraphException	{
		int x = findIndex ( searchKey1 );
		int y = findIndex ( searchKey2 );
		if ( x < 0 || y < 0 )
			throw new GraphException ( "Movie connection does not exist!" );
		return movielist[x][y] + "(" + datelist[x][y] + ")";
	}
	
	/**
	 * Returns a vertex by index.
	 * @param index Integer number representing the vertex you want.
	 * @return GraphNode of the vertex at the specified index.
	 * @throws GraphException if index is out of range ( 1 - size ).
	 */
	public GraphNode getVertex ( int index ) throws GraphException	{
		if ( index > size || index < 0 )
			throw new GraphException ( "Index out of bounds!" );
		else
			return (GraphNode)vertexList.get ( index );
	}
	
	/**
	 * Returns the vertex represented by a searchable key.
	 * @param searchKey vertex to find.
	 * @return GraphNode representing the vertex at key searchKey,
	 * @throws GraphException if vertex is not found,
	 */
	public GraphNode getVertex ( Comparable searchKey ) throws GraphException	{
		return getVertex ( findIndex ( searchKey ) );
	}
	
	/**
	 * Returns the searchable key found at a particular index.
	 * @param index integer index to get the key from.
	 * @return Comparable of the searchable key.
	 * @throws GraphException if the index is out of range.
	 */
	public Comparable getSearchKey ( int index ) throws GraphException	{
		if ( index > size || index < 0 )
			throw new GraphException ( "Index out of range!" );
		return ((GraphNode)(vertexList.get ( index ))).getKey();
	}
	
	/**
	 * Private method for finding the index of a given searchable key.
	 * @param key of the vertex to find in the graph. Assumes to be in the range 1 - size.
	 * @return int representing the index of the vertex or -1 if no vertex is found.
	 */
	private int findIndex ( Comparable key )	{
		for ( int i = 0; i < vertexList.size(); i++ )
			if ( ((GraphNode)vertexList.get ( i )).getKey().compareTo ( key ) == 0 )
				return i;
		return -1;
	}
	
	/**
	 *  Clears all vertices, sets all to be unmarked.
	 */
	private void clearMarks()	{
		for ( int x = 0; x < size; x++ )	{
			((GraphNode)vertexList.get ( x )).setMarked ( false );
		}
	}

	/**
	 * Removes an edge between two searchable keys.
	 * @param searchKey1 first vertex to find.
	 * @param searchKey2 second vertex to find.
	 * @throws GraphException if vertecies do not exist.
	 */
	public void removeEdge ( Comparable searchKey1, Comparable searchKey2 ) throws GraphException	{
		int a = findIndex ( searchKey1 );
		int b = findIndex ( searchKey2 );
		if ( a == -1 || b == -1 )	throw new GraphException ( "Entry not found in list!" );
		adjacent[a][b] = Double.POSITIVE_INFINITY;
		movielist[a][b] = "";
		if ( !directed )	{
			adjacent[b][a] = Double.POSITIVE_INFINITY;
			movielist[b][a] = "";
		}
	}
	
	/**
	 * Removes a vertex by a searchable key.
	 * @param key the vertex to find.
	 * @return GraphNode of the vertex removed from the graph.
	 * @throws GraphException if the vertex is not in the graph.
	 */
	public GraphNode removeVertex ( Comparable key ) throws GraphException	{
		int index = findIndex ( key );
		if ( index == -1 )	throw new GraphException ( "Vertex not in graph!" ); 
		double[][] temp = new double[adjacent.length - 1][adjacent.length - 1]; 
		String[][] mtemp = new String[adjacent.length - 1][adjacent.length - 1]; 
		for ( int row = 0; row < adjacent.length; row++ )	{
			for ( int col = 0; col < adjacent[0].length; col++ ) {
				if ( row != index && col != index )	{
					int newrow = row;
					int newcol = col;
					/* make sure the row/col are not in the row/col of the vertex to be removed */
					if ( row > index )	newrow = row - 1;
					if ( col > index )	newcol = col - 1;
					temp[newrow][newcol] = adjacent[row][col];
					mtemp[newrow][newcol] = movielist[row][col];
				}
			}
		}
		adjacent = temp;
		movielist = mtemp;
		size--;
		return (GraphNode)vertexList.remove ( index );
	}
	
	/**
	 * Returns the breadth-first traversal of a searchable key.
	 * @param searchKey vertex to start from.
	 * @return ArrayList of the vertecies in the path. Empty if no path is found.
	 */
	public ArrayList bft ( Comparable searchKey )	{
		GraphNode temp;
		ArrayList searchList = new ArrayList();
		ArrayList bfsQueue = new ArrayList ( size );
		bfsQueue.add ( bfsQueue.size(), vertexList.get ( findIndex ( searchKey ) ) );
		getVertex ( searchKey ).setMarked ( true );
		searchList.add ( getVertex ( searchKey ) );

		while ( !bfsQueue.isEmpty() )	{
			temp = (GraphNode)bfsQueue.remove ( 0 );
			for ( int g = 0; g < size; g++ )	{
				if ( adjacent[findIndex ( temp.getKey() )][g] != Double.POSITIVE_INFINITY && !getVertex ( g ).isMarked() )	{
					((GraphNode)vertexList.get ( g )).setMarked ( true );
					bfsQueue.add ( bfsQueue.size(), vertexList.get ( g ) );
					searchList.add ( getVertex ( g ) );
				}
			}
		}
		clearMarks();
		return searchList;
	}

	/**
	 * Determines the diameter of the graph, returns infinity if unconnected.
	 * @return int representing the diameter of the graph.
	 */
	public double diameter()	{
		// early check... can make it take a really long time
		if ( !isConnected() )	return Double.POSITIVE_INFINITY;
		System.out.println ( "Graph is connected." );
		double mins[] = new double[size];
		// find all shortest paths and determine the maximum of each
		for ( int x = 0; x < size; x++ )	{
			mins[x] = 0.0;
			Comparable key = getVertex ( x ).getKey();
			for ( int y = 0; y < size; y++ )	{
				if ( x == y )	continue;
				ArrayList path = shortestPath ( key, getVertex ( y ).getKey() );
				if ( path.size() > mins[x] )	mins[x] = path.size();
			}
		}
		// find the minimum of the paths
		double max = mins[0];
		for ( int x = 0; x < size; x++ )	{
			if ( mins[x] > max )	max = mins[x];
		}
		// return num edges between them, not number of nodes
		return max - 1;
	}
	
	/**
	 * Returns a breadth-first search between two searchable keys.
	 * @param searchKey1 first key to find.
	 * @param searchKey2 second key to find.
	 * @return ArrayList containing the path between the two keys, empty if no path is possible.
	 */
	public ArrayList bfs ( Comparable searchKey1, Comparable searchKey2 )	{
		ArrayList p = bft ( searchKey1 );
		Comparable curkey = searchKey1;
		int x = 0;
		for ( x = 0; x < p.size() && curkey.compareTo ( searchKey2 ) != 0; x++ )
			curkey = ((GraphNode) p.get ( x )).getKey();
		
		if ( x > p.size() / 2 )	{
			for ( int y = p.size() - 1; y > x; y-- )
				p.remove( y );
		}
		else	{
			ArrayList q = new ArrayList ( x );
			for ( int y = 0; y <= x; y++ )
				q.add ( p.get ( x ) );
			p = q;
		}
		return p;
	}
	
	/**
	 * Performs a depth-first search for a searchable key. 
	 * @param searchKey vertex to find.
	 * @return ArrayList containing the path to the vertex.
	 * @throws GraphException if no path is found.
	 */
	public ArrayList dfs ( Comparable searchKey ) throws GraphException	{
		ArrayList dfsList = new ArrayList();
		dfsList = dfsRec ( getVertex ( searchKey ), dfsList );
		clearMarks();
		if ( dfsList.isEmpty() )	{
			throw new GraphException ( "No path is found!" );
		}
		return dfsList;
	}

	/**
	 *  Private recursive method called to generate a breadth-first search.
	 *  @param vertex GraphNode to search from.
	 *  @param searchRecList list to use while recursing. 
	 *  @return ArrayList of the generated path. 
	 */
	private ArrayList dfsRec ( GraphNode vertex, ArrayList searchRecList )	{
		searchRecList.add ( vertex );
		vertex.setMarked ( true );
		int i = vertexList.indexOf ( vertex );
		for ( int j = 0; j<size; j++ )	{
			if ( adjacent[i][j] != Double.POSITIVE_INFINITY && !(getVertex ( j )).isMarked() )
				dfsRec( getVertex ( j ), searchRecList );
		}
		return searchRecList;
	}

   /**
    * Used to calculate the shortest distance between two vertices using Djisktra's algorithm.
    * @throws GraphException if both Comparables are equal or there is no connecting path.
    * @return ArrayList containing the path between the two items in the Graph.
    */
	public ArrayList shortestPath ( Comparable firstkey, Comparable lastkey ) throws GraphException	{
		if ( firstkey.compareTo ( lastkey ) == 0 )
			throw new GraphException ( "Cannot find shortest path to same vertex!" );
		/* the arraylist containing the raw path array */
		ArrayList path = new ArrayList ( vertexList.size() );
		/* mark the first vertex */
		getVertex ( firstkey ).setMarked ( true );
		/* indexes of the first and last search keys */
		int firstindex = findIndex ( firstkey );
		int secondindex = findIndex ( lastkey );
		/* the row of the adjacency matrix at firstindex */
		double[] weight = new double[vertexList.size()];
		/* initialize the weight/path arrays */
		for ( int i = 0; i < weight.length; i++ )	{
			path.add ( vertexList.get ( i ) );
			weight[i] = adjacent[firstindex][i];
			/* if the weight is not infinity we must change the path to reflect the changes */
			if ( weight[i] < Double.POSITIVE_INFINITY )
	            path.set ( i, getVertex ( firstkey ) );
		}
		/* loop through all the elements in the graph, must mark them all */
		for ( int i = 1; i < vertexList.size(); i++ )	{
			/* index of smallest vertex */
			int smallest = -1;
			/* smallest weight thus far that is not marked */
			double smallestweight = Double.POSITIVE_INFINITY;
			for ( int j = 0; j < weight.length; j++ )
				/* run through the weight array and find the smallest weight */
				if ( smallestweight >= weight[j] && !((GraphNode)vertexList.get ( j )).isMarked() )	{
					/* note smallest vertex */
					smallest = j;
					smallestweight = weight[j];
				}
			//if ( smallest == -1 )	continue;
			/* mark smallest vertex */
			GraphNode smallnode = (GraphNode)vertexList.get ( smallest );
			smallnode.setMarked ( true );
			/* update the weight/path arrays */
			for ( int j = 0; j < weight.length; j++ )
				/* if a new weight to that vertex is less than the current weight, change the weight in the array and change the path arraylist */
				if ( weight[j] > weight[smallest] + adjacent[smallest][j] )	{
					weight[j] = weight[smallest] + adjacent[smallest][j];
					path.set ( j, vertexList.get ( smallest ) );
				}
		}
		/* backwards path */
		ArrayList result = new ArrayList();
		GraphNode node = getVertex ( lastkey );
		int lastindex = findIndex ( lastkey );
		/* while the node is not the first node */
		while ( lastindex != firstindex )	{
			result.add ( node );
			node = (GraphNode)path.get ( lastindex );
			int newlastindex = findIndex ( node.getKey() );
			/* if the indexes are the same, then there is no way to get to that vertex */
			if ( newlastindex == lastindex )
				throw new GraphException ( "No connecting path!" );
			else
				lastindex = newlastindex;
		}
		/* gotta add the first one */
		result.add ( getVertex ( firstkey ) );
		
		/* unmark everything */
		clearMarks();
		
		/* reverse the arraylist */
		path.clear();
		for ( int i = result.size() - 1; i >= 0; i-- )
			path.add ( result.get ( i ) );
		return path;
	}
}
