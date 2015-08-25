package threadPerClient;

import java.util.Scanner;
import java.util.concurrent.Callable;

public class CallableScanner implements Callable<Boolean> {

	// gets a string from the console input, check if it's exit, returns false if it is. returns true otherwise.
	public Boolean call() throws Exception {
		final Scanner scan = new Scanner(System.in);
		while(true){
			String s = scan.nextLine();
			if(s.equals("exit")){
				System.out.println("A graceful shutdown has been executed.. trying to shutdown the server!");
				scan.close();
				return Boolean.FALSE;
			}
		}
		
	}
}
