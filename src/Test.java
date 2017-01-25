import GraphCore.Arc;
import GraphCore.GraphUtility;
import GraphCore.Vertex;
import multithreading.SolverCaller;
import org.jgrapht.graph.SimpleWeightedGraph;
import parser.Parser;
import solvers.*;
import sun.nio.ch.SelectorImpl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

/**
 * Created by emanueleghelfi on 20/01/2017.
 */
public class Test{

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("write input and output file path as parameter");
            System.exit(0);
        }

        String inputPath = args[0];
        File correct = new File(inputPath);
        String name = correct.getName();
        System.out.println("Input File "+ name);


        String withoutExt = name.substring(0,name.indexOf("."));
        String outputPath = withoutExt.concat(".sol");
        String currentDirectory = GetExecutionPath().concat(File.separator);

        String output = currentDirectory.concat(outputPath);
        String modelPath = currentDirectory.concat("pedibus_mod.mod");

        Parser p = new Parser(inputPath,modelPath);
        List<Vertex> vertexList = p.getVertexListWithoutSchool();
        SimpleWeightedGraph<Vertex, Arc> graph = GraphUtility.createCompleteGraph(vertexList);
        //get school from parser and set in utility
        GraphUtility.setSchool(p.getSchool());
        //MaxDistanceSolver s = new MaxDistanceSolver();
        //SimpleWeightedGraph<Vertex, Arc> graph1  = s.solve((SimpleWeightedGraph<Vertex,Arc>)graph.clone(),p.getAlpha(),0);
        try {
            SolutionFormatter.writeSolution(solveMultiThread((SimpleWeightedGraph<Vertex,Arc>)graph.clone(),p.getAlpha()), output);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("File written here: "+output);

        System.exit(1);
    }


    private static String GetExecutionPath(){
        String absolutePath = Test.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        //absolutePath = absolutePath.substring(1);
        System.out.println(absolutePath);
        absolutePath = absolutePath.substring(0, absolutePath.lastIndexOf("/"));
        absolutePath = absolutePath.replaceAll("%20"," "); // Surely need to do this here
        return absolutePath;
    }

    private static SimpleWeightedGraph<Vertex,Arc> solveMultiThread(SimpleWeightedGraph<Vertex,Arc> simpleWeightedGraph, double alpha) throws ExecutionException, InterruptedException {

        //all solutions from algorithms
        ArrayList<SimpleWeightedGraph<Vertex,Arc>> solutions = new ArrayList<>();
        //executor service for multithreading
        ExecutorService executorService = Executors.newCachedThreadPool();
        Set<Future<SimpleWeightedGraph<Vertex,Arc>>> futureSet = new HashSet<Future<SimpleWeightedGraph<Vertex,Arc>>>();
        //create all instances of solvers
        SolverInterface maxNodeSolver = new MaxNodeSolver();
        SolverInterface maxDistanceSolver = new MaxDistanceSolver();
        SolverInterface localSolver = new LocalSolver();
        SolverInterface minDistanceSolver = new MinDistanceSolver();
        SolverInterface localSolverFromScratch = new LocalSolverFromScratch();

        List<SolverInterface> solverInterfaces= new ArrayList<>();
        solverInterfaces.add(maxDistanceSolver);
        solverInterfaces.add(maxNodeSolver);
        solverInterfaces.add(localSolver);
        solverInterfaces.add(minDistanceSolver);
        solverInterfaces.add(localSolverFromScratch);


        for(SolverInterface solverInterface:solverInterfaces){
            Callable<SimpleWeightedGraph<Vertex,Arc>> solverCaller = new
                    SolverCaller(solverInterface,(SimpleWeightedGraph<Vertex,Arc>)simpleWeightedGraph.clone(),alpha);
            Future<SimpleWeightedGraph<Vertex,Arc>> futures = executorService.submit(solverCaller);
            futureSet.add(futures);
        }

        for(Future<SimpleWeightedGraph<Vertex,Arc>> future:futureSet){
            solutions.add(future.get());
        }

        // compare all solutions
        int minLeaf = GraphUtility.computeLeaf(solutions.get(0));
        int dangerousness = GraphUtility.computeDangerousness(solutions.get(0));
        // the real solution
        SimpleWeightedGraph<Vertex,Arc> solution = solutions.get(0);

        for(SimpleWeightedGraph<Vertex,Arc> currentGraph: solutions){
            int leaves = GraphUtility.computeLeaf(currentGraph);
            int dang = GraphUtility.computeDangerousness(currentGraph);
            if(leaves<minLeaf){
                solution = currentGraph;
                minLeaf =  leaves;
                dangerousness = dang;
            }
            else{
                if(minLeaf==leaves && dang<dangerousness){
                    solution = currentGraph;
                    minLeaf =  leaves;
                    dangerousness = dang;
                }
            }
        }

        System.out.println("Leaf of solution: "+ GraphUtility.computeLeaf(solution));

        for(List<Vertex>path: GraphUtility.computePaths(solution)){
            if(!GraphUtility.checkPathFeasible(path,alpha)){
                System.out.println("ERRROR!");

            }
        }

        return solution;

    }



    private static boolean isWindows(String os) {
        return (os.contains("win"));
    }



}
