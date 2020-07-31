import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class GraphImplementation implements Graph
{
    private ArrayList<ArrayList<Integer>> graph; //Allows our graph to expand if a new node is added

    public GraphImplementation( int size )
    {
        graph = new ArrayList<>();
        for( int src = 0; src < size; src++ )
        {
            ArrayList<Integer> vertex = new ArrayList<>(); //ArrayList represents vertex and its edges
            //Setting the edge to other nodes to 0 (false)
            for( int dest = 0; dest < size; dest++ )
            { vertex.add( 0 ); }
            graph.add( vertex ); //After creating this vertex, add it to our graph
        }
    }

    @Override
    public void addEdge( int v1, int v2 ) throws Exception
    {
        try
        {
            //A vertex can not visit itself, so this will throw an error
            if( v1 == v2 )
            { throw new IndexOutOfBoundsException(); }
            graph.get( v1 ).set( v2, 1 );
        }
        catch( IndexOutOfBoundsException e )
        {
//            //Normal case
//            System.out.println("[One or both vertices are invalid]");
            //For Assignment 10
            throw new Exception();
        }
    }

    @Override
    public List<Integer> neighbors( int vertex )
    {
        List<Integer> neighbors = new ArrayList<>();
        for( int dest = 0; dest < graph.size(); dest++ )
        {
            if( graph.get( vertex ).get( dest ) == 1 )
            { neighbors.add( dest ); }
        }
        return neighbors;
    }

    //Similar to neighbors, this collects all of the vertices POINTING TO the argument vertex
    public List<Integer> pointedTo( int vertex )
    {
        List<Integer> pointedTo = new ArrayList<>();
        for( int src = 0; src < graph.size(); src++ )
        {
            if( graph.get( src ).get( vertex ) == 1 )
            { pointedTo.add( src ); }
        }
        return pointedTo;
    }

    @Override
    public List<Integer> topologicalSort()
    {
        List< Integer > sort = new ArrayList<>();
        int[][] visited = new int[graph.size()][2];
        int sortCount = 0, lastVisit = -1, prevLastVisit = -1; //sortCount controls while loop
        // Mark all vertices as not visited and include num of pre req vertices
        for( int i = 0; i < graph.size(); i++ )
        {
            visited[ i ][ 0 ] = pointedTo( i ).size();
            visited[ i ][ 1 ] = 0;
        }
        //Loops search if not sorted after first run
        while(sortCount != graph.size())
        {
            for( int i = 0; i < graph.size(); i++ )
            {
                //Sort finished
                if(sortCount == graph.size())
                { continue; }
                //Vertex that hasn't been visited and all prev vertices been fulfilled
                if ( visited[ i ][ 0 ] == 0 && visited[ i ][ 1 ] == 0)
                {
                    //Initial case
                    if(sortCount == 0)
                    {
                        lastVisit = i;  //lastVisit ensures subsequent vertex is next in sort
                        topologicalSortUtil( i, visited, sort );
                        sortCount++;
                    }
                    else
                    {
                        //Checks if current vertex is connected to last check vertex
                        if( neighbors( lastVisit ).contains( i ) )
                        {
                            lastVisit = i;
                            topologicalSortUtil( i, visited, sort );
                            sortCount++;
                        }
                        //If prevLastVisit at end of last full run is same as current lastVisit
                        //Means the graph is disconnected so only after one section is fully sorted
                        //The next will be sorted
                        else if( lastVisit == prevLastVisit )
                        {
                            lastVisit = i;
                            topologicalSortUtil( i, visited, sort );
                            sortCount++;
                        }
                    }
                }
            }
            //If the neither value changes, it can be assumed the graph is cyclic
            if(prevLastVisit == lastVisit)
            {
                System.out.println("This is a cyclic graph.");
                return null;
            }
            else
            { prevLastVisit = lastVisit; } //Sets the last run's final visit
        }
        System.out.println( sort );
        return sort;
    }

    private void topologicalSortUtil( int v, int[][] visited, List<Integer> sort)
    {
        visited[ v ][ 1 ] = 1; // Mark the current node as visited
        //Fulfills all vertices with this vertex as requirement
        for( int track = 0; track < visited.length; track++ )
        {
            if( pointedTo( track ).contains( v ) )
            { visited[ track ][ 0 ]--; }
        }
        sort.add( v ); //Adds vertex to sort list
    }

    public String toString()
    {
        String output = "s  d"; //s: "source" vertices (down) and d: "destination" vertices (right)
        for( int dest = 0; dest < graph.size(); dest++ )
        {
            output += dest;
            if( dest + 1 != graph.size() )
            { output += "\t"; }
        }
        output += "\n";
        for( int src = 0; src < graph.size(); src++ )
        {
            output += src + " [ ";
            for( int dest = 0; dest < graph.size(); dest++ )
            {
                output += graph.get( src ).get( dest );
                if( dest + 1 != graph.size() )
                { output += "\t"; }
            }
            output += " ]\n";
        }
        return output;
    }
}