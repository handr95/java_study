public class Quote {
    private final String shopNm;
    private final double price;
    private final Discount.Code discountCode;

    public Quote(String shopNm, double price, Discount.Code discountCode) {
        this.shopNm = shopNm;
        this.price = price;
        this.discountCode = discountCode;
    }

    public static Quote parse (String s) {
        String[] split = s.split(":");
        String shopNm = split[0];
        double price = Double.parseDouble(split[1]);
        Discount.Code discountCode = Discount.Code.valueOf(split[2]);
        return new Quote(shopNm, price, discountCode);
    }

    public String getShopNm() {
        return shopNm;
    }

    public double getPrice() {
        return price;
    }

    public Discount.Code getDiscountCode() {
        return discountCode;
    }
}
