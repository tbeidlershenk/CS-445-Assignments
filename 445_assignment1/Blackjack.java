public class Blackjack {

    private static BlackjackCards deck;
    private static BlackjackCards discard;
    private static int rounds;
    private static int shoe;

    public static void main (String [] args) {
        
        rounds = Integer.parseInt(args[0]);
        shoe = Integer.parseInt(args[1]);
        int trace = Integer.parseInt(args[2]);
        int playerwins = 0, dealerwins = 0, ties = 0;


        deck = new BlackjackCards(52);
        discard = new BlackjackCards(10);

        // Builds the deck
        for (int i = 0; i < shoe; i++) {
            for (Card.Suits s: Card.Suits.values()) {
                for (Card.Ranks r: Card.Ranks.values()) deck.enqueue(new Card(s, r));
            }
        }

        deck.shuffle();
        int[] totals = {0,0,0};

        // Runs the game for the specified number of rounds
        System.out.println("\nStarting BlackJack game: " + rounds + " rounds, with " + shoe + " decks in the shoe");
        for (int i = 0; i < rounds; i++) {
            boolean print = i < trace;
            int result = playRound(print, i);
            if (result == 0) {
                if (print) System.out.println("Result: Player wins!");
                totals[0]++;
            } else if (result == 1) {
                if (print) System.out.println("Result: Dealer wins!");
                totals[1]++;
            } else {
                if (print) System.out.println("It's a tie!");
                totals[2]++;
            }
        }

        // Prints the results of the rounds
        System.out.println("\nResults after " + rounds + " rounds:\n" 
                + "Player won     - " + totals[0] + " times\n"                                         
                + "Dealer won     - " + totals[1] + " times\n"                                         
                + "Number of ties - " + totals[2] + " times\n" );                                                                             
    }
    /** Plays out one round, prints the round information if print == true
     *  Returns 0 if player won, 1 if dealer won, 2 if tied
     * @param print
     * @param round
     * @return int (0,1 or 2)
     */
    private static int playRound (boolean print, int round) {
        shuffle(round);
        if (print) 
            System.out.println("\nRound " + round + " beginning:");
        BlackjackCards player = new BlackjackCards(4), dealer = new BlackjackCards(4);
        for (int i = 0; i < 4; i++) {
            if (i % 2 == 0) 
                player.enqueue(deck.getFront());
            else 
                dealer.enqueue(deck.getFront());
            deck.dequeue();
        }
        if (print) 
            System.out.println("Player: " + player.toString() + " : " + player.getValue() + "\n" + "Dealer: " + dealer.toString() + " : " + dealer.getValue());
        
        if (!hit(player, "Player", print)) {
            discardHands(player, dealer);
            return 1;
        } else if (!hit(dealer, "Dealer", print)) {
            discardHands(player, dealer);
            return 0;
        } else {
            int result = determineWinner(player, dealer);  
            discardHands(player, dealer);
            return result;
        }  
    }

    /** Determines the winner (between player and dealer)
     *  Returns 0 if player won, 1 if dealer won, 2 if tied
     * @param player
     * @param dealer
     * @return int (0,1 or 2)
     */
    public static int determineWinner (BlackjackCards player, BlackjackCards dealer) {
        boolean pbj = player.size() == 2 && player.getValue() == 21;
        boolean dbj = dealer.size() == 2 && dealer.getValue() == 21;
        if (player.getValue() > dealer.getValue()) return 0;
        if (player.getValue() < dealer.getValue()) return 1;
        else {
            if (pbj && !dbj) return 0;
            if (!pbj && dbj) return 1;
            else return 2;
        }
    }

    /** Returns true if p chose to hit, or false if they bust or stand
     * @param p
     * @param s
     * @param print
     * @return boolean
     */
    public static boolean hit (BlackjackCards p, String s, boolean print) {
        while (true) {
            if (p.getValue() > 16) {
                if (print) System.out.println(s + " STANDS: " + p.toString() + " : " + p.getValue());
                return true;
            } else {
                p.enqueue(deck.getFront());
                deck.dequeue();
                if (p.getValue() > 21) {
                    if (print) System.out.println(s + " BUSTS: " + p.toString() + " : " + p.getValue());
                    return false;
                }
                if (print) System.out.println(s + " HITS: " + p.toString() + " : " + p.getValue());
            }
        }
    }

    /** Discards player and dealer hands (adds their queues to RandIndexQueue discard)
     * @param player
     * @param dealer
     */
    public static void discardHands (BlackjackCards player, BlackjackCards dealer) {
        while (player.size() > 0) {
            discard.enqueue(player.getFront());
            player.dequeue();
        }
        while (dealer.size() > 0) {
            discard.enqueue(dealer.getFront());
            dealer.dequeue();
        }
    }

    /** If deck is at a quarter it's original size:
     *  All cards from queue discard returned to queue deck, then shuffles deck
     * @param round
     */
    private static void shuffle (int round) {
        if (deck.size() <= (52 * shoe) / 4) {
            System.out.println("\nReshuffling the shoe in round " + round);
            while (discard.size() > 0) {
                deck.enqueue(discard.getFront());
                discard.dequeue();
            } 
            deck.shuffle();
        } 
    }

}
