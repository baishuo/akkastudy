package akkachapter7.geym.akka.demo.STM;

import akka.actor.UntypedActor;
import akka.transactor.Coordinated;
import scala.concurrent.stm.Ref.View;
import scala.concurrent.stm.japi.STM;

public class EmployeeActor extends UntypedActor {
	private View<Integer> count = STM.newRef(50);
	@Override
	public void onReceive(Object msg) throws Exception {
		if (msg instanceof Coordinated) {
			final Coordinated c = (Coordinated) msg;
			final int downCount = (Integer)c.getMessage();
			try {
				c.atomic(new Runnable() {
					
					@Override
					public void run() {
						STM.increment(count, downCount); //’Àªßº”«Æ¿≤£°
						
					}
				});
			} catch (Exception e) {
			}
		} else if ("GetCount".equals(msg)) {
	          getSender().tell(count.get(), getSelf());
		} else {
              unhandled(msg);
		}

	}

}
