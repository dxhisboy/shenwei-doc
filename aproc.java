import java.util.*;
import java.io.*;

public class aproc{
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
	StringBuilder dpBuilder = new StringBuilder(), spBuilder = new StringBuilder();
	while ((scurr = nextNonEmptyLine(bfr)) != null){
	    boolean updateLast = true;
	    if ("函数名".equals(scurr)){
		//System.out.println("-");
		//System.out.print(" name: ");
		if (dpBuilder.toString().length() > 0){
		    String dpd = dpBuilder.toString();
		    String spd = spBuilder.toString();
		    if (!dpd.equals(spd)){
			System.out.print(dpd);
			System.out.print(ccs);
		    }
		    
		    System.out.print(spd);
		    if (spd.contains("floatv4")){
			System.out.print(ccs.replaceAll("SIMD-dp", "SIMD-sp"));
		    }else
			System.out.print(ccs);
		    
		}
		dpBuilder = new StringBuilder();
		spBuilder = new StringBuilder();
		dpBuilder.append("-\n");
		dpBuilder.append(" name: ");
		spBuilder.append("-\n");
		spBuilder.append(" name: ");
	    } else if ("对应指令".equals(scurr)){
		//System.out.print(" inst: ");
		dpBuilder.append(" inst: ");
		spBuilder.append(" inst: ");
	    } else if ("参数说明".equals(scurr)){
		//System.out.println(" params:");
		dpBuilder.append(" params:\n");
		spBuilder.append(" params:\n");
	    } else if ("返回值".equals(scurr)){
		//System.out.println(" -");
		//System.out.print("  return ");
		dpBuilder.append(" -\n");
		dpBuilder.append("  return ");
		spBuilder.append(" -\n");
		spBuilder.append("  return ");
	    } else if ("功能说明".equals(scurr)){
		dpBuilder.append(" desc: |" + "\n");
		spBuilder.append(" desc: |" + "\n");
	    } else {
		updateLast = false;
	    }
	    if (updateLast){
		slast = scurr;
		continue;
	    }
	    if ("函数名".equals(slast)){
		dpBuilder.append(scurr.split(" ")[0] + "\n");
		//dpBuilder.append(ccs);
		spBuilder.append(scurr.split(" ")[0] + "\n");
		//spBuilder.append(ccs);
	    } else if ("对应指令".equals(slast)){
		dpBuilder.append(scurr + "\n");
		spBuilder.append(scurr + "\n");
	    } else if ("参数说明".equals(slast)){
		String pm = scurr.split(";")[0];
		String dppm = pm.replaceAll("doublev4/floatv4", "doublev4");
		String sppm = pm.replaceAll("doublev4/floatv4", "floatv4");
		dpBuilder.append(" -" + "\n");
		dpBuilder.append("  " + dppm + ":" + "\n");
		spBuilder.append(" -" + "\n");
		spBuilder.append("  " + sppm + ":" + "\n");
	    } else if ("返回值".equals(slast)){
		String rt = scurr.split(" ")[0];
		String dprt = rt.replaceAll("doublev4/floatv4", "doublev4");
		String sprt = rt.replaceAll("doublev4/floatv4", "floatv4");
		dpBuilder.append(dprt + ":" + "\n");
		spBuilder.append(sprt + ":" + "\n");
	    } else if ("功能说明".equals(slast)){
		dpBuilder.append("  " + scurr + "\n");
		spBuilder.append("  " + scurr + "\n");
	    } else {
		updateLast = false;
	    }
	}
	if (dpBuilder.toString().length() > 0){
	    String dpd = dpBuilder.toString();
	    String spd = spBuilder.toString();
	    if (!dpd.equals(spd)){
		System.out.print(dpd);
		System.out.print(ccs);
	    }
		    
	    System.out.print(spd);
	    if (spd.contains("floatv4")){
		System.out.print(ccs.replaceAll("SIMD-dp", "SIMD-sp"));
	    }else
		System.out.print(ccs);
		    
	}

    }
}
