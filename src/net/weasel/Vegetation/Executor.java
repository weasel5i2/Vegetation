package net.weasel.Vegetation;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Executor implements Runnable {

	private Queue<ChunkTask> jobs;
	
	public Executor() {
		this.jobs = new ConcurrentLinkedQueue<ChunkTask>();
	}
	
	public void addJob(ChunkTask r) {
		jobs.add(r);
	}
	
	@Override
	public void run() {
		if(jobs.peek() != null) {
			jobs.poll().run();
		}
	}
	
	

}
