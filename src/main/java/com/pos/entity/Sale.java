package main.java.com.pos.entity;

import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Objects;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Sale {
    private Long id;
    private String transactionNumber;
    private Long cashierId;
    private List<SaleItem> items;
    private BigDecimal subTotal;
    private BigDecimal discountTotal;
    private BigDecimal total;
    private boolean completed;
    private boolean cancelled;
    private LocalDateTime createdAt;

    public Sale(String transactionNumber, Long cashierId) {
        this.transactionNumber = Objects.requireNonNull(transactionNumber, "Nomor transaksi tidak boleh null");
        this.cashierId = Objects.requireNonNull(cashierId, "ID kasir tidak boleh null");
        this.items = new ArrayList<>();
        this.subTotal = BigDecimal.ZERO;
        this.discountTotal = BigDecimal.ZERO;
        this.total = BigDecimal.ZERO;

        this.completed = false;
        this.cancelled = false;
        this.createdAt = LocalDateTime.now();
    }

    public void addItem(SaleItem item) {
        validateTransaction();

        Objects.requireNonNull(item, "Item tidak boleh null");
        item.calculteLineTotal();

        items.add(item);
        calculatedTotal();
    }

    public void removeItem(Long itemId) {
        validateTransaction();

        Objects.requireNonNull(itemId, "ID item tidak boleh null");
        boolean removed = items.removeIf(item -> itemId.getId().equals(item.getId()));

        if (!removed) {
            throw new IllegalArgumentException("Item dengan ID " + itemId + " tidak ditemukan dalam transaksi");
        }
        calculatedTotal();
    }

    public BigDecimal calculatedSubtotal() {
        subtotal = BigDecimal.ZERO;
        for (saleItem item : items) {
            BigDecimal itemSubtotal = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            subtotal = subtotal.add(itemSubtotal);
        }
        return subtotal;
    }

    public BigDecimal calculatedTotal() {
        calculatedSubtotal();
        discountTotal = BigDecimal.ZERO;
        for (saleItem item : items) {
            discountTotal = discountTotal.add(item.getDiscountAmount());
        }
        total = subtotal.subtract(discountTotal);
        return total;
    }

    public void applyPayment(Payment payment) {

    }

    public void markCompleted() {
        validateTransaction();
        if (items.isEmpty()) {
            throw new IllegalStateException("Transaksi tidak memiliki item");
        }
        calculatedTotal();
        completed = true;
    }

    public void cancelPendingSale(String reason) {
        if (completed) {
            throw new IllegalStateException("Transaksi sudah selesai");
        }

        if (cancelled) {
            throw new IllegalStateException("Transaksi sudah dibatalkan");
        }

        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("Alasan pembatalan tidak boleh kosong");
        }
        cancelled = true;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void validateTransaction() {
        if (completed || cancelled) {
            throw new IllegalStateException("Transaksi tidak dapat diubah");
        }
    }

    public Long getId() {
        return id;
    }

    public String getTransactionNumber() {
        return transactionNumber;
    }

    public Long getCashierId() {
        return cashierId;
    }

    public List<SaleItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public BigDecimal getDiscountTotal() {
        return discountTotal;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
