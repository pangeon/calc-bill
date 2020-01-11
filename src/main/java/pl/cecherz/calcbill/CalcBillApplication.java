package pl.cecherz.calcbill;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CalcBillApplication {

	public static void main(String[] args) {
		SpringApplication.run(CalcBillApplication.class, args);
		System.out.println("Aplikacja Calc-Bill uruchomiona.");
	}

}
