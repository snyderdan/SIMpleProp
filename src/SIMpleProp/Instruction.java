package SIMpleProp;

public abstract class Instruction {
	
	protected final static int SRC_OFFSET  = 0;
	protected final static int DEST_OFFSET = 9;
	protected final static int COND_OFFSET = 18;
	protected final static int IFLG_OFFSET = 22;
	protected final static int RFLG_OFFSET = 23;
	protected final static int CFLG_OFFSET = 24;
	protected final static int ZFLG_OFFSET = 25;
	protected final static int ISTR_OFFSET = 26;
	
	protected final static int SRC_MASK  = 0x1FF << SRC_OFFSET;
	protected final static int DEST_MASK = 0x1FF << DEST_OFFSET;
	protected final static int COND_MASK = 0xF   << COND_OFFSET;
	protected final static int IFLG_MASK = 0x1   << IFLG_OFFSET;
	protected final static int RFLG_MASK = 0x1   << RFLG_OFFSET;
	protected final static int CFLG_MASK = 0x1   << CFLG_OFFSET;
	protected final static int ZFLG_MASK = 0x1   << ZFLG_OFFSET;
	protected final static int ISTR_MASK = 0x3F  << ISTR_OFFSET;
	
	protected final static long INT_MASK = 0xFFFFFFFF;
	
	public final static int cycles = 4;	// Number of cycles that a particular instruction takes (min)
	
	public COG cog;
	public int count;
	public int instr;
	
	public Instruction(COG cog) {
		this.count = 0;
		this.cog = cog;
	}

	public boolean step() {	// method called at each clock cycle
		
		if (!this.testCondition()) {
			if (++count == Nop.cycles) {
				return true;
			} else {
				return false;
			}
		}
		
		if (++count == cycles) {
			this.execute();
		}
		
		if (count >= cycles) {
			return true;
		}
		
		return false;
		
	}
	
	// Following methods just deconstruct instruction
	
	public int getSrcField() {
		return (instr & SRC_MASK) >>> SRC_OFFSET;
	}
	
	public int getDestField() {
		return (instr & DEST_MASK) >>> DEST_OFFSET;
	}
	
	public int getCondition() {
		return (instr & COND_MASK) >>> COND_OFFSET;
	}
	
	public int getIFlag() {
		return (instr & IFLG_MASK) >>> IFLG_OFFSET;
	}
	
	public int getRFlag() {
		return (instr & RFLG_MASK) >>> RFLG_OFFSET;
	}
	
	public int getCFlag() {
		return (instr & CFLG_MASK) >>> CFLG_OFFSET;
	}
	
	public int getZFlag() {
		return (instr & ZFLG_MASK) >>> ZFLG_OFFSET;
	}
	
	public int getOpcode() {
		return (instr & ISTR_MASK) >>> ISTR_OFFSET;
	}
	
	public int getInstruction() {
		return this.instr;
	}
	
	public void setInstruction(int instr) {
		this.count = 0;
		this.instr = instr;
	}
	
	protected void writeResult(int data) {	// handles whether or not we write the result
		
		if (getRFlag() == 0) {
			return;
		}
		
		cog.cogram[getDestField()] = data;
		
	}
	
	protected int getSource() {
		
		int src = getSrcField();
		
		if (getIFlag() == 1) {
			return src;
		}
		
		return cog.cogram[src];
		
	}
	
	protected int getDestination() {
		return cog.cogram[getDestField()];
	}
	
	protected void setC(boolean data) {
		if (getCFlag() == 1) {
			cog.cflag = data ? 1 : 0;
		}
	}
	
	protected void setZ(boolean data) {
		if (getZFlag() == 1) {
			cog.zflag = data ? 1 : 0;
		}
	}
	
	protected boolean testCondition() {
		
		switch (this.getCondition()) {
		case 0:
			return false;
		case 1:
			return (cog.cflag == 0) && (cog.zflag == 0);
		case 2:
			return (cog.cflag == 0) && (cog.zflag == 1);
		case 3:
			return (cog.cflag == 0);
		case 4:
			return (cog.cflag == 1) && (cog.zflag == 0);
		case 5:
			return (cog.zflag == 0);
		case 6:
			return (cog.cflag != cog.zflag);
		case 7:
			return (cog.cflag == 0) | (cog.zflag == 0);
		case 8:
			return (cog.cflag == 1) && (cog.zflag == 1);
		case 9:
			return (cog.cflag == cog.zflag);
		case 10:
			return (cog.zflag == 1);
		case 11:
			return (cog.cflag == 0) | (cog.zflag == 1);
		case 12:
			return (cog.cflag == 1);
		case 13:
			return (cog.cflag == 1) | (cog.zflag == 0);
		case 14:
			return (cog.cflag == 1) | (cog.zflag == 1);
		case 15:
			return true;
		default:
			return false;
			
		}
	}
	
	
	protected abstract void execute();	// Code part to be filled by each instruction definition
	
}

