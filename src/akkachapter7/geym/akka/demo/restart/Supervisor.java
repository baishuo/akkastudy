package akkachapter7.geym.akka.demo.restart;

import java.util.concurrent.TimeUnit;

import akka.actor.OneForOneStrategy;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import akka.actor.UntypedActor;
import akka.actor.SupervisorStrategy.Directive;
import akka.japi.Function;
import scala.concurrent.duration.Duration;

public class Supervisor extends UntypedActor { // 1 分钟内重试三次
	private static SupervisorStrategy straregy = new OneForOneStrategy(3, 
			Duration.create(1,TimeUnit.MINUTES), 
			new Function<Throwable, SupervisorStrategy.Directive>() {
				
				@Override
				public Directive apply(Throwable t) throws Exception {
					if (t instanceof ArithmeticException) {
						System.out.println("meet ArithmeticException just resume");
						return SupervisorStrategy.resume();
					} else if (t instanceof NullPointerException) {
						System.out.println("meet NullPointerException just resume");
						return SupervisorStrategy.restart();
					} else if (t instanceof IllegalArgumentException) {
						return SupervisorStrategy.stop();
					} else {
						return SupervisorStrategy.escalate();
					}
				}
			});
	
	@Override
	public SupervisorStrategy supervisorStrategy() {
		return straregy;
	}

	@Override
	public void onReceive(Object o) throws Exception {
		if (o instanceof Props) {
			getContext().actorOf((Props)o, "restartActor");
		} else {
			unhandled(o);
		}

	}

}
