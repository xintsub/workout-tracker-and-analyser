import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Workout {

    ArrayList<Exercise> exercises = new ArrayList<>();
    public static void main(String[] args) throws IOException{
        Scanner scan = new Scanner(System.in);
        File f = new File("workouts");
        File[] ff = f.listFiles();
        Workout workout = new Workout();
        File chosen = new File("");

        if(ff == null){
            System.out.println("No workouts found. Creating a new one...");
            workout.newWorkout();
        }

        else{
            Boolean esc = true;
            while(esc){
        
                ff = f.listFiles();
                System.out.println("Please choose workout by typing its number. Type n to create new workout, d to delete, a for analysis, exit to exit.");
                for(int i = 0; i < ff.length; i++){
                    System.out.println(i + ": " + ff[i].getName());
                }
                String input = scan.nextLine();
                if(input.equals("n")) workout.newWorkout();
                else if (input.equals("d")) workout.deleteWorkout();
                else if (input.equals("a")) workout.analyse();
                else if (input.equals("exit")) System.exit(0);
                else{
                    int cindex = Integer.parseInt(input);
                    if(cindex >= 0 && ff[cindex].exists()){
                        chosen = ff[cindex];
                        esc = false;
                    }
                    else System.out.println("Wrong input!");
                }
            }
            
            try(Scanner file = new Scanner(chosen)){
                while(file.hasNextLine()){
                    String data = file.nextLine();
                    String[] data2 = data.split(",");
                    Exercise e = new Exercise(data2[0], Integer.parseInt(data2[1]), data2[2]);
                    workout.addExercise(e);
                }
            }
            catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            }
            workout.enterSets();
            workout.getExercises();
            workout.saveExercise();
        }   
    }

    public void analyse(){
        Scanner scan = new Scanner(System.in);
        File f = new File("history.txt");
        try {
            Scanner fs = new Scanner(f);
            System.out.println("Please enter the name of the exercise you want to analyse.");
            String exName = scan.nextLine().toLowerCase();
            float maxVolume[] = {0, 0, 0};  // Volume, Weight, Rep
            float maxWeight[] = {0, 0};     // Weight, Rep
            float maxRep[] = {0, 0};        // Rep, Weight
            while(fs.hasNextLine()){
                String curLine = fs.nextLine().toLowerCase();
                String[] splitLine = curLine.split(",");
                if(splitLine[0].equals(exName)){
                    for(int i = 1; i < splitLine.length; i++){
                        String weightAndRep[] = splitLine[i].split("x");
                        float weight = Float.parseFloat(weightAndRep[0].trim());
                        int rep = Integer.parseInt(weightAndRep[1].trim());
                        float volume = weight * rep;
                        if(weight > maxWeight[0]){
                            maxWeight[0] = weight;
                            maxWeight[1] = rep;
                        } 
                        if(rep > maxRep[0]) {
                            maxRep[0] = rep;
                            maxRep[1] = weight;
                        }
                        if(volume > maxVolume[0]){
                            maxVolume[0] = volume;
                            maxVolume[1] = weight;
                            maxVolume[2] = rep;
                        } 
                    }
                }
            }
            System.out.println("Analysis for " + exName + ":");
            System.out.println("Maximum Volume: " + maxVolume[0] + "\t( " + maxVolume[1]+" x "+(int)maxVolume[2]+" )");
            System.out.println("Maximum Weight: " + maxWeight[0] + "\t( "+maxWeight[0]+" x "+(int)maxWeight[1]+" )");
            System.out.println("Maximum Rep:    " + (int)maxRep[0] + "\t( "+maxRep[1]+" x "+(int)maxRep[0]+" )");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void listWorkouts(){
    }

    public void deleteWorkout(){
    }

    public void newWorkout() throws IOException{
        Scanner scan = new Scanner(System.in);
        System.out.println("Please enter a name for the workout.");
        String woName = scan.nextLine();
        File f = new File("workouts/"+woName+".txt");
        if(f.createNewFile()){
            FileWriter fw = new FileWriter(f, true);
            while(true){
                System.out.println("Please enter the name of exercise, type exit to stop.");
                String eName = scan.nextLine();
                if (eName.equals("exit")){
                    fw.close();
                    break;
                } 
                System.out.println("Please enter set count for " + eName);
                int sCo = scan.nextInt();
                scan.nextLine();
                while(sCo <= 0){
                    System.out.println("Set count must be greater than 0, Please enter again.");
                    sCo = scan.nextInt();
                    scan.nextLine();
                }
                fw.append(eName+","+sCo+","+"soon\n");
            }
        }
        else{
            System.out.println("An error occured!");
            return;
        }
        System.out.println("Workout succesfully created!");
    }

    public void addExercise(Exercise e){
            exercises.add(e);
    }

    public void getExercises(){
        for (Exercise i : exercises) {
            System.out.println(i.name+"    ("+i.targetMuscles+")     " + "("+i.setCount+" sets)");
            i.printSets();
        }
    }

    public void enterSets(){
        for (Exercise e : exercises) {
            e.input();
        }
    }

    public void saveExercise() throws IOException{
        FileWriter fw = new FileWriter("temp.txt", true);
        File f = new File("history.txt");
        Scanner fs = new Scanner(f);
        int wonumber = Integer.parseInt(fs.nextLine().substring(8)) + 1;
        fw.append("WoNumber"+wonumber);
        for (Exercise e : exercises) {
            fw.append("\n"+e.name+",");
            for (Sets s : e.set) {
                fw.append(s.weight+"x"+s.reps+",");
            }
        }
        fw.append("\nWoNumber"+(wonumber - 1)+"\n");
        while(fs.hasNextLine()){
            fw.append(fs.nextLine() + "\n");
        }
        fs.close();
        fw.close();
        if(f.exists()) f.delete();
        File temp = new File("temp.txt");
        temp.renameTo(f);
    }
}