/**
 * Instruction classes
 */

class Nop extends Instruction { // no real NOP opcode - just a filler for undefined opcodes
	
	public Nop(COG cog) {
		super(cog);
		// TODO Auto-generated constructor stub
	}

	protected void execute() {
		// TODO Auto-generated method stub
		
	}	
}

class Rdwrbyte extends Instruction {
	
	public final int cycles = 8;

	public Rdwrbyte(COG cog) {
		super(cog);
		// TODO Auto-generated constructor stub
	}
	
	protected void execute() {
		
		if (cog.hubAccess()) {	// If our cog has hub access, then perform operation
			
			if (this.getRFlag() == 0) { // WRBYTE
				
				cog.hub.wrbyte(this.getSource(), this.getDestination());
				
				setZ(((this.getSource() & 0b11) != 0));
				setC(false);
				
			} else {		// RDBYTE
				
				writeResult(cog.hub.rdbyte(this.getSource()));
				
				setC((this.getDestination() == 0));
				setZ(false);
			}
			
		} else {	// otherwise we continue to wait
			count--;
		}
	}
}

class Rdwrword extends Instruction {
	
	public final int cycles = 8;

	public Rdwrword(COG cog) {
		super(cog);
		// TODO Auto-generated constructor stub
	}
	
	protected void execute() {
		
		if (cog.hubAccess()) {	// If our cog has hub access, then perform operation
			
			if (this.getRFlag() == 0) { // WRWORD
				
				cog.hub.wrword(this.getSource(), this.getDestination());
				
				setZ(((this.getSource() & 0b11) == 0));
				setC(false);
				
			} else {		// RDWORD
				
				writeResult(cog.hub.rdword(this.getSource()));
				
				setC((this.getDestination() == 0));
				setZ(false);
			}
			
		} else {	// otherwise we continue to wait
			count--;
		}
	}
}

class Rdwrlong extends Instruction {
	
	public final int cycles = 8;

	public Rdwrlong(COG cog) {
		super(cog);
		// TODO Auto-generated constructor stub
	}
	
	protected void execute() {
		
		if (cog.hubAccess()) {	// If our cog has hub access, then perform operation
			
			if (this.getRFlag() == 0) { // WRLONG
				
				cog.hub.wrlong(this.getSource(), this.getDestination());
				
				setZ(false);
				setC(false);
				
			} else {		// RDLONG
				
				writeResult(cog.hub.rdlong(this.getSource()));
				
				setC((this.getDestination() == 0));
				setZ(false);
			}
			
		} else {	// otherwise we continue to wait
			count--;
		}
	}
}

class Hubop extends Instruction {		// fuck everything about this instruction
	
	public final int cycles = 8;
	
	public Hubop(COG cog) {
		super(cog);
		// TODO Auto-generated constructor stub
	}

	protected void execute() {
		
		int result;
		
		if (!cog.hubAccess()) {
			count--;
			return;
		}

		switch (instr & 0b111) {
		
		case 0:		// CLKSET -- I don't really use a clock at the moment.
			break;
			
		case 1:		// COGID 			
			this.writeResult(cog.id);
			setZ((cog.id == 0));
			setC(false);
			break;
			
		case 2:		// COGINIT
			result = cog.hub.coginit(this.getDestination());
			setC(result > 7);
			setZ((result == 0));
			this.writeResult(result);

			if (result > 7) {
				this.writeResult(7);
			}
			
			break;
		
		case 3:		// COGSTOP
			result = cog.hub.cogstop(this.getDestination());
			setC(result == 7);
			setZ(result == 0);
			this.writeResult(result);
			break;
			
		case 4:		// LOCKNEW
			result = cog.hub.locknew();
			setC(result > 7);
			setZ(result == 0);
			this.writeResult(result);
			
			if (result > 7) {
				this.writeResult(7);
			}
			break;
			
		case 5:		// LOCKRET
			cog.hub.lockret(this.getDestination());
			this.writeResult(this.getDestination() & 0b111);
			setC((cog.hub.activeLockCount() == 7));
			setZ((this.getDestination() & 0b111) == 0);
			break;
			
		case 6:		// LOCKSET
			this.writeResult(this.getDestination() & 0b111);
			setC((cog.hub.lockset(this.getDestination()) == 1));
			setZ((this.getDestination() & 0b111) == 0);
			break;
			
		case 7:		// LOCKCLR
			this.writeResult(this.getDestination() & 0b111);
			setC((cog.hub.lockclr(this.getDestination()) == 1));
			setZ((this.getDestination() & 0b111) == 0);
			break;
		}
	}
}

