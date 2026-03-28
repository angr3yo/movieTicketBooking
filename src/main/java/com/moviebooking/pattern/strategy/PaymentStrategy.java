package com.moviebooking.pattern.strategy;

/**
 * STRATEGY PATTERN (Behavioral)
 * ──────────────────────────────
 * Defines a family of payment algorithms (UPI, Card, Net Banking, Wallet).
 * Each strategy is interchangeable at runtime.
 *
 * SOLID Open/Closed: New payment methods can be added
 * without changing existing code.
 * GRASP Low Coupling: PaymentContext doesn't depend on
 * concrete payment implementations.
 */

// ─── Strategy Interface ─────────────────────────────────────────────────────
public interface PaymentStrategy {
    boolean pay(double amount);
    String getPaymentMethodName();
}

// ─── Concrete Strategy: UPI ─────────────────────────────────────────────────
class UpiPaymentStrategy implements PaymentStrategy {
    private final String upiId;

    public UpiPaymentStrategy(String upiId) {
        this.upiId = upiId;
    }

    @Override
    public boolean pay(double amount) {
        System.out.println("Processing UPI payment of ₹" + amount + " via " + upiId);
        // Integrate actual UPI gateway here
        return true;
    }

    @Override
    public String getPaymentMethodName() { return "UPI"; }
}

// ─── Concrete Strategy: Card ─────────────────────────────────────────────────
class CardPaymentStrategy implements PaymentStrategy {
    private final String cardNumber;
    private final String cvv;

    public CardPaymentStrategy(String cardNumber, String cvv) {
        this.cardNumber = cardNumber;
        this.cvv = cvv;
    }

    @Override
    public boolean pay(double amount) {
        System.out.println("Processing Card payment of ₹" + amount +
                " via card ending " + cardNumber.substring(cardNumber.length() - 4));
        // Integrate actual card gateway here
        return true;
    }

    @Override
    public String getPaymentMethodName() { return "CARD"; }
}

// ─── Concrete Strategy: Net Banking ─────────────────────────────────────────
class NetBankingPaymentStrategy implements PaymentStrategy {
    private final String bankName;

    public NetBankingPaymentStrategy(String bankName) {
        this.bankName = bankName;
    }

    @Override
    public boolean pay(double amount) {
        System.out.println("Processing Net Banking payment of ₹" + amount + " via " + bankName);
        return true;
    }

    @Override
    public String getPaymentMethodName() { return "NET_BANKING"; }
}

// ─── Concrete Strategy: Wallet ───────────────────────────────────────────────
class WalletPaymentStrategy implements PaymentStrategy {
    private final String walletId;

    public WalletPaymentStrategy(String walletId) {
        this.walletId = walletId;
    }

    @Override
    public boolean pay(double amount) {
        System.out.println("Processing Wallet payment of ₹" + amount + " via wallet " + walletId);
        return true;
    }

    @Override
    public String getPaymentMethodName() { return "WALLET"; }
}

// ─── Context ─────────────────────────────────────────────────────────────────
class PaymentContext {
    private PaymentStrategy strategy;

    public void setStrategy(PaymentStrategy strategy) {
        this.strategy = strategy;
    }

    public boolean executePayment(double amount) {
        if (strategy == null) throw new IllegalStateException("No payment strategy set.");
        return strategy.pay(amount);
    }

    public String getMethodName() {
        return strategy != null ? strategy.getPaymentMethodName() : "NONE";
    }
}

// ─── Factory Helper ──────────────────────────────────────────────────────────
class PaymentStrategyFactory {
    public static PaymentStrategy getStrategy(String method, String detail1, String detail2) {
        return switch (method.toUpperCase()) {
            case "UPI"         -> new UpiPaymentStrategy(detail1);
            case "CARD"        -> new CardPaymentStrategy(detail1, detail2);
            case "NET_BANKING" -> new NetBankingPaymentStrategy(detail1);
            case "WALLET"      -> new WalletPaymentStrategy(detail1);
            default            -> throw new IllegalArgumentException("Unknown payment method: " + method);
        };
    }
}
