// ================= BLACKJACK GAME =================
// Java console-based Blackjack game.
// Player plays against computer-controlled dealer following Blackjack rules.
// This project demonstrates Java Collections, loops, conditions, scanning input
// and logical program structuring in a real-world style application.
// ===================================================

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class Blackjack {

    // ======================= CARD VALUE STORAGE =============================
    // HashMap stores card ranks with their values
    // Example: "K" -> 10, "A" -> 11 initially
    private static final Map<String, Integer> CARD_VALUES = new HashMap<>();

    // Static block runs once when program loads
    static {
        // Creating list of possible card ranks
        List<String> ranks = Arrays.asList("2","3","4","5","6","7","8","9","10","J","Q","K","A");

        // Assigning values to each card
        for (String r : ranks) {
            switch (r) {
                case "J": case "Q": case "K":
                    CARD_VALUES.put(r, 10); // Face cards are always 10
                    break;
                case "A":
                    CARD_VALUES.put(r, 11); // Ace initially counts as 11
                    break;
                default:
                    CARD_VALUES.put(r, Integer.parseInt(r)); // Number cards are their face value
            }
        }
    }

    // ======================= DECK CREATION FUNCTION =========================
    // This function creates a deck of 52 cards
    // Each rank appears 4 times (4 suits)
    // Deck is shuffled randomly before returning
    private static List<String> createDeck() {
        List<String> deck = new ArrayList<>();
        List<String> ranks = new ArrayList<>(CARD_VALUES.keySet());

        // Adding each card rank 4 times (like real deck)
        for (String rank : ranks) {
            for (int i = 0; i < 4; i++) deck.add(rank);
        }

        // Shuffle the deck randomly
        Collections.shuffle(deck, new Random());
        return deck;
    }

    // ======================= SCORE CALCULATION ==============================
    // This calculates total score of cards in a player's hand
    // It also handles Ace smartly
    // If total > 21 and Ace exists, Ace becomes 1 instead of 11
    private static int sumOfCards(List<String> hand) {
        int score = 0;
        int aces = 0; // Track number of Aces

        // Add card values
        for (String c : hand) {
            score += CARD_VALUES.get(c);
            if (c.equals("A")) aces++;
        }

        // Adjust Ace value if required
        while (score > 21 && aces > 0) {
            score -= 10; // Convert Ace from 11 → 1
            aces--;
        }

        return score;
    }

    // ======================= DISPLAY FUNCTION ===============================
    // Shows Player & Dealer cards
    // hideDealer = true → hides dealer 2nd card initially
    private static void display(List<String> phand, List<String> dhand, boolean hideDealer) {
        System.out.println();
        System.out.println("Player's hand: " + phand + "  Score: " + sumOfCards(phand));

        if (hideDealer) {
            System.out.println("Dealer's hand: [" + dhand.get(0) + ", ?]");
        } else {
            System.out.println("Dealer's hand: " + dhand + "  Score: " + sumOfCards(dhand));
        }
    }

    // ======================= MAIN FUNCTION =================================
    // This handles the complete game process
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Create & shuffle deck
        List<String> deck = createDeck();

        // Lists to store cards of player & dealer
        List<String> phand = new ArrayList<>();
        List<String> dhand = new ArrayList<>();

        // Initial dealing: 2 cards each
        phand.add(deck.remove(deck.size() - 1));
        phand.add(deck.remove(deck.size() - 1));
        dhand.add(deck.remove(deck.size() - 1));
        dhand.add(deck.remove(deck.size() - 1));

        System.out.println("Welcome to Blackjack!\n");

        // ======================= PLAYER TURN =================================
        while (true) {
            display(phand, dhand, true); // Show cards but hide dealer second card

            System.out.print("Do you want to hit(h) or stand(s)? ");
            String choice = scanner.nextLine().trim().toLowerCase();

            if (choice.equals("h")) {
                // Give player another card
                if (deck.isEmpty()) deck = createDeck();
                phand.add(deck.remove(deck.size() - 1));

                // Check if player busts
                if (sumOfCards(phand) > 21) {
                    display(phand, dhand, false);
                    System.out.println("Player busts! Dealer wins.");
                    scanner.close();
                    return;
                }

            } else if (choice.equals("s")) {
                // Player stops → dealer turn begins
                break;
            } else {
                System.out.println("Invalid. Please type 'h' to hit or 's' to stand.");
            }
        }

        // ======================= DEALER TURN ================================
        display(phand, dhand, false); // Show full cards now

        // Dealer keeps drawing cards until score >= 17
        while (sumOfCards(dhand) < 17) {
            System.out.println();
            System.out.println("Dealer hits...");

            if (deck.isEmpty()) deck = createDeck();
            dhand.add(deck.remove(deck.size() - 1));

            display(phand, dhand, false);
        }

        // ======================= RESULT SECTION =============================
        int dealerScore = sumOfCards(dhand);
        int playerScore = sumOfCards(phand);

        if (dealerScore > 21) {
            System.out.println("Dealer busts! Player wins.");
        } else if (playerScore > dealerScore) {
            System.out.println("Player wins!");
        } else if (playerScore < dealerScore) {
            System.out.println("Dealer wins!");
        } else {
            System.out.println("It's a tie!");
        }

        scanner.close();
    }
}