class Ror extends Instruction {
	
	public Ror(COG cog) {
		super(cog);
		// TODO Auto-generated constructor stub
	}

	protected void execute() {
		
		setZ(this.getDestination() == 0);
		setC((this.getDestination() & 1) == 1);
		
		this.writeResult(java.lang.Integer.rotateRight(this.getDestination(), this.getSource() & 0x1F));
	}
}

class Rol extends Instruction {
	
	public Rol(COG cog) {
		super(cog);
		// TODO Auto-generated constructor stub
	}

	protected void execute() {
		
		setZ(this.getDestination() == 0);
		setC((this.getDestination() & 0x80000000) == 0x80000000);
		
		this.writeResult(java.lang.Integer.rotateLeft(this.getDestination(), this.getSource() & 0x1F));
	}
}

class Shr extends Instruction {
	
	public Shr(COG cog) {
		super(cog);
		// TODO Auto-generated constructor stub
	}

	protected void execute() {
		
		int workspace; 
		
		setC((this.getDestination() & 1) == 1);
		
		workspace = this.getDestination() >>> (this.getSource() & 0x1F);
		
		setZ(workspace == 0);
		this.writeResult(workspace);
	}
}

class Shl extends Instruction {
	
	public Shl(COG cog) {
		super(cog);
		// TODO Auto-generated constructor stub
	}

	protected void execute() {
		
		int workspace; 
		
		setC((this.getDestination() & 0x80000000) == 0x80000000);
		
		workspace = this.getDestination() << (this.getSource() & 0x1F);
		
		setZ(workspace == 0);
		this.writeResult(workspace);
	}
}

class Rcr extends Instruction {
	
	public Rcr(COG cog) {
		super(cog);
		// TODO Auto-generated constructor stub
	}

	protected void execute() {
		
		int workspace, i, mask;
		
		mask = (cog.cflag == 1) ? 0x80000000 : 0;
		workspace = this.getDestination();
		
		setC((this.getDestination() & 1) == 1);
		
		for (i=0; i<(this.getSource() & 0x1F); i++) {
			workspace >>>= 1;
			workspace |= mask;
		}
		
		setZ(workspace == 0);
		this.writeResult(workspace);
	}
}

class Rcl extends Instruction {
	
	public Rcl(COG cog) {
		super(cog);
		// TODO Auto-generated constructor stub
	}

	protected void execute() {
		
		int workspace, i, mask;
		
		mask = (cog.cflag == 1) ? 1 : 0;
		workspace = this.getDestination();
		
		setC((this.getDestination() & 0x80000000) == 0x80000000);
		
		for (i=0; i<(this.getSource() & 0x1F); i++) {
			workspace <<= 1;
			workspace |= mask;
		}
		
		setZ(workspace == 0);
		this.writeResult(workspace);
	}
}

class Sar extends Instruction {
	
	public Sar(COG cog) {
		super(cog);
		// TODO Auto-generated constructor stub
	}

	protected void execute() {
		
		int workspace; 
		
		setC((this.getDestination() & 1) == 1);
		
		workspace = this.getDestination() >> (this.getSource() & 0x1F);
		
		setZ(workspace == 0);
		this.writeResult(workspace);
	}
}

class Rev extends Instruction {
	
	public Rev(COG cog) {
		super(cog);
		// TODO Auto-generated constructor stub
	}

	protected void execute() {
		
		int dest = this.getDestination(), 
				src = this.getSource(), 
				workspace = 0,
				mask = 1, 
				i;
		
		setC((dest & 1) == 1);
		
		for (i=0; i<src; i++) {
			mask |= (mask << 1);
		}
		
		workspace = mask & dest;
		dest &= (0xFFFFFFFF ^ mask);
		
		java.lang.Integer.reverse(workspace);
		
		dest |= workspace;
		
		this.writeResult(dest);
		setZ(dest == 0);
	}
}

class Mins extends Instruction {

