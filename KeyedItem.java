//given to me from mark boshart
public abstract class KeyedItem 
{
  private Comparable searchKey;
  
  public KeyedItem(Comparable key) 
  {
    searchKey = key;
  }  // end constructor

  public Comparable getKey() 
  {
    return searchKey;
  }  // end getKey

  public String toString()
  {
     return searchKey.toString();
  }

}