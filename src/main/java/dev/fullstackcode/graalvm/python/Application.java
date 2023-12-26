package dev.fullstackcode.graalvm.python;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {

		try {
	//	System.getProperties().list(System.out);
		Process p = Runtime.getRuntime().exec("uname -r");
		BufferedReader in = new BufferedReader(
				new InputStreamReader(p.getInputStream()));

			String distro = in.readLine();
		//	System.out.println("distro  -> "+distro);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		SpringApplication.run(Application.class, args);
	}

}