	public Mins(COG cog) {
		super(cog);
		// TODO Auto-generated constructor stub
	}
	
	protected void execute() {
		
		int src = getSource(), dest = getDestination();
		
		setC(dest < src);
		setZ(src == 0);
		this.writeResult((dest < src) ? dest : src);
	}
}

class Maxs extends Instruction {
	
	public Maxs(COG cog) {
		super(cog);
		// TODO Auto-generated constructor stub
	}
	
	protected void execute() {
		
		int src = getSource(), dest = getDestination();
		
		setC(dest > src);
		setZ(src == 0);
		this.writeResult((dest > src) ? dest : src);
	}
}

class Min extends Instruction {

	public Min(COG cog) {
		super(cog);
		// TODO Auto-generated constructor stub
	}
	
	protected void execute() {
		
		long dest = ((long) this.getDestination()) & INT_MASK;
		long src  = ((long) this.getDestination()) & INT_MASK;
		
		setC(dest < src);
		setZ(src == 0);
		this.writeResult((dest < src) ? (int) dest : (int) src);
	}
}

class Max extends Instruction {

	public Max(COG cog) {
		super(cog);
		// TODO Auto-generated constructor stub
	}
	
	protected void execute() {
		
		long dest = ((long) this.getDestination()) & INT_MASK;
		long src  = ((long) this.getDestination()) & INT_MASK;
		
		setC(dest > src);
		setZ(src == 0);
		this.writeResult((dest > src) ? (int) dest : (int) src);
	}
}

class Movs extends Instruction {
	
	public Movs(COG cog) {
		super(cog);
		// TODO Auto-generated constructor stub
	}

	protected void execute() {
		
		int result = (this.getDestination() & ((int) INT_MASK ^ SRC_MASK)) | (this.getSource() & SRC_MASK);
		
		setZ(result == 0);
		setC(false);
	}
}

class Movd extends Instruction {
	
	public Movd(COG cog) {
		super(cog);
		// TODO Auto-generated constructor stub
	}

	protected void execute() {
		
		int result = (this.getDestination() & ((int) INT_MASK ^ DEST_MASK)) | ((this.getSource() & SRC_MASK) << DEST_MASK);
		
		setZ(result == 0);
		setC(false);
	}
}

class Movi extends Instruction {
	
	public Movi(COG cog) {
		super(cog);
		// TODO Auto-generated constructor stub
	}

	protected void execute() {
		
		int result = (this.getDestination() & ((int) INT_MASK ^ RFLG_MASK)) | ((this.getSource() & SRC_MASK) << RFLG_MASK);
		
		setZ(result == 0);
		setC(false);
	}
}

class Jmpret extends Instruction {
	
	public Jmpret(COG cog) {
		super(cog);
		// TODO Auto-generated constructor stub
	}

	protected void execute() {
		
		cog.cogram[this.getDestination()] = (cog.cogram[this.getDestination()] & (int) INT_MASK ^ SRC_MASK) | (cog.progcounter - 1); // subtract 1 since we queue two instructions
		cog.progcounter = this.getSource();
		cog.advanceCount();
		
		setC(true);
		setZ(this.getDestination() == 0);
	}
}

class And extends Instruction {
	
	public And(COG cog) {
		super(cog);
		// TODO Auto-generated constructor stub
	}

	protected void execute() {
		
		int result = this.getDestination() & this.getSource();
		
		this.writeResult(result);
		setZ(result == 0);
		
		// calculate parity
		for (int i=0; i<32; i++) {
			result = (result >>> 1) ^ (result & 1);
		}
		
		setC(result == 1);
	}
}

class Andn extends Instruction {
	
	public Andn(COG cog) {
		super(cog);
		// TODO Auto-generated constructor stub
	}

	protected void execute() {
		
		int result = this.getDestination() & (this.getSource() ^ (int) INT_MASK);
		
		this.writeResult(result);
		setZ(result == 0);
		
		// calculate parity
		for (int i=0; i<32; i++) {
			result = (result >>> 1) ^ (result & 1);
		}
		
		setC(result == 1);
	}
}

class Or extends Instruction {
	
	public Or(COG cog) {
		super(cog);
		// TODO Auto-generated constructor stub
	}

	protected void execute() {
		
		int result = this.getDestination() | this.getSource();
		
		this.writeResult(result);
		setZ(result == 0);
		
		// calculate parity
		for (int i=0; i<32; i++) {
			result = (result >>> 1) ^ (result & 1);
		}
		
		setC(result == 1);
	}
}

