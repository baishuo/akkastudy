package akkachapter7.geym.akka.demo.future;

import akka.actor.UntypedActor;
import akkachapter7.geym.akka.demo.future.Printer.Msg;

public class Worker extends UntypedActor {

	@Override
	public void onReceive(Object msg) throws Exception {
		if (msg instanceof Integer) {
			int i = (Integer)msg;
			
			Thread.sleep(1000);
			getSender().tell(i*i, getSelf());
		}
        if (msg == Msg.DONE) {
			System.out.println("stop working");
		}
        if (msg == Msg.CLOSE) {
			System.out.println("I will shut down");
			getSender().tell(Msg.CLOSE, getSelf()); // »Ø¸´
        	getContext().stop(getSelf());
		} else {
			unhandled(msg);
		}
	}

}
