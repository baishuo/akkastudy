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
				Future<Integer> f = AgentDemo.counterAgent.alter(addMapper); // ����addMapperȥalter counterAgent
				AgentDemo.futures.add(f); // �����ص�future��������ռ�
			}
			getContext().stop(getSelf()); // actor �����˳��� inbox���յ���Ϣ
		} 
		 else {
            unhandled(msg);
		}
		
	}

}