class Xor extends Instruction {
	
	public Xor(COG cog) {
		super(cog);
		// TODO Auto-generated constructor stub
	}

	protected void execute() {
		
		int result = this.getDestination() ^ this.getSource();
		
		this.writeResult(result);
		setZ(result == 0);
		
		// calculate parity
		for (int i=0; i<32; i++) {
			result = (result >>> 1) ^ (result & 1);
		}
		
		setC(result == 1);
	}
}

class Muxc extends Instruction {
	
	public Muxc(COG cog) {
		super(cog);
		// TODO Auto-generated constructor stub
	}
	
	protected void execute() {
		
		int result = (cog.cflag == 1) ? this.getDestination() | this.getSource() : this.getDestination() & ((int) INT_MASK ^ this.getSource());
		
		this.writeResult(result);
		setZ(result == 0);
		
		// calculate parity
		for (int i=0; i<32; i++) {
			result = (result >>> 1) ^ (result & 1);
		}
		
		setC(result == 1);
	}
}

class Muxnc extends Instruction {
	
	public Muxnc(COG cog) {
		super(cog);
		// TODO Auto-generated constructor stub
	}
	
	protected void execute() {
		
		int result = (cog.cflag == 0) ? this.getDestination() | this.getSource() : this.getDestination() & ((int) INT_MASK ^ this.getSource());
		
		this.writeResult(result);
		setZ(result == 0);
		
		// calculate parity
		for (int i=0; i<32; i++) {
			result = (result >>> 1) ^ (result & 1);
		}
		
		setC(result == 1);
	}
}

class Muxz extends Instruction {
	
	public Muxz(COG cog) {
		super(cog);
		// TODO Auto-generated constructor stub
	}
	
	protected void execute() {
		
		int result = (cog.zflag == 1) ? this.getDestination() | this.getSource() : this.getDestination() & ((int) INT_MASK ^ this.getSource());
		
		this.writeResult(result);
		setZ(result == 0);
		
		// calculate parity
		for (int i=0; i<32; i++) {
			result = (result >>> 1) ^ (result & 1);
		}
		
		setC(result == 1);
	}
}

class Muxnz extends Instruction {
	
	public Muxnz(COG cog) {
		super(cog);
		// TODO Auto-generated constructor stub
	}
	
	protected void execute() {
		
		int result = (cog.zflag == 0) ? this.getDestination() | this.getSource() : this.getDestination() & ((int) INT_MASK ^ this.getSource());
		
		this.writeResult(result);
		setZ(result == 0);
		
		// calculate parity
		for (int i=0; i<32; i++) {
			result = (result >>> 1) ^ (result & 1);
		}
		
		setC(result == 1);
	}
}

class Add extends Instruction {
	
	public Add(COG cog) {
		super(cog);
		// TODO Auto-generated constructor stub
	} 

	protected void execute() {
		
		long result = (long) this.getDestination() & INT_MASK;
		result += (long) this.getSource() & INT_MASK;
		
		this.writeResult((int)result);
		setZ(((int) result) == 0);
		setC((result & (1 + INT_MASK)) > INT_MASK);
	}
}

class Sub extends Instruction {
	
	public Sub(COG cog) {
		super(cog);
		// TODO Auto-generated constructor stub
	} 

	protected void execute() {
		
		long result = (long) this.getDestination() & INT_MASK;
		result -= (long) this.getSource() & INT_MASK;
		
		this.writeResult((int)result);
		setZ(((int) result) == 0);
		setC((result & (1 + INT_MASK)) > INT_MASK);
	}
}

class Addabs extends Instruction {
	
	public Addabs(COG cog) {
		super(cog);
		// TODO Auto-generated constructor stub
	} 

	protected void execute() {
		
		long result = (long) this.getDestination() & INT_MASK;
		result += (long) Math.abs(this.getSource()) & INT_MASK;
		
		this.writeResult((int)result);
		setZ(((int) result) == 0);
		setC((result & (1 + INT_MASK)) > INT_MASK);
	}
}

class Subabs extends Instruction {
	
	public Subabs(COG cog) {
		super(cog);
		// TODO Auto-generated constructor stub
	} 

	protected void execute() {
		
		long result = (long) this.getDestination() & INT_MASK;
		result -= (long) Math.abs(this.getSource()) & INT_MASK;
		
		this.writeResult((int)result);
		setZ(((int) result) == 0);
		setC((result & (1 + INT_MASK)) > INT_MASK);
	}
}