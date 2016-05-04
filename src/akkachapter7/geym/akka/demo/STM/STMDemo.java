package akkachapter7.geym.akka.demo.STM;

import java.util.concurrent.TimeUnit;

import com.typesafe.config.ConfigFactory;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.transactor.Coordinated;
import akka.util.Timeout;
import scala.concurrent.Await;

public class STMDemo {
	public static ActorRef company = null;
	public static ActorRef employee = null;

	public static void main(String[] args) throws Exception {
		final ActorSystem system = ActorSystem.create("transactionDemo", ConfigFactory.load("samplehello.conf"));
		company = system.actorOf(Props.create(CompanyActor.class),"compangy");
		employee = system.actorOf(Props.create(EmployeeActor.class), "employee");
		Timeout timeout = new Timeout(1, TimeUnit.SECONDS);
		
		for (int i = 0; i < 20; i++) {
			company.tell(new Coordinated(i, timeout), ActorRef.noSender());
			Thread.sleep(200);
			Integer companyCount = (Integer) Await.result(Patterns.ask(company, "GetCount", timeout), timeout.duration());
			Integer employCount = (Integer) Await.result(Patterns.ask(employee,"GetCount", timeout), timeout.duration());
			System.out.println("company count=" + companyCount);
			System.out.println("employee count=" + employCount);
			System.out.println("===================================");
		}

	}

}
