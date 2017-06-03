import java.util.*;
import java.io.*;

public class fastproc{
    private static String nextNonEmptyLine(BufferedReader bfr) throws IOException{
	String s;
	while ((s = bfr.readLine()) != null){
	    if (s.trim().length() > 0)
		return s.trim();
	}
	return null;
    }

    public static void main(String args[]) throws Exception{
	File in = new File(args[0]);
	String ccs = "";
	if (args.length > 1){
	    File cin = new File(args[1]);
	    BufferedReader cbfr = new BufferedReader(new FileReader(cin));
	    StringBuilder cc = new StringBuilder();
	    String s;
	    while ((s = cbfr.readLine()) != null){
		if (s.length() != 0)
		    cc.append(s + "\n");
	    }
	    ccs = cc.toString();
	}
	BufferedReader bfr = new BufferedReader(new FileReader(in));
	String scurr, slast = "";

	while ((scurr = nextNonEmptyLine(bfr)) != null){
	    boolean updateLast = true;
	    if ("函数名".equals(scurr)){
		System.out.println("-");
		System.out.print(" name: ");
	    } else if ("对应指令".equals(scurr)){
		System.out.print(" inst: ");
	    } else if ("参数说明".equals(scurr)){
		System.out.println(" params:");
	    } else if ("返回值".equals(scurr)){
		System.out.println(" -");
		System.out.print("  return ");
	    } else if ("功能说明".equals(scurr)){
		System.out.println(" desc: |");
	    } else {
		updateLast = false;
	    }
	    if (updateLast){
		slast = scurr;
		continue;
	    }
	    if ("函数名".equals(slast)){
		System.out.println(scurr.split(" ")[0]);
		System.out.print(ccs);
	    } else if ("对应指令".equals(slast)){
		System.out.println(scurr);
	    } else if ("参数说明".equals(slast)){
		System.out.println(" -");
		System.out.println("  " + scurr.split(";")[0] + ":");
	    } else if ("返回值".equals(slast)){
		System.out.println(scurr + ":");
	    } else if ("功能说明".equals(slast)){
		System.out.println("  " + scurr);
	    } else {
		updateLast = false;
	    }
	}
    }
}
