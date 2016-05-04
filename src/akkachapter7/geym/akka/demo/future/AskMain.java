package akkachapter7.geym.akka.demo.future;

import java.nio.channels.Pipe;
import java.util.concurrent.TimeUnit;

import com.typesafe.config.ConfigFactory;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import akka.actor.Props;
import akka.pattern.Patterns;


import akkachapter7.geym.akka.demo.watcher.WatchActor;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

public class AskMain {

	public static void main(String[] args) throws Exception {
		ActorSystem system = ActorSystem
				.create("askDemo", ConfigFactory.load("sampleHello.conf"));
		ActorRef worker = system.actorOf(Props.create(Worker.class), "worker");
		ActorRef printer = system.actorOf(Props.create(Printer.class), "printer");
		system.actorOf(Props.create(WatchActor.class, worker), "Watcher");
		
		Future<Object> f = Patterns.ask(worker, 5, 1500);
		int re = (int) Await.result(f, Duration.create(6, TimeUnit.SECONDS));
		f = Patterns.ask(worker, 6, 1500);
		Patterns.pipe(f, system.dispatcher()).to(printer);
		worker.tell(PoisonPill.getInstance(), ActorRef.noSender());
	}

}
