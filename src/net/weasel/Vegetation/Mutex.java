package net.weasel.Vegetation;

public class Mutex {

	private boolean lock;
	
	public Mutex()
	{
		lock = false;
	}
	
	public void aquire()
	{
		while( isLocked() ) {}
		lock = true;
	}
	
	public void release()
	{
		lock = false;
	}
	
	public boolean isLocked()
	{
		return lock;
	}
}