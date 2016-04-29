package geym.akka.demo.restart;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Option;
import geym.akka.demo.hello.MyWorker.Msg;

public class RestartActor extends UntypedActor {

	private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	public static enum Msg {
		DONE,RESTART;
	}
	@Override
	public void preStart() throws Exception {
		System.out.println("preStart hashcode:" + this.hashCode());
	};
	@Override
	public void postStop() throws Exception {
		System.out.println("postStop hashcode:" + this.hashCode());
	};
	@Override
	public void postRestart(Throwable reason) throws Exception {
		super.postRestart(reason);
		System.out.println("postRestart hashcode:" + this.hashCode());
	};
	
	@Override
	public void preRestart(Throwable reason, scala.Option<Object> message) throws Exception {
		System.out.println("preRestart hashcode:" + this.hashCode());
	};
	
	@Override
	public void onReceive(Object msg) throws Exception {
		if (msg == Msg.DONE) {
			System.out.println("stop working");
		}
		if (msg == Msg.RESTART) {
			System.out.println(((Object)null).toString());
			double a = 0 / 0;
		} else {
            unhandled(msg);
		}

	}

}
