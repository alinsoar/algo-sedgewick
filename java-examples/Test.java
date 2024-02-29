
import java.util.function.BiFunction;

class Test {
  public static void main (String[] args) {
      int n = 10;
      if (args.length == 1) n = Integer.parseInt(args[0]);
      for (int i=0; i <= n; i++)
          System.out.println (((BiFunction<Integer, Integer, Integer>)
                               (x,y)->2*x+y).apply(i,1));
  }
}


