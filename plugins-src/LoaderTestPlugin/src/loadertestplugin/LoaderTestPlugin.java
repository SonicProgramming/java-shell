package loadertestplugin;

/**
 * @author 123
 */
public class LoaderTestPlugin {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("loadertestplugin.LoaderTestPlugin");
    }
    
    public static String handleInput(String line){
        String output = "-1";
        
        if(line.equals("help")) {
            System.out.println("exit (ignore case) - exit program\necho <arg> - echo your string");
            output = "0";
        }
        
        return output;
    }
    
}
