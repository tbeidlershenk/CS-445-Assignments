public class BlackjackCards extends RandIndexQueue<Card> {

    private int[] currValues;
    
    public BlackjackCards (int sz) {
        super(sz);
        currValues = new int[20];
    }

    /** Stores the value of the RandIndexQueue of Card objects in int val, then return val
     * @return val
     */
    public int getValue () {
        int val = 0;
        for (int i = 0; i < size(); i++) {
            if (val + get(i).value() <= 21) {
                val += get(i).value();
                currValues[i] = get(i).value();
            } else if (val + get(i).value2() <= 21) {
                val += get(i).value2();
                currValues[i] = get(i).value2();
            } else {
                val = 0;
                boolean found = false;
                int arrLen = i;
                for (int j = 0; j < arrLen; j++) {
                    if (currValues[j] == 11 && !found) {
                        currValues[j] = 1;
                        i--;
                        found = true;
                    }
                    val += currValues[j];
                }
                if (!found) val += get(i).value();
            }   
            

        }
        return val;
    }
    
}
