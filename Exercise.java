import java.util.ArrayList;
import java.util.Scanner;

public class Exercise {
    String name, targetMuscles;
    int setCount, id;
    ArrayList<Sets> set = new ArrayList<>();

    public void input(){
        Scanner scan = new Scanner(System.in);
        
        for(int i = 1; i <= this.setCount; i++){
            System.out.println("Enter weight for " + i + ". set of " + this.name);
            float w = scan.nextFloat();
            System.out.println("Enter reps for " + i + ". set of "+ this.name);
            int r = scan.nextInt();
            Sets currentSet = new Sets(w, r);
            this.addSet(currentSet);
        }
    }

    public Exercise(String name, int setCount, String targetMuscles){
        this.name = name;
        this.setCount = setCount;
        this.targetMuscles = targetMuscles;
    }

    public void addSet(Sets sets){
        this.set.add(sets);
    }

    public void printSets(){
        for (Sets i : set) {
            System.out.println("     "+i.weight + " x " + i.reps);
        }
    }

    public ArrayList<Sets> getSets(){
        return set;
    }

    public void saveSets(){

    }

    public void getFromId(){
        
    }

}
