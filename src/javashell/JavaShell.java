package javashell;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Sonic
 */

public class JavaShell {

    private static boolean toLoad = false, toExit = false;
    
    private static Method[] inputHandlers;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        for(String arg : args) if(arg.equals("--plugins")) toLoad = true;
        
        if(toLoad) getPlugins();
        
        Scanner scan = new Scanner(System.in);
        while(!toExit){
            System.out.print("\n> ");
            String command = scan.nextLine();
            handleInput(command);
        }
    }
    
    private static void handleInput(String line){
        if(line.contains("echo ")) System.out.print(line.substring(5));
        else if(line.equalsIgnoreCase("exit") || line.equalsIgnoreCase("exit ")) toExit = true;
        else if (toLoad) handlePluginInput(line);
        else System.out.println("Invalid command!");
    }
    
    private static void getPlugins(){
        File pluginDir = new File(System.getProperty("user.dir") + "/plugins/");
        File[] contents = pluginDir.listFiles();
        File[] infoList = new File[contents.length/2];
        File[] pluginList = new File[contents.length/2];
        String[] classList = new String[contents.length/2];
        
        int i = 0;
        for(File f : contents) {
            if(f.getName().endsWith(".txt")) {
                infoList[i] = f;
                i++;
            }
        }
        
        i = 0;
        for(File f : contents) {
            if(f.getName().endsWith(".jar")) {
                pluginList[i] = f;
                i++;
            }
        }
        
        i = 0;
        for(File f : infoList) {
            classList[i] = getPluginInfo(f);
            i++;
        }
        
        loadPlugins(pluginList, classList, pluginList.length);
    }
    
    private static void handlePluginInput(String line){
        try {
            String result = "-1";
            
            for(Method mtd : inputHandlers) {
                result = mtd.invoke(mtd, line).toString();
                if(result.equals("0")) break;
            }
            
            if(result.equals("-1")) System.out.println("Invalid command!");
            
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(JavaShell.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private static Method loadSingle(File path, String CLASS){
        
        Method toreturn = null;
        
        try {
            URL classUrl = path.toURI().toURL();
            URL[] urls = {classUrl};
            
            URLClassLoader cld = new URLClassLoader(urls);
            Class loadedClass = Class.forName(CLASS, false, cld);
            
            for(Method mtd : loadedClass.getMethods()) if(mtd.getName().equals("handleInput")) toreturn = mtd;
        } catch (MalformedURLException | ClassNotFoundException ex) {
            Logger.getLogger(JavaShell.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return toreturn;
    }
    
    private static String getPluginInfo(File f){
        String str = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            str = br.readLine();
            br.close();
        } catch (FileNotFoundException ex) {
            return "";
        } catch (IOException ex) {
            Logger.getLogger(JavaShell.class.getName()).log(Level.SEVERE, null, ex);
        }
        return str;
    }

    private static void loadPlugins(File[] pluginList, String[] classList, int length) {
        inputHandlers = new Method[length];
        for(int i = 0; i < length; i++) inputHandlers[i] = loadSingle(pluginList[i], classList[i]);
    }
    
}
