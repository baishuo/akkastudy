package akkachapter7.geym.akka.demo.hello;

import java.util.concurrent.TimeUnit;

import com.typesafe.config.ConfigFactory;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Inbox;
import akka.actor.Props;
import akka.actor.Terminated;
import scala.concurrent.duration.Duration;


public class InboxMain {

	public static void main(String[] args) {
		ActorSystem system = ActorSystem.create("inboxdemo", ConfigFactory.load("sampleHello.conf"));
		ActorRef worker = system.actorOf(Props.create(MyWorker.class), "worker");
		
		final Inbox inbox = akka.actor.Inbox.create(system);
		inbox.watch(worker);
		inbox.send(worker, MyWorker.Msg.WORKING);
		inbox.send(worker, MyWorker.Msg.DONE);
		inbox.send(worker, MyWorker.Msg.CLOSE);
		while (true) {
			Object msg = inbox.receive(Duration.create(1, TimeUnit.SECONDS));
			if (msg == MyWorker.Msg.CLOSE) {
				System.out.println("My worker is closed");
			} else if (msg instanceof Terminated) {
				System.out.println("My worker is dead");
				system.shutdown();
				break;
			} else {
				System.out.println(msg);
			}
		}

	}

}
