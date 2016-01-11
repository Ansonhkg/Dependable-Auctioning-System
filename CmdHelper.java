import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CmdHelper {

    public static String getText() throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("> ");
        System.out.flush();
        String line = in.readLine();
        
        if ("quit".equals(line) || "exit".equals(line))
            throw new IOException();
        
        return line;
    }
}