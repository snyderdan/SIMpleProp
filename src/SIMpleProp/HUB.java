package SIMpleProp;

public class HUB {
	
	public static final int RDLONG = 1;
	public static final int RDWORD = 2;
	public static final int RDBYTE = 3;
	public static final int WRLONG = 4;
	public static final int WRWORD = 5;
	public static final int WRBYTE = 6;
	
	public static final int ROM_START = 32*1024;
	
	public static COG cogs[];
	public static byte locks[];
	public static byte memory[];
	
	public int cogid;
	public int cogsused;
	public int locksused;
	
	
	public HUB() {
		cogs = new COG[8];
		locks = new byte[8];
		memory = new byte[64*1024];
	}
	
	public int rdlong(int address) {
		address &= 0xFFFC;
		return ((int)(memory[address+3]) << 24) | ((int)(memory[address+2]) << 16) |
				((int)(memory[address+1]) << 8) | ((int)(memory[address]));
	}
	
	public int rdword(int address) {
		address &= 0xFFFE;
		return ((int)(memory[address+1]) << 8) | ((int)(memory[address]));
	}
	
	public int rdbyte(int address) {
		return (int) memory[address];
	}
	
	public void wrlong(int address, int data) {
		
		if (address >= ROM_START) return;
		
		address &= 0xFFFC;
		memory[address+3] = (byte) (data >>> 24);
		memory[address+2] = (byte) (data >>> 16);
		memory[address+1] = (byte) (data >>> 8);
		memory[address+0] = (byte) (data);
	
	}
	
	public void wrword(int address, int data) {
		
		if (address >= ROM_START) return;
		
		address &= 0xFFFE;
		memory[address+1] = (byte) (data >>> 8);
		memory[address+0] = (byte) (data);
	
	}
	
	public void wrbyte(int address, int data) {
		
		if (address >= ROM_START) return;
		
		memory[address] = (byte) data;
		
	}
			
	public int coginit(int data) {
		
		int i;
		
		if ((data & 0b1000) != 0) {	// set for COGNEW
			
			for (i=0; i<8; i++) {
				if (cogs[i] == null) {
					cogs[i] = new COG(i, data, this);
					return i;
				}
			}
			
			return i;
		} else {		// set for REINIT
			cogs[data & 0b111] = new COG(data & 0b111, data, this);
			return data & 0b111;
		}
	}
	
	public int cogstop(int data) {
		cogs[data & 0b111] = null;
		return data & 0b111;
	}
	
	public int activeCogCount() {
		return cogsused;
	}
	
	public int locknew() {
		
		int i;

		for (i=0; i<8; i++) {
			
			if ((locks[i] & 2) == 0) {
				locks[i] = 0b11;
				locksused++;
				return i;
			}
			
		}
		
		return i;
	}
	
	public int lockret(int lock) {
		lock &= 0b111;
		locks[lock] = 0;
		locksused--;
		return lock;
	}
	
	public int lockset(int lock) {
		
		int state;
		
		lock &= 0b111;
		state = locks[lock] & 1;
		locks[lock] |= 1;
		return state;
	}
	
	public int lockclr(int lock) {
		
		int state;
		
		lock &= 0b111;
		state = locks[lock] & 1;
		locks[lock] &= 2;
		return state;
	}

	
	public int activeLockCount() {
		return locksused;
	}
}
