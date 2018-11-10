package org.greece.plutus.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by hujiahua on 2018/11/10.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DayStockTrade {
    private String tradeDate;
    private Float openingPrice;
    private Float closingPrice;
    private Float minPrice;
    private Float maxPrice;
    private Long tradeShare;
    private Long tradeAmount;


    public String toCvsLine(){
        String delimiter = ",";
        StringBuilder sb = new StringBuilder();
        sb.append(tradeDate).append(delimiter)
                .append(openingPrice).append(delimiter)
                .append(closingPrice).append(delimiter)
                .append(minPrice).append(delimiter)
                .append(maxPrice).append(delimiter)
                .append(tradeShare).append(delimiter)
                .append(tradeAmount).append(delimiter);
        return sb.toString();
    }

    public static DayStockTrade fromCvsLine(String line){
        String delimiter = ",";
        String [] fields = line.split(delimiter);
        DayStockTrade dayStockTrade = new DayStockTrade();
        dayStockTrade.setTradeDate(fields[0]);
        dayStockTrade.setOpeningPrice(Float.valueOf(fields[1]));
        dayStockTrade.setMaxPrice(Float.valueOf(fields[2]));
        dayStockTrade.setClosingPrice(Float.valueOf(fields[3]));
        dayStockTrade.setMinPrice(Float.valueOf(fields[4]));
        dayStockTrade.setTradeShare(Long.valueOf(fields[5]));
        dayStockTrade.setTradeAmount(Long.valueOf(fields[6]));
        return dayStockTrade;
    }

}
