package SIMpleProp;

public class COG {
	
	public static final int COGRAM_GENERAL_REGS = 496;
	public static final int COGRAM_SPECIAL_REGS = 16;
	public static final int COGRAM_ADDRESS_SPACE = COGRAM_GENERAL_REGS + COGRAM_SPECIAL_REGS;
	
	public static final int COG_PAR = COGRAM_GENERAL_REGS;
	public static final int COG_CNT = COG_PAR + 1;
	public static final int COG_INA = COG_CNT + 1;
	public static final int COG_INB = COG_INA + 1;
	public static final int COG_OUTA = COG_INB + 1;
	public static final int COG_OUTB = COG_OUTA + 1;
	public static final int COG_DIRA = COG_OUTB + 1;
	public static final int COG_DIRB = COG_DIRA + 1;
	public static final int COG_CTRA = COG_DIRB + 1;
	public static final int COG_CTRB = COG_CTRA + 1;
	public static final int COG_FRQA = COG_CTRB + 1;
	public static final int COG_FRQB = COG_FRQA + 1;
	public static final int COG_PHSA = COG_FRQB + 1;
	public static final int COG_PHSB = COG_PHSA + 1;
	public static final int COG_VCFG = COG_PHSB + 1;
	public static final int COG_VSCL = COG_VCFG + 1;
	
	public int cogram[];
	
	public HUB hub;
	public int id;
	
	public int cflag;
	public int zflag;
	
	public int progcounter;
	private boolean initstate;
	private int initcount;
	private int initptr;
	private Instruction queue[] = {new Nop(this), new Nop(this)};
	private Instruction iTable[] = {new Rdwrbyte(this), new Rdwrword(this), new Rdwrlong(this), new Hubop(this), new Nop(this), 
			new Nop(this), new Nop(this), new Nop(this), new Ror(this), new Rol(this), new Shr(this), new Shl(this), new Rcr(this), 
			new Rcl(this), new Sar(this), new Rev(this), new Mins(this), new Maxs(this), new Min(this), new Max(this),
			new Movs(this), new Movd(this), new Movi(this), new Jmpret(this), new And(this), new Andn(this), new Or(this), 
			new Xor(this), new Muxc(this), new Muxnc(this), new Muxz(this), new Muxnz(this), new Add(this), new Sub(this), 
			new Addabs(this), new Subabs(this), };
	
	public COG(int id, int params, HUB hub) {
		
		queue = new Instruction[2];
		progcounter = 0;
		advanceCount();
		advanceCount();
		
		cogram = new int[COGRAM_ADDRESS_SPACE];
		cogram[COG_PAR] = (params >> 16) & 0b1111111111111100;
		
		initptr   = (params >> 2) & 0b1111111111111100;
		initstate = true;
		initcount = 0;
		this.hub = hub;
		this.id  = id;
		cflag = 0;
		zflag = 0;
	}
	
	public void advanceCount() { // we queue things to simulate the delay of field modifying instructions
		queue[0] = decode(queue[1].getInstruction());
		queue[1].setInstruction(cogram[progcounter++]);
	}
	
	public Instruction decode(int instr) {
		
		Instruction t = new Nop(this);
		
		t.setInstruction(instr);
		iTable[t.getOpcode()].setInstruction(instr);
		
		return iTable[t.getOpcode()];
	}
	
	public void step(int syscnt) {
		
		if (initstate) {
			
			if (!this.hubAccess()) {
				return;
			}
			
			cogram[initcount++] = hub.rdlong(initptr++);
			
			if (initcount == 496) {
				initstate = false;
			}
			
			return;
		}
		
		cogram[COG_CNT] = syscnt;
		
		if (queue[0].step()) {
			advanceCount();
		}
	}
	
	public boolean hubAccess() {
		
		if (hub.cogid == this.id) {
			return true;
		}
		
		return false;
		
	}
	
}