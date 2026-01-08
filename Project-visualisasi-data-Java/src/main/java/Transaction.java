import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaction {
    private Long id;

    // Menandakan format tanggal agar Jackson tidak bingung 
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "M/d/yyyy")
    private LocalDate transaction_data;

    private BigDecimal amount;
    private String category;
    private String payment_method;
    private String description;
    private Boolean isIncome;

    // Constructor
    public Transaction(String category, BigDecimal amount) {
        this.category = category;
        this.amount = amount;
    }

    // Default constructor for Jackson
    public Transaction() {}

    // Getters
    public LocalDate getTransactionDate() {
        return transaction_data;
    }

    public String getCategory() {
        return category;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getPaymentMethod() {
        return payment_method;
    }

    // Setters (optional, for completeness)
    public void setTransactionDate(LocalDate transactionDate) {
        this.transaction_data = transactionDate;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.payment_method = paymentMethod;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }
}