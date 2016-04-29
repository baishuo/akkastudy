package geym.akka.demo.future;

import akka.actor.UntypedActor;


public class Printer extends UntypedActor {
	public static enum Msg {
		DONE,CLOSE;
	}
	@Override
	public void onReceive(Object msg) throws Exception {
		if (msg instanceof Integer) {
			System.out.println("Printer:" + msg);
		}
		else if (msg == Msg.DONE) {
			System.out.println("stop working");
		}
		else if (msg == Msg.CLOSE) {
        	System.out.println("I will shutdown");
        	getSender().tell(Msg.CLOSE, getSelf()); // »Ø¸´
        	getContext().stop(getSelf());
		} else {
			unhandled(msg);
		}
        
	}

}
