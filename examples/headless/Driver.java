import me.lsdo.processing.*;		
		
public class Driver		
{		
    public static void main(String[] args){		
        long start = System.currentTimeMillis();		
		
        Dome dome = new Dome();		
        OPC opc = new OPC();		
        DomeAnimation animation = new GridTest(dome, opc);		
		
        while(true) {		
            double t = (System.currentTimeMillis() - start) / 1000d;		
            animation.draw(t);		
        }		
    }		
}		
