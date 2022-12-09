public class Multiplier extends Thread{
    private double multi = 2;
    private double i = 100;
    public Multiplier() {
    }

    @Override
    public void run() {
        while(true){
            multi();
        }
    }

    public void multi(){
        if(i >= 1) {
            multi = 1 + i / 100;
            i--;
        }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    public double getMulti() {
        return multi;
    }

    public void setI(double i) {
        this.i = i;
    }
}
