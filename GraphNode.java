/**
 *	@author Andrew Coleman
 *	GraphNode is an object representing a state or item in a tree. Allows for one unique searchable key to identify the vertex.
 */

public class GraphNode extends KeyedItem
{
	//the current state of this vertex
	private boolean mark;

	/**
	 * Constructor, makes a new GraphNode.
	 * @param item A Comparable for the unique searchable key of this vertex.
	 */
	GraphNode ( Comparable item )	{
		super ( item );
		mark = false;
	}
	
	/**
	 * Returns true if node is marked, false if it is not.
	 * @return A boolean of the current GraphNode's state.
	 */
	public boolean isMarked()	{
		return mark;
	}
	
	/**
	 * Sets value of mark to true or false.
	 * @param boolean State of the current vertex.
	 */
	public void setMarked ( boolean setbool )	{
		mark = setbool;
	}
	
	/**
	 * Allows for the comparison of two GraphNode objects.
	 * @param node A GraphNode to be compared against the current object.
	 * @return Returns a boolean indicating if the two GraphNodes are equal.
	 */
	public boolean equals ( GraphNode node )	{
		return ( node.getKey().compareTo ( this.getKey() ) == 0);
	}
}