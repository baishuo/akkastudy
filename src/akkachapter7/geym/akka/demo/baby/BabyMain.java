package akkachapter7.geym.akka.demo.baby;

import com.typesafe.config.ConfigFactory;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import akka.actor.Props;
import akkachapter7.geym.akka.demo.router.WatchActor;

public class BabyMain {

	public static void main(String[] args) {
		ActorSystem system = ActorSystem.create("route", ConfigFactory.load("sampleHello.conf"));
		ActorRef child = system.actorOf(Props.create(BabyActor.class), "baby");
		system.actorOf(Props.create(WatchActor.class), "watcher");
		child.tell(BabyActor.Msg.PLAY, ActorRef.noSender());
		child.tell(BabyActor.Msg.SLEEP, ActorRef.noSender());
		child.tell(BabyActor.Msg.PLAY, ActorRef.noSender());
		child.tell(BabyActor.Msg.PLAY, ActorRef.noSender());
		
		child.tell(PoisonPill.getInstance(), ActorRef.noSender());
	}

}
