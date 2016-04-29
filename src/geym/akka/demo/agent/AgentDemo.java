package geym.akka.demo.agent;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

import com.typesafe.config.ConfigFactory;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import akka.actor.Props;
import akka.actor.Terminated;
import akka.actor.Inbox;
import akka.agent.Agent;
import akka.dispatch.ExecutionContexts;
import akka.dispatch.Futures;
import akka.dispatch.OnComplete;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;


public class AgentDemo {
    public static Agent<Integer> counterAgent = Agent.create(0, ExecutionContexts.global());
    static ConcurrentLinkedQueue<Future<Integer>> futures = new ConcurrentLinkedQueue<Future<Integer>>();
	
    public static void main(String[] args) {
		final ActorSystem system = ActorSystem.create("route", ConfigFactory.load("sampleHello.conf"));
		
		ActorRef[] counter = new ActorRef[10];
		
		for (int i= 0; i < counter.length; i ++) {
			counter[i] = system.actorOf(Props.create(CounterActor.class), "counter_" + i);
		}
		
		final Inbox inbox = Inbox.create(system);
		
		for (int i = 0; i < counter.length; i++) {
			inbox.send(counter[i], 1); // ����CounterActor ��ʼ�ۼ�
			inbox.watch(counter[i]); // ������䣬�ͻᱨ deadline passed�� ���� onComplete ִ�в���
		}
		
		int closeCount = 0;
		
		while (true) {
			Object msg = inbox.receive(Duration.create(1, TimeUnit.SECONDS));
			if (msg instanceof Terminated) {
				closeCount ++;
				if (closeCount == counter.length) { // 10�� ȫ������
					break;
				}
			} else {
                System.out.println(msg);
			}
			
		}
		
		// Futures.sequence �Ǿ�̬����
		Futures.sequence(futures, system.dispatcher()).onComplete(
				new OnComplete<Iterable<Integer>>() {
					@Override
					public void onComplete(Throwable arg0, Iterable<Integer> arg1) throws Throwable {
						System.out.println("futures:" + futures.size());
						System.out.println("counterAgent=" + counterAgent.get());
						system.shutdown();
					}
				}, system.dispatcher()
				);

	}

}
