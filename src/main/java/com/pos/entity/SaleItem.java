package main.java.com.pos.entity;

import java.math.BigDecimal;

public class SaleItem {
    private Long id;
    private Long medicineId;
    private String medicineNameSnapshot;;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal discountAmount;
    private BigDecimal lineTotal;

    public SaleItem(Long medicineId, String medicineNameSnapshot, int quantity, BigDecimal unitPrice,
            BigDecimal discountAmount) {
        this.medicineId = Objects.requireNonNull(medicineId, "ID obat tidak boleh null");
        this.medicineNameSnapshot = Objects.requireNonNull(medicineNameSnapshot, "Nama obat tidak boleh null");
        this.quantity = quantity;
        this.unitPrice = Objects.requireNonNull(unitPrice, "Harga satuan tidak boleh null");
        this.discountAmount = Objects.requireNonNull(discountAmount, "Jumlah diskon tidak boleh null");
        this.lineTotal = calculateLineTotal();

        validateQuantity();
        validateDiscount();
    }

    public BigDecimal calculatedLineTotal() {
        validateQuantity();
        validateDiscount();

        lineTotal = unitPrice.multiply(BigDecimal.valueOf(quantity)).subtract(discountAmount);
        return lineTotal;
    }

    public void changeQuantity(int newQuantity) {
        if (newQuantity <= 0) {
            throw new IllegalArgumentException("Kuantitas harus lebih besar dari 0");
        }

        BigDecimal newSubtotal = unitPrice.multiply(BigDecimal.valueOf(newQuantity));

        if (discountAmount.compareTo(newSubtotal) > 0) {
            throw new IllegalArgumentException("Jumlah diskon tidak boleh lebih besar dari total harga item");
        }
        this.quantity = newQuantity;
        calculatedLineTotal();
    }

    public void validateQuantity() {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Kuantitas harus lebih besar dari 0");
        }
    }

    public void validateDiscount() {
        if (unitPrice == null || discountAmount == null) {
            throw new IllegalArgumentException("Harga satuan dan jumlah diskon tidak boleh null");
        }

        if (unitPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Harga satuan tidak boleh negatif");
        }

        if (discountAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Jumlah diskon tidak boleh negatif");
        }

        BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity));

        if (discountAmount.compareTo(subtotal) > 0) {
            throw new IllegalArgumentException("Diskon melebihi subtotal");
        }
    }

    public Long getId() {
        return id;
    }

    public Long getMedicineId() {
        return medicineId;
    }

    public String getMedicineNameSnapshot() {
        return medicineNameSnapshot;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public BigDecimal getLineTotal() {
        return lineTotal;
    }
}