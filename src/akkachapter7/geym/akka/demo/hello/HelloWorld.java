package akkachapter7.geym.akka.demo.hello;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akkachapter7.geym.akka.demo.hello.Greeter.Msg;

public class HelloWorld extends UntypedActor {
	ActorRef greeter;

	@Override
	public void preStart() {
		greeter = getContext().actorOf(Props.create(Greeter.class), "greeter");
		System.out.println("Greeter Actor Path:" + greeter.path());
		greeter.tell(Greeter.Msg.GREET, getSelf());
	}
	
	@Override
	public void onReceive(Object msg) throws Exception {
		// TODO Auto-generated method stub
		if (msg == Greeter.Msg.DONE) {
			greeter.tell(Greeter.Msg.GREET, getSelf());
			getContext().stop(getSelf());
		} else {
			unhandled(msg);
		}
	}

}
