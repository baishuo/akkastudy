package akkachapter7.geym.akka.demo.agent;

import akka.actor.UntypedActor;
import akka.dispatch.Mapper;
import scala.concurrent.Future;


public class CounterActor  extends UntypedActor {
	Mapper<Integer, Integer> addMapper = new Mapper<Integer, Integer>() {
		@Override
		public Integer apply(Integer i) {
			return i +1;
		}
	};

	@Override
	public void onReceive(Object msg) throws Exception {
		if (msg instanceof Integer) {
			for (int i = 0; i <10000; i ++) {
				Future<Integer> f = AgentDemo.counterAgent.alter(addMapper); // 调用addMapper去alter counterAgent
				AgentDemo.futures.add(f); // 将返回的future对象进行收集
			}
			getContext().stop(getSelf()); // actor 自行退出， inbox会收到消息
		} 
		 else {
            unhandled(msg);
		}
		
	}

}
